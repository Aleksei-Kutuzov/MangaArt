package com.killerficha.mangaart;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Stack;


class Instrument {
    Paint paint;
    Paint markerPaint;

    Stack<Point> stack;
    Paint eraserPaint; // Paint для ластика

    enum mode_list {PENCIL, MARKER, ERASER, FILL, VECTOR} // режимы: карандаш, маркер, ластик, заливка

    mode_list mode = mode_list.PENCIL; // по дефолту **PENCIL**

    Instrument(int color, int thickness){
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
        eraserPaint.setColor(Color.WHITE); //хыфвщхвпфшзщявхпашщзаыяфгзщгыфавщзгфавфыгавшывфагщывящазхгвщзыфгхашгфывхфгащш

        markerPaint = new Paint(ANTI_ALIAS_FLAG); // это сглаживание, но оно может немного снижать производительность
        markerPaint.setColor(color);  // Цвет линии
        markerPaint.setStrokeWidth(thickness); // Толщина линии
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeCap(Paint.Cap.ROUND); // Круглые концы
        markerPaint.setStrokeJoin(Paint.Join.ROUND); // Круглые соединения
        markerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        markerPaint.setAntiAlias(true);
        markerPaint.setAlpha(1);



    }

    Instrument(){
        this(Color.BLACK, 5);
    }

    void setColor(int color) {
        paint.setColor(color);
    }

    int getColor() {
        return paint.getColor();
    }

    public void floodFill(Bitmap bitmap, int x, int y, int targetColor, int fillColor) {
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            if (p.x < 0 || p.x >= bitmap.getWidth() || p.y < 0 || p.y >= bitmap.getHeight()) continue;

            int currentColor = bitmap.getPixel(p.x, p.y);
            if (currentColor != targetColor || currentColor == fillColor) continue;

            bitmap.setPixel(p.x, p.y, fillColor);

            stack.push(new Point(p.x + 1, p.y)); // Вправо
            stack.push(new Point(p.x - 1, p.y)); // Влево
            stack.push(new Point(p.x, p.y + 1)); // Вниз
            stack.push(new Point(p.x, p.y - 1)); // Вверх
        }
    }



    void setThickness(int thickness) {
        paint.setStrokeWidth(thickness);
        eraserPaint.setStrokeWidth(thickness); // Обновляем толщину ластика
    }

    int getThickness() {
        return paint.getHinting();
    }

    public mode_list getMode() {
        return mode;
    }


    public void setMode(mode_list mode) {
        this.mode = mode;
    }

    public void execute(MotionEvent event, List<DrawableObject> freeLines, List<DrawableObject> deletedlines, EditorDraw ED) /* lines это кастыль, здесь должен быть обьект который содержит все виды рис элементов и которым можно управлять историей */ {
        switch (this.mode) {
            case PENCIL:
                deletedlines.clear();
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
            case MARKER:
                if (this.mode == mode_list.MARKER){
                deletedlines.clear();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Начало рисования
                    DrawableObject newDrawable = new FreeLine(event.getX(), event.getY());
                    newDrawable.setPaint(new Paint(markerPaint));
                    freeLines.add(newDrawable);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // Рисуем линию при движении
                    DrawableObject current = freeLines.get(freeLines.size() - 1);
                    current.lineTo(event.getX(), event.getY());
                }
        }

            case ERASER:
                if (this.mode == mode_list.ERASER) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Начало рисования
                    ;
                    DrawableObject newDrawable = new FreeLine(event.getX(), event.getY());
                    newDrawable.setPaint(new Paint(eraserPaint));
                    freeLines.add(newDrawable);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // Рисуем линию при движении
                    DrawableObject current = freeLines.get(freeLines.size() - 1);
                    current.lineTo(event.getX(), event.getY());
                }
                }
                else{
                    eraserPaint.setXfermode(null);
                }

            case FILL:
                if (this.mode == mode_list.FILL) {
                    // bitmapa = ED.getBitmap();

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        DrawableObject newDrawable = new FreeLine(event.getX(), event.getY());
                        newDrawable.getPath().setFillType(Path.FillType.WINDING);
                        newDrawable.setPaint(new Paint(eraserPaint));
                        freeLines.add(newDrawable);
                        floodFill(ED.getBitmap(), (int) event.getX(), (int) event.getY(), Color.WHITE, getColor());
                        DrawableObject current = freeLines.get(freeLines.size() - 1);
                        current.lineTo(event.getX(), event.getY());
                    }
                }
            default:
                ED.invalidate(); // Перерисовываем экран
        }
    }
}