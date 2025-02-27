package com.killerficha.mangaart;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

class Instrument {
    Paint paint;
    Paint eraserPaint; // Paint для ластика

    enum mode_list {PENCIL, MARKER, ERASER, FILL, VECTOR} // режимы: карандаш, маркер, ластик, заливка
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

                    // Получаем цвет пикселя в точке клика
                    int targetColor = bitmap.getPixel(x, y);

                    // Цвет заливки (текущий цвет инструмента)
                    int newColor = paint.getColor();

                    // Вызываем Flood Fill
                    floodFill(bitmap, x, y, targetColor, newColor);

                    // Обновляем Canvas
                    ED.invalidate();
                }
                break;
        }
        ED.invalidate(); // Перерисовываем экран
    }

    // Реализация Flood Fill
    private void floodFill(Bitmap bitmap, int x, int y, int targetColor, int newColor) {
        if (targetColor == newColor) return;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (x < 0 || x >= width || y < 0 || y >= height) return;

        int startColor = bitmap.getPixel(x, y);
        if (startColor != targetColor) return;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.remove();
            if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) continue;

            int currentColor = bitmap.getPixel(p.x, p.y);
            if (currentColor == targetColor) {
                bitmap.setPixel(p.x, p.y, newColor);

                queue.add(new Point(p.x + 1, p.y)); // Вправо
                queue.add(new Point(p.x - 1, p.y)); // Влево
                queue.add(new Point(p.x, p.y + 1)); // Вниз
                queue.add(new Point(p.x, p.y - 1)); // Вверх
            }
        }
    }
}