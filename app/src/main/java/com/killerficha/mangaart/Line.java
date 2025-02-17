package com.killerficha.mangaart;

import android.graphics.Canvas;
import android.graphics.Paint;

// Вспомогательный класс для представления линии
public class Line {
    float startX, startY, endX, endY;

    void moveTo(float x, float y) {
        endX = startX - endX + x;
        endY = startY - endY + y;
        startX = x;
        startY = y;
    }

    Line(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    void draw(Canvas canvas, Paint paint){
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

}
