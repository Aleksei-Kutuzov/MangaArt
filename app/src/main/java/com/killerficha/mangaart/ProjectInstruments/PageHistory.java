package com.killerficha.mangaart.ProjectInstruments;

import com.killerficha.mangaart.DrawableComponents.DrawableObject;

import java.io.Serializable;
import java.util.Stack;

public class PageHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Stack<DrawableObject> drawableObjects;
    private Stack<DrawableObject> deletedDrawableObjects;



    public PageHistory(){
        drawableObjects = new Stack<>();
        deletedDrawableObjects = new Stack<>();

    }

    public Stack<DrawableObject> getDeletedDrawableObjects() {
        return deletedDrawableObjects;
    }

    public Stack<DrawableObject> getDrawableObjects() {
        return drawableObjects;
    }

    public void undo(){
        if (!drawableObjects.isEmpty()) {
            deletedDrawableObjects.push(drawableObjects.pop());
        }
    }

    public void redo(){
        if (!deletedDrawableObjects.isEmpty()) {
            drawableObjects.push(deletedDrawableObjects.pop());
        }
    }
}
