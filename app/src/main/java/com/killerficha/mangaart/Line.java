package com.killerficha.mangaart;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Line {
    private float startX, startY, endX, endY;
    private Paint paint;
    private Path path;

    // Конструктор линии
    public Line(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        // Инициализация кисти (цвет, толщина)
        paint = new Paint();
        paint.setColor(Color.BLACK);  // Цвет линии
        paint.setStrokeWidth(5);      // Толщина линии
        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
    }

    // Метод для рисования линии на холсте
    public void draw(Paint paint, android.graphics.Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    // Метод для обновления конечной точки линии (например, для редактирования)
    public void updateEndPoint(float endX, float endY) {
        this.endX = endX;
        this.endY = endY;
        path.reset();  // Обновим путь
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
    }
}
