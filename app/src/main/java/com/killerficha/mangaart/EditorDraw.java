package com.killerficha.mangaart;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class EditorDraw extends View {
    Paint p = new Paint();
    Canvas canvas;
    Path path = new Path();
    public EditorDraw (Context context) {
        super(context);
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawARGB(255, 255, 255, 255);
        p.setStrokeWidth(12);
        //canvas.drawCircle(100, 100, 100, p);
        canvas.drawPath(path, p);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
             path.addPath(path);
                path.moveTo(getX(), getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(getX(), getY());
                break;
            case MotionEvent.ACTION_UP:
                path.reset();
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}