package com.killerficha.mangaart;

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

    void undo(){
        if (!drawableObjects.isEmpty()) {
            deletedDrawableObjects.push(drawableObjects.pop());
        }
    }

    void redo(){
        if (!deletedDrawableObjects.isEmpty()) {
            drawableObjects.push(deletedDrawableObjects.pop());
        }
    }
}
