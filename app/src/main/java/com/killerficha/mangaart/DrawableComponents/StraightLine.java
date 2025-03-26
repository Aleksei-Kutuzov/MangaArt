
package com.killerficha.mangaart.DrawableComponents;

import android.graphics.Paint;
import android.util.Log;

// Вспомогательный класс для представления линии
public class StraightLine extends FreeLine {
    StraightLine(float startX, float startY) {
        super(startX, startY);
        this.path.lineTo(startX, startY);
    }


    public void lineTo(float x, float y) {
        this.path.setLastPoint(x, y);
        Log.d("&", "4");
    }

    @Override
    public void setPaint(Paint paint) {
        super.setPaint(paint);
        paint.setAlpha(128);
        paint.setAntiAlias(false);
        paint.setStrokeCap(Paint.Cap.BUTT);
    }
}