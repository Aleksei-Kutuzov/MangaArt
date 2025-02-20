
package com.killerficha.mangaart;

import android.graphics.Canvas;
import android.graphics.Paint;

// Вспомогательный класс для представления линии
public class StraightLine extends DrawableObject {
    float startX, startY, endX, endY;

    void moveTo(float x, float y) {
        endX = startX - endX + x;
        endY = startY - endY + y;
        startX = x;
        startY = y;
    }


    StraightLine(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public void draw(Canvas canvas){
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    public void lineTo(float x, float y) {
        this.endX = x;
        this.endY = y;
    }

}