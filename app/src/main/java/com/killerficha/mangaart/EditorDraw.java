package com.killerficha.mangaart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.ScaleGestureDetector;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Stack;

public class EditorDraw extends View {

    private Stack<DrawableObject> freeLines = new Stack<>();
    private Stack<DrawableObject> deletedlines = new Stack<>();
    public Instrument instrument;
    Canvas canvas;
    Bitmap img_bitmap;

    // Добавляем ScaleGestureDetector и GestureDetector
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private float scaleFactor = 1.0f; // Масштабирование
    private float rotationAngle = 0.0f; // Угол поворота
    private float translationX = 0.0f; // Смещение по X
    private float translationY = 0.0f; // Смещение по Y

    private boolean isEditMode = false; // Режим редактирования (масштабирование/поворот) или рисования

    public EditorDraw(Context context) {
        super(context);
        instrument = new Instrument();

        // Инициализация ScaleGestureDetector и GestureDetector
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawARGB(255, 255, 255, 255);

        // Применяем трансформации: смещение, масштабирование, поворот
        canvas.save();
        canvas.translate(translationX, translationY);
        canvas.scale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
        canvas.rotate(rotationAngle, getWidth() / 2, getHeight() / 2);

        // Ограничиваем область отрисовки
        canvas.clipRect(0, 0, getWidth(), getHeight());

        // Рисуем все линии
        for (DrawableObject freeLine : freeLines) {
            freeLine.draw(canvas);
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEditMode) {
            // Режим редактирования: обрабатываем жесты
            scaleGestureDetector.onTouchEvent(event);
            gestureDetector.onTouchEvent(event);
            return true;
        } else {
            // Режим рисования: передаем события в инструмент
            instrument.execute(event, freeLines, deletedlines, this);
            return true;
        }
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
            freeLines.remove(deletedlines.lastElement()); // Удаляем последнюю линию
            invalidate(); // Перерисовываем экран
        }
    }

    public void restoreLastLine() {
        if (!deletedlines.isEmpty()) {
            freeLines.push(deletedlines.pop()); // Возвращение последней линии
            deletedlines.remove(freeLines.lastElement()); // Чистим удалённые
            invalidate(); // Перерисовываем экран
        }
    }

    public Bitmap getBitmap() {
        // 1. Создаем Bitmap того же размера, что и View
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        // 2. Создаем Canvas, связанный с этим Bitmap
        Canvas canvas = new Canvas(bitmap);

        // 3. Рисуем фон View (если он есть)
        if (getBackground() != null) {
            getBackground().draw(canvas);
        } else {
            // Если фона нет, заливаем Canvas белым цветом (или любым другим)
            canvas.drawColor(Color.WHITE);
        }

        // 4. Вызываем метод draw, передавая наш Canvas
        draw(canvas);

        // 5. Возвращаем Bitmap с результатом рисования
        return bitmap;
    }

    public void switchEditMode() {
        instrument.setMode(Instrument.mode_list.NO_EDIT);
        setEditMode(true);

    }

    // Внутренний класс для обработки масштабирования
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float previousScale = scaleFactor;
            scaleFactor *= detector.getScaleFactor();

            // Ограничиваем масштабирование
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            // Корректируем смещение при масштабировании
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            translationX += (focusX - translationX) * (1 - scaleFactor / previousScale);
            translationY += (focusY - translationY) * (1 - scaleFactor / previousScale);

            invalidate(); // Перерисовываем экран
            return true;
        }
    }

    // Внутренний класс для обработки жестов (например, поворота)
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Обработка прокрутки (смещение)
            translationX -= distanceX / scaleFactor;
            translationY -= distanceY / scaleFactor;
            invalidate(); // Перерисовываем экран
            return true;
        }


        public boolean onRotate(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Обработка поворота
            float deltaX = e2.getX() - e1.getX();
            float deltaY = e2.getY() - e1.getY();
            rotationAngle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
            invalidate(); // Перерисовываем экран
            return true;
        }
    }

    // Метод для переключения режима
    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public boolean isEditMode() {
        return isEditMode;
    }
}