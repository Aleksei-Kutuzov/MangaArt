package com.killerficha.mangaart;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import org.opencv.android.OpenCVLoader;

import yuku.ambilwarna.AmbilWarnaDialog;



public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE_DIRECTORY = 1;
    ImageButton createButton;

    ImageButton editorButton;
    private Uri selectedDirectoryUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        if (!OpenCVLoader.initDebug()) {
//            Log.e("OpenCV", "Initialization failed");
//        } else {
//            Log.d("OpenCV", "Initialization succeeded");
//        }

        //int defaultColor = Color.BLACK;
        createButton = findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_DIRECTORY);
            }
        });


        editorButton = findViewById(R.id.getToEditir);
        editorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Editor.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE_DIRECTORY && resultCode == RESULT_OK) {
            if (data != null) {
                // Сохраняем URI выбранной директории
                selectedDirectoryUri = data.getData();

                // Показываем диалог для ввода названия папки
                startActivity(new Intent(MainActivity.this, Editor.class));
            }
        }
    }
}