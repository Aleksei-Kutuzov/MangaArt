package com.killerficha.mangaart.DrawableComponents;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.io.Serializable;

public class DrawableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    SerialisePath path;
    SerialisePaint paint;

    public void setPaint(SerialisePaint paint) {
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