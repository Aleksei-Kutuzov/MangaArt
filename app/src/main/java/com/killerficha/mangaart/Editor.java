package com.killerficha.mangaart;

import static android.view.View.MeasureSpec.getMode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Editor extends AppCompatActivity {

    private static final int REQUEST_CODE_SAVE_FILE = 5252; // это индификатор запроса он не должен совподать с другим инд.зап. приложения
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
        ImageView pencilButton = findViewById(R.id.pencil);
        ImageButton eraserButton = findViewById(R.id.eraser);
        TextView modeX = findViewById(R.id.mode);
        ImageView chooseColorButton = findViewById(R.id.chooseColorButton);
        SeekBar chooseThicknessBar = findViewById(R.id.chooseThicknessBar);

        // Настроим действия кнопок
        clearButton.setOnClickListener(v -> editorDraw.clear());
        removeButton.setOnClickListener(v -> editorDraw.removeLastLine());
        restoreButton.setOnClickListener(v -> editorDraw.restoreLastLine());
        eraserButton.setOnClickListener(v -> editorDraw.instrument.setMode(Instrument.mode_list.ERASER));
        pencilButton.setOnClickListener(v -> editorDraw.instrument.setMode(Instrument.mode_list.PENCIL));
        modeX.setText("Mode" + Instrument.s);

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

        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(v -> saveProject());
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
                saveProject();
            }
        });
        colorPicker.show();
    }

    void saveProject(){

        // Создаем Intent для выбора директории
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/png"); // Указываем тип файла (например, PNG)
        intent.putExtra(Intent.EXTRA_TITLE, "my comics.png"); // Предлагаемое имя файла

        // Запускаем Intent
        startActivityForResult(intent, REQUEST_CODE_SAVE_FILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SAVE_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                // Теперь вы можете сохранить Bitmap в выбранную директорию
                saveBitmapToUri(editorDraw.getBitmap(), uri);
            }
        }
    }

    private void saveBitmapToUri(Bitmap bitmap, Uri uri) {
        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
