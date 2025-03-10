package com.killerficha.mangaart;

import static android.app.PendingIntent.getActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Editor extends AppCompatActivity {

    private static final int REQUEST_CODE_SAVE_FILE = 5252; // это индификатор запроса он не должен совподать с другим инд.зап. приложения
    private EditorDraw editorDraw; // Объект для рисования

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        context = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        ImageView fillButton = findViewById(R.id.fill);
        ImageView markerButton = findViewById(R.id.marker);
        ImageButton cropRotate = findViewById(R.id.crop_rotate);
        ImageButton nextPageButton = findViewById(R.id.next_page);
        ImageButton prewPageButton = findViewById(R.id.prew_page);
        ImageButton addPageButton = findViewById(R.id.add_page);

        ImageButton eraserButton = findViewById(R.id.eraser);
        ImageView chooseColorButton = findViewById(R.id.chooseColorButton);
        SeekBar chooseThicknessBar = findViewById(R.id.chooseThicknessBar);

        // Настроим действия кнопок
        clearButton.setOnClickListener(v -> {editorDraw.clear(); editorDraw.setEditMode(false);});
        //Предлага.ю эту кнопку переделать для уджаление страницы с подтверждением через диалоговое окно
        removeButton.setOnClickListener(v -> editorDraw.removeLastLine());
        restoreButton.setOnClickListener(v -> editorDraw.restoreLastLine());
        eraserButton.setOnClickListener(v -> {editorDraw.instrument.setMode(Instrument.mode_list.ERASER); editorDraw.setEditMode(false);});
        pencilButton.setOnClickListener(v -> {editorDraw.instrument.setMode(Instrument.mode_list.PENCIL); editorDraw.setEditMode(false);});
        fillButton.setOnClickListener(v -> {editorDraw.instrument.setMode(Instrument.mode_list.FILL); editorDraw.setEditMode(false);});
        markerButton.setOnClickListener(v -> {editorDraw.instrument.setMode(Instrument.mode_list.MARKER); editorDraw.setEditMode(false);});
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

        cropRotate.setOnClickListener(v -> editorDraw.switchEditMode());

        nextPageButton.setOnClickListener(v -> nextPage());
        prewPageButton.setOnClickListener(v -> lastPage());
        addPageButton.setOnClickListener(v -> addPage());

    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] templates = {"2x2", "3x2", "4x2"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(context, this)); //решить вопрос  с активити
        builder.setTitle("Выберите шаблон")
                .setItems(templates, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Templates x;
                        x = new Templates(context);
                        x.parseToTempl(templates[which]);

                    }
                })
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //отмена
                    }
                });

//связать с
        return builder.create();
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

    private void nextPage() {
        editorDraw.project.nextPage();
        editorDraw.invalidate();
    }

    private void lastPage() {
        editorDraw.project.lastPage();
        editorDraw.invalidate();
    }

    private void addPage() {
        editorDraw.project.pageAdd();
        editorDraw.invalidate();
    }
}
