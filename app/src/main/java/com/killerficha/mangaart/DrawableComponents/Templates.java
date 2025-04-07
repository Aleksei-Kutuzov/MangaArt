package com.killerficha.mangaart.DrawableComponents;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Templates extends View {
    public int cols;
    public int rows;
    public enum templ {TWOxTWO, TWOxTHREE, TWOxFOUR}
    static templ modeT = templ.TWOxTWO;
    private Paint paint;

    public Templates(Context context) {
        super(context);
        init();
    }

    public Templates(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Templates(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2); // Толщина линий
    }

    void setModeT(templ mode){
        this.modeT = mode;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        switch (modeT) {
            // Рассчитываем ширину и высоту ячейки
            case TWOxTWO:
                cols = 2;
                rows = 2;
                break;
            case TWOxTHREE:
                cols = 3;
                rows = 2;
                break;
            case TWOxFOUR:
                cols = 4;
                rows = 2;
                break;
        }
        int cellWidth = width / cols;
        int cellHeight = height / rows;
        // Рисуем вертикальные линии
        for (int i = 1; i < cols; i++) {
            int x = i * cellWidth;
            canvas.drawLine(x, 0, x, height, paint);
        }
        // Рисуем горизонтальные линии
        for (int i = 1; i < rows; i++) {
            int y = i * cellHeight;
            canvas.drawLine(0, y, width, y, paint);
        }
    }
}
