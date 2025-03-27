package com.killerficha.mangaart.DrawableComponents;

import android.graphics.Paint;

import java.io.Serializable;

public class SerialisePaint extends Paint implements Serializable {
    private static final long serialVersionUID = 1L;
    public SerialisePaint(){
        super();
    }
    public SerialisePaint(Paint paint) {
        super(paint);
    }
}
