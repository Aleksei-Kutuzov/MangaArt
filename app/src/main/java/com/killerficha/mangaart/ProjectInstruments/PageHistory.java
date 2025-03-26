package com.killerficha.mangaart.ProjectInstruments;

import com.killerficha.mangaart.DrawableComponents.DrawableObject;

import java.io.Serializable;
import java.util.Stack;

public class PageHistory {
    private Stack<DrawableObject> drawableObjects;
    private Stack<DrawableObject> deletedDrawableObjects;



    PageHistory(){
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
