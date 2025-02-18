package com.killerficha.mangaart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Editor extends AppCompatActivity {

    private EditorDraw editorDraw; // Объект для рисования

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Получаем ссылку на контейнер FrameLayout
        FrameLayout drawingContainer = findViewById(R.id.view);

        // Инициализируем EditorDraw и добавляем его в контейнер
        editorDraw = new EditorDraw(this);
        drawingContainer.addView(editorDraw);

        // Настроим кнопки для управления холстом
        ImageView clearButton = findViewById(R.id.delete);
        ImageView removeButton = findViewById(R.id.back);
        ImageView restoreButton = findViewById(R.id.next);
       // ImageView pencilButton = findViewById(R.id.pencil);


        // Настроим действия кнопок
        clearButton.setOnClickListener(v -> editorDraw.clear());
        removeButton.setOnClickListener(v -> editorDraw.removeLastLine());
        restoreButton.setOnClickListener(v -> editorDraw.restoreLastLine());
        //pencilButton.setOnClickListener(v -> Instrument.setMode(Instrument.mode_list.PENCIL));


    }
}
