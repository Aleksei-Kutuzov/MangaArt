package com.killerficha.mangaart;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import java.util.Stack;

public class EditorDraw extends View {

    private Stack<DrawableObject> freeLines = new Stack<>();

    private Stack<DrawableObject> deletedlines = new Stack<>();

    public Instrument instrument;

    Canvas canvas;


    public EditorDraw(Context context) {
        super(context);
        instrument = new Instrument();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Рисуем все линии
        for (DrawableObject freeLine : freeLines) {
            freeLine.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        instrument.execute(event, freeLines, deletedlines, this);
        return true;
    }

    // Метод для очистки всех линий
    public void clear() {
        deletedlines.addAll(freeLines);
        freeLines.clear();
        invalidate(); // Перерисовываем экран
    }

    // Метод для удаления последней линии
    public void removeLastLine() {
        if (!freeLines.isEmpty()) {
            deletedlines.push(freeLines.pop());
            freeLines.remove(deletedlines.lastElement()); // Удаляем последнюю линию (так там же есть метод last)
            invalidate(); // Перерисовываем экран
        }
    }
    public void restoreLastLine(){
        if (!deletedlines.isEmpty()) {
            freeLines.push(deletedlines.pop()); //Возвращение последней линии
            deletedlines.remove(freeLines.lastElement()); // чистим удалённые
            invalidate(); // Перерисовываем экран
        }
    }
}