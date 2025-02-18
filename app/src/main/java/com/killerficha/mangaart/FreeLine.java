package com.killerficha.mangaart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

// класс для представления свободной линии
public class FreeLine extends DrawableObject {

    public void moveTo(float x, float y) {
        path.moveTo(x, y);
    }

    FreeLine(float startX, float startY) {
        path = new Path();
        path.reset();
        path.moveTo(startX, startY);
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawPath(path, paint);
    }

    public void lineTo(float x, float y) {
        path.lineTo(x, y);
    }

}
