package com.killerficha.mangaart;

import static android.opengl.ETC1.getHeight;
import static android.opengl.ETC1.getWidth;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable {
    private List<PageHistory> pages;
    private List<DrawableObject> freeLines;

    private int enabledPageIndex;
    Canvas canvas;

    enum templ {EMPTY_T, TWO_X_TWO, THREE_X_TWO, FOUR_X_TWO, CUSTOM}

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

    Project(){
        pages = new ArrayList<>();
        enabledPageIndex = 0;
    }


    public void pageAdd(PageHistory pageHistory){
        pages.add(pageHistory);
        enabledPageIndex = pages.size() - 1;
    }

    public void pageAdd(){pageAdd(new PageHistory());}

    public void pageAdd(){
//pageAdd();
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        switch (modeT) {
            case EMPTY_T:
                break;
            case TWO_X_TWO:
                // Рассчитываем ширину и высоту ячейки
                setRows(2);
                setCols(2);
                cellWidth = width / getCols();
                cellHeight = height / getRows();

                break;
            case THREE_X_TWO:
                // Рассчитываем ширину и высоту ячейк
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
            //case CUSTOM:
//                setCols();
        }
        pageAdd();
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
//            canvas.drawLine(0, y, width, y, paint);
            DrawableObject newDrawable = new FreeLine(0, y);
            newDrawable.setPaint(new Paint(paint));
            freeLines.add(newDrawable);
        }
    }

    public int getEnabledPageIndex() {
        return enabledPageIndex;
    }

    public List<PageHistory> getPages() {
        return pages;
    }

    public void setEnabledPageIndex(int enabledPageIndex) {
        this.enabledPageIndex = enabledPageIndex;
    }

    public PageHistory getEnabledPage() {
        return pages.get(getEnabledPageIndex());
    }

    public void nextPage(){
        enabledPageIndex++;
        if (pages.size() - 1 < enabledPageIndex){
            enabledPageIndex--;
        }
    }

    public void lastPage(){
        enabledPageIndex--;
        if (enabledPageIndex < 0){
            enabledPageIndex = 0;
        }
    }


}
