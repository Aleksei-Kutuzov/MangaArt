package com.killerficha.mangaart;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;

public class EditorDraw extends View {
    public EditorDraw (Context context) {
        super(context);
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Paint p = new Paint();
        canvas.drawCircle(100, 100, 100, p);
        super.onDraw(canvas);
    }
}