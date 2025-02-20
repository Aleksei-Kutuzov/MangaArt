package com.killerficha.mangaart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

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
        Button chooseColorButton = findViewById(R.id.chooseColorButton);
        SeekBar chooseThicknessBar = findViewById(R.id.chooseThicknessBar);

        // Настроим действия кнопок
        clearButton.setOnClickListener(v -> editorDraw.clear());
        removeButton.setOnClickListener(v -> editorDraw.removeLastLine());
        restoreButton.setOnClickListener(v -> editorDraw.restoreLastLine());
        //pencilButton.setOnClickListener(v -> Instrument.setMode(Instrument.mode_list.PENCIL));

        chooseThicknessBar.setProgress(editorDraw.instrument.getThickness());
        chooseColorButton.setOnClickListener(v -> chooseColor());
        chooseThicknessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editorDraw.instrument.setThickness(seekBar.getProgress());
            }
        });
    }


    void chooseColor(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, editorDraw.instrument.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // Выбранный цвет
                editorDraw.instrument.setColor(color);
            }
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Действие при отмене
            }
        });
        colorPicker.show();
    }
}
