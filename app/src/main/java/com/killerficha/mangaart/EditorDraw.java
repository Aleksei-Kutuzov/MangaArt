package com.killerficha.mangaart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.ScaleGestureDetector;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.killerficha.mangaart.DrawableComponents.DrawableObject;
import com.killerficha.mangaart.ProjectInstruments.Project;

public class EditorDraw extends View {

    Project project;

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

    // Матрица для трансформаций
    private Matrix transformMatrix = new Matrix();
    private Matrix inverseMatrix = new Matrix();

    public EditorDraw(Context context) {
        super(context);
        instrument = new Instrument();

        // Инициализация ScaleGestureDetector и GestureDetector
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());


        project = new Project();
        project.pageAdd();

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawARGB(255, 255, 255, 255);

        // Применяем трансформации: смещение, масштабирование, поворот
        canvas.save();

        // Создаем матрицу трансформаций
        transformMatrix.reset();
        transformMatrix.postTranslate(translationX, translationY);
        transformMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
        transformMatrix.postRotate(rotationAngle, getWidth() / 2, getHeight() / 2);

        // Применяем матрицу к Canvas
        canvas.concat(transformMatrix);

        // Ограничиваем область отрисовки
        canvas.clipRect(0, 0, getWidth(), getHeight());

        // Рисуем все линии
        for (DrawableObject freeLine : project.getEnabledPage().getDrawableObjects()) {
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
            // Режим рисования: преобразуем координаты и передаем события в инструмент
            MotionEvent transformedEvent = transformEvent(event);
            instrument.execute(transformedEvent,project.getEnabledPage().getDrawableObjects(),
                    project.getEnabledPage().getDeletedDrawableObjects(), this);
            return true;
        }
    }

    // Метод для преобразования координат касаний
    private MotionEvent transformEvent(MotionEvent event) {
        // Копируем событие, чтобы не изменять оригинал
        MotionEvent transformedEvent = MotionEvent.obtain(event);

        // Вычисляем обратную матрицу
        transformMatrix.invert(inverseMatrix);

        // Преобразуем координаты события
        float[] coords = {event.getX(), event.getY()};
        inverseMatrix.mapPoints(coords);
        transformedEvent.setLocation(coords[0], coords[1]);

        return transformedEvent;
    }

    // Метод для очистки всех линий
    public void clear() {
        project.getEnabledPage().getDeletedDrawableObjects().addAll(project.getEnabledPage().getDrawableObjects());
        project.getEnabledPage().getDrawableObjects().clear();
        invalidate(); // Перерисовываем экран
    }

    // Метод для удаления последней линии
    public void removeLastLine() {
        project.getEnabledPage().undo();
        invalidate(); // Перерисовываем экран
    }

    public void restoreLastLine() {
        project.getEnabledPage().redo();
        invalidate(); // Перерисовываем экран
    }

    public Bitmap getBitmap() {
        // 1. Создаем Bitmap того же размера, что и View
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        // 2. Создаем Canvas, связанный с этим Bitmap
        Canvas canvas = new Canvas(bitmap);

        canvas.concat(inverseMatrix);

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