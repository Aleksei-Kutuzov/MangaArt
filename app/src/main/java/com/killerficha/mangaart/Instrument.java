package com.killerficha.mangaart;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;

import java.util.List;


class Instrument {
    Paint paint;
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
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // Режим CLEAR для стирания
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

    void setThickness(int thickness) {
        paint.setStrokeWidth(thickness);
        eraserPaint.setStrokeWidth(thickness); // Обновляем толщину ластика
    }

    int setThickness() {
        return paint.getHinting();
    }

    public mode_list getMode() {
        return mode;
    }

    public void setMode(mode_list mode) {
        this.mode = mode;
    }

    public void execute(MotionEvent event, List<DrawableObject> freeLines, List<DrawableObject> deletedlines, EditorDraw ED) /* lines это кастыль, здесь должен быть обьект который содержит все виды рис элементов и которым можно управлять историей */ {
        switch (this.mode){
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
                ;
            case ERASER:
                ;
            case FILL:
                ;
            default:
                ED.invalidate(); // Перерисовываем экран
        }
    }
}