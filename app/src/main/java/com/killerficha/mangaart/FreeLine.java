package com.killerficha.mangaart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

// класс для представления свободной линии
public class FreeLine extends DrawableObject {

    Path path;
    Paint paint;

    public void moveTo(float x, float y) {
        path.moveTo(x, y);
    }

    FreeLine(float startX, float startY) {
        paint = new Paint();
        path = new Path();
        path.reset();
        path.moveTo(startX, startY);
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
        this.paint.setAntiAlias(false);
    }

    public void draw(Canvas canvas){
        canvas.drawPath(path, paint);
    }

    public void lineTo(float x, float y) {
        path.lineTo(x, y);
    }

}