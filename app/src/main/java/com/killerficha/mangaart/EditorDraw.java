package com.killerficha.mangaart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class EditorDraw extends View {

    private Stack<Line> lines = new Stack<>();

    private Stack<Line> deletedlines = new Stack<>();

    private Paint paint;

    public EditorDraw(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);  // Цвет линии
        paint.setStrokeWidth(5);      // Толщина линии
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Рисуем все линии
        for (Line line : lines) {
            line.draw(canvas, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        deletedlines.clear();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Начало рисования
            lines.add(new Line(event.getX(), event.getY(), event.getX(), event.getY()));
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // Рисуем линию при движении
            Line currentLine = lines.get(lines.size() - 1);
            currentLine.endX = event.getX();
            currentLine.endY = event.getY();
        }
        invalidate(); // Перерисовываем экран
        return true;
    }

    // Метод для очистки всех линий
    public void clear() {
        deletedlines.addAll(lines);
        lines.clear();
        invalidate(); // Перерисовываем экран
    }

    // Метод для удаления последней линии
    public void removeLastLine() {
        if (!lines.isEmpty()) {
            deletedlines.push(lines.pop());
            lines.remove(deletedlines.lastElement()); // Удаляем последнюю линию (так там же есть метод last)
            invalidate(); // Перерисовываем экран
        }
    }
    public void restoreLastLine(){
        if (!deletedlines.isEmpty()) {
            lines.push(deletedlines.pop()); //Возвращение последней линии
            deletedlines.remove(lines.lastElement()); // чистим удалённые
            invalidate(); // Перерисовываем экран
        }
    }
}
