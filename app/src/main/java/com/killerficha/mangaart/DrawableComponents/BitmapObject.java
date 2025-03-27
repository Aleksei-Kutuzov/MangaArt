package com.killerficha.mangaart.DrawableComponents;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

public class BitmapObject extends DrawableObject {
    Bitmap bitmap;
    SerialisePaint paint;
    public BitmapObject(Bitmap bitmap) {
        this.bitmap = bitmap;
        paint = new SerialisePaint();
        //paint.setAntiAlias(true);
        //paint.setFilterBitmap(true);
        //paint.setDither(true);
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }
}
