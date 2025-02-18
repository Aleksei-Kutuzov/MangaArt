package com.killerficha.mangaart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DrawableObject {
    Path path;


    public void setPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public void draw(Canvas canvas, Paint paint) {
    }

    public void lineTo(float x, float y) {
    }
}
