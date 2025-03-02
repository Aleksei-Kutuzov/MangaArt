package com.killerficha.mangaart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DrawableObject {
    Path path;
    Paint paint;

    public void setPaint(Paint paint) {
        this.paint = paint;
        this.paint.setAntiAlias(false);
    }

    public Paint getPaint() {
        return paint;
    }


    public void draw(Canvas canvas) {
    }

    public void lineTo(float x, float y) {
    }
}