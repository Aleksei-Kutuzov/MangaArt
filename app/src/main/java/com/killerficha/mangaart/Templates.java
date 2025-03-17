package com.killerficha.mangaart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Templates extends View {

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

    enum templ {TWO_X_TWO, THREE_X_TWO, FOUR_X_TWO, CUSTOM}

    templ modeT = templ.TWO_X_TWO;
    private int rows = 2; // Количество строк
    private int cols = 2; // Количество столбцов
    int cellHeight;
    int cellWidth;
    private Paint paint;

    int getRows() {
        return rows;
    }

    void setRows(int r) {
        rows = r;
    }

    int getCols() {
        return cols;
    }

    void setCols(int c) {
        cols = c;
    }


    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2); // Толщина линий
    }

    void setTMode(templ modet) {
        this.modeT = modet;
    }


    void exec(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        switch (modeT) {
            case TWO_X_TWO:
                // Рассчитываем ширину и высоту ячейки
                setRows(2);
                setCols(2);
                cellWidth = width / getCols();
                cellHeight = height / getRows();

                break;
            case THREE_X_TWO:
                // Рассчитываем ширину и высоту ячейки
                setRows(3);
                setCols(2);
                cellWidth = width / getCols();
                cellHeight = height / getRows();
                break;
            case FOUR_X_TWO:
                // Рассчитываем ширину и высоту ячейки
                setRows(4);
                setCols(2);
                cellWidth = width / getCols();
                cellHeight = height / getRows();
                break;
            case CUSTOM:
//                setCols();

        }
        for (int i = 1; i < cols; i++) {
            int x = i * cellWidth;
//            canvas.drawLine(0, 0, x, height, paint);
            DrawableObject newDrawable = new FreeLine(x, 0);
            newDrawable.setPaint(new Paint(paint));
            freeLines.add(newDrawable);

        }

        // Рисуем горизонтальные линии
        for (int i = 1; i < rows; i++) {
            int y = i * cellHeight;
            canvas.drawLine(0, y, width, y, paint);
        }
    }

}
