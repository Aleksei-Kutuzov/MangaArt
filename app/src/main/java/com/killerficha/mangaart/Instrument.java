package com.killerficha.mangaart;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

//import org.opencv.android.Utils;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;

public class Instrument {
    Paint paint;
    Paint eraserPaint; // Paint для ластика

    public enum mode_list {PENCIL, MARKER, ERASER, FILL, NO_EDIT} // режимы: карандаш, маркер, ластик, заливка и маштабирование
    static mode_list mode = mode_list.PENCIL; // по дефолту **PENCIL**

    Instrument(int color, int thickness) {
        paint = new Paint(ANTI_ALIAS_FLAG); // это сглаживание, но оно может немного снижать производительность
        paint.setColor(color);  // Цвет линии
        paint.setStrokeWidth(thickness); // Толщина линии
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND); // Круглые концы
        paint.setStrokeJoin(Paint.Join.ROUND); // Круглые соединения

        // Инициализация Paint для ластика
        eraserPaint = new Paint(ANTI_ALIAS_FLAG);
        eraserPaint.setStrokeWidth(thickness); // Толщина ластика
        eraserPaint.setStyle(Paint.Style.STROKE);
        eraserPaint.setStrokeCap(Paint.Cap.ROUND); // Круглые концы
        eraserPaint.setStrokeJoin(Paint.Join.ROUND); // Круглые соединения
        eraserPaint.setColor(Color.WHITE); // Цвет ластика
    }

    Instrument() {
        this(Color.BLACK, 5);
    }

    void setColor(int color) {
        paint.setColor(color);
    }

    int getColor() {
        return paint.getColor();
    }

    void setThickness(int thickness) {
        paint.setStrokeWidth(thickness);
        eraserPaint.setStrokeWidth(thickness); // Обновляем толщину ластика
    }

    int getThickness() {
        return (int) paint.getStrokeWidth();
    }

    public mode_list getMode() {
        return mode;
    }

    public void setMode(mode_list mode) {
        this.mode = mode;
    }

    public void execute(MotionEvent event, List<DrawableObject> freeLines, List<DrawableObject> deletedLines, EditorDraw ED) {
        switch (this.mode) {
            case PENCIL:
                deletedLines.clear();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Начало рисования
                    DrawableObject newDrawable = new FreeLine(event.getX(), event.getY());
                    newDrawable.setPaint(new Paint(paint));
                    freeLines.add(newDrawable);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // Рисуем линию при движении
                    DrawableObject current = freeLines.get(freeLines.size() - 1);
                    current.lineTo(event.getX(), event.getY());
                }
                break;
            case MARKER:
                deletedLines.clear();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Начало рисования
                    DrawableObject newDrawable = new StraightLine(event.getX(), event.getY());
                    newDrawable.setPaint(new Paint(paint));
                    freeLines.add(newDrawable);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // Рисуем линию при движении
                    DrawableObject current = freeLines.get(freeLines.size() - 1);
                    current.lineTo(event.getX(), event.getY());
                }
                break;
            case ERASER:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Начало рисования
                    DrawableObject newDrawable = new FreeLine(event.getX(), event.getY());
                    newDrawable.setPaint(new Paint(eraserPaint));
                    freeLines.add(newDrawable);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // Рисуем линию при движении
                    DrawableObject current = freeLines.get(freeLines.size() - 1);
                    current.lineTo(event.getX(), event.getY());
                }
                break;
            case FILL:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Получаем Bitmap из EditorDraw
                    Bitmap bitmap = ED.getBitmap();
                    if (bitmap == null) return;

                    // Получаем координаты клика
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    // Цвет заливки (текущий цвет инструмента)
                    int newColor = paint.getColor();

                    // Запускаем Scanline Fill в новом потоке
                    new Thread(() -> {
                        // Выполняем Scanline Fill и получаем Bitmap с заливкой
                        Bitmap filledBitmap = ScanlineFill(bitmap, x, y, newColor);

                        // Возвращаем результат в UI-поток
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (filledBitmap != null) {
                                // Создаем DrawableObject с залитым Bitmap
                                DrawableObject newDrawable = new BitmapObject(filledBitmap);
                                freeLines.add(newDrawable);
                                // Обновляем Canvas
                                ED.invalidate();
                            }
                        });
                    }).start();
                }
                break;
        }
        ED.invalidate(); // Перерисовываем экран
    }


    public Bitmap ScanlineFill(Bitmap bitmap, int x, int y, int newColor) {
        // Проверяем, что Bitmap не null
        if (bitmap == null) {
            Log.e("ScanlineFill", "Bitmap is null");
            return null;
        }

        // Проверяем, что координаты внутри границ
        if (x < 0 || x >= bitmap.getWidth() || y < 0 || y >= bitmap.getHeight()) {
            Log.e("ScanlineFill", "Coordinates are out of bounds");
            return null;
        }

        // Получаем цвет начальной точки
        int targetColor = bitmap.getPixel(x, y);

        // Если целевой цвет совпадает с новым, возвращаем пустой Bitmap
        if (targetColor == newColor) {
            Log.d("ScanlineFill", "Target color is the same as new color");
            return null;
        }

        // Создаем новый Bitmap с прозрачным фоном
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Очередь для хранения отрезков строк, которые нужно обработать
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x, x, y}); // x1, x2, y

        // Одномерный массив для отслеживания посещенных точек
        boolean[] visited = new boolean[bitmap.getWidth() * bitmap.getHeight()];

        // Пока очередь не пуста, обрабатываем отрезки строк
        while (!queue.isEmpty()) {
            int[] segment = queue.remove();
            int x1 = segment[0];
            int x2 = segment[1];
            int currentY = segment[2];

            // Находим границы строки
            while (x1 >= 0 && bitmap.getPixel(x1, currentY) == targetColor && !visited[x1 + currentY * bitmap.getWidth()]) x1--;
            while (x2 < bitmap.getWidth() && bitmap.getPixel(x2, currentY) == targetColor && !visited[x2 + currentY * bitmap.getWidth()]) x2++;

            // Заливаем строку
            for (int i = x1 + 1; i < x2; i++) {
                resultBitmap.setPixel(i, currentY, newColor);
                visited[i + currentY * bitmap.getWidth()] = true;
            }

            // Проверяем строки выше и ниже
            if (currentY > 0) {
                for (int i = x1 + 1; i < x2; i++) {
                    if (bitmap.getPixel(i, currentY - 1) == targetColor && !visited[i + (currentY - 1) * bitmap.getWidth()]) {
                        queue.add(new int[]{i, i, currentY - 1});
                    }
                }
            }
            if (currentY < bitmap.getHeight() - 1) {
                for (int i = x1 + 1; i < x2; i++) {
                    if (bitmap.getPixel(i, currentY + 1) == targetColor && !visited[i + (currentY + 1) * bitmap.getWidth()]) {
                        queue.add(new int[]{i, i, currentY + 1});
                    }
                }
            }
        }

        return resultBitmap;
    }
}