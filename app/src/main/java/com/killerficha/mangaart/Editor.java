package com.killerficha.mangaart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Editor extends AppCompatActivity {

    private EditorDraw editorDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        FrameLayout frameLayout = findViewById(R.id.view);
        EditorDraw editorDraw = new EditorDraw(this);
        frameLayout.addView(editorDraw);


        Button clearButton = findViewById(R.id.clearButton);
        Button removeButton = findViewById(R.id.removeButton);

        // Очистка холста
        clearButton.setOnClickListener(v -> editorDraw.clearCanvas());

        // Удаление последней линии
        removeButton.setOnClickListener(v -> editorDraw.removeLastLine());
    }
}
