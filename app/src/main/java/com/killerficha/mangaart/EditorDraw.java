package com.killerficha.mangaart;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class EditorDraw extends View {
    Paint p;
    Canvas canvas;
    Path path;
    public EditorDraw (Context context) {
        super(context);
        p = new Paint();
        path = new Path();
        canvas = new Canvas();

    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        // invalidate();
        this.canvas = canvas;
        canvas.drawARGB(255, 255, 255, 255);
        //canvas.drawCircle(100, 100, 100, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(12);
        p.setColor(Color.BLACK);
        canvas.drawPath(path, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                path.moveTo(touchX, touchY);
                canvas.drawPath(path, p);
                //path.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
}