package com.killerficha.mangaart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class EditorDraw extends View {

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private Path path;
    private List<DrawableObject> objects; // Список для объектов





    public EditorDraw(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10f); // Толщина кисти
        paint.setStyle(Paint.Style.STROKE);
        path = new Path();
        objects = new ArrayList<>();

        // Инициализация bitmap и canvas
        bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Рисуем все объекты на canvas
        for (DrawableObject object : objects) {
            object.draw(canvas, paint);
        }

        // Рисуем bitmap на самом view
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                // Добавляем путь в список объектов после завершения рисования
                objects.add(new LineObject(new Path(path), paint.getColor(), paint.getStrokeWidth()));
                path.reset(); // Сбросить путь для нового рисования
                break;
        }
        invalidate(); // Перерисовать экран
        return true;
    }

    // Метод для очистки холста
    public void clearCanvas() {
        objects.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    // Метод для удаления последнего нарисованного объекта
    public void removeLastLine() {
        if (!objects.isEmpty()) {
            objects.remove(objects.size() - 1);
            invalidate();
        }
    }
}
