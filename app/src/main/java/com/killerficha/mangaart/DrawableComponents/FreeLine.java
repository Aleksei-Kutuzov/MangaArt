package com.killerficha.mangaart.DrawableComponents;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

// класс для представления свободной линии
public class FreeLine extends DrawableObject {

    SerialisePath path;
    Paint paint;

    public void moveTo(float x, float y) {
        path.moveTo(x, y);
    }

    public FreeLine(float startX, float startY) {
        paint = new Paint();
        path = new SerialisePath();
        path.reset();
        path.moveTo(startX, startY);
    }

    @Override
    public void setPaint(SerialisePaint paint) {
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