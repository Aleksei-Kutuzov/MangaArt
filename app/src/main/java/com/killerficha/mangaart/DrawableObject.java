package com.killerficha.mangaart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public interface DrawableObject {
    void draw(Canvas canvas, Paint paint);
}

class LineObject implements DrawableObject {

    private Path path;
    private int color;
    private float strokeWidth;

    public LineObject(Path path, int color, float strokeWidth) {
        this.path = path;
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawPath(path, paint);
    }
}
