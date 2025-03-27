package com.killerficha.mangaart;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import org.opencv.android.OpenCVLoader;

import com.killerficha.mangaart.PDB.ProjectInfo;
import com.killerficha.mangaart.PDB.ProjectsDataBaseOpener;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE_DIRECTORY = 1;
    private ImageButton createButton;
    private ImageButton editorButton;
    private Uri selectedDirectoryUri;
    private ProjectsDataBaseOpener dbHelper;
    private SimpleCursorAdapter adapter;
    private ListView listView;

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

        // Получаем экземпляр БД через синглтон
        dbHelper = ProjectsDataBaseOpener.getInstance(this);

        // Настройка ListView
        listView = findViewById(R.id.projectsListview);
        setupListView();

        createButton = findViewById(R.id.create);
        createButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_DIRECTORY);
        });

        editorButton = findViewById(R.id.getToEditir);
        editorButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Editor.class));
        });
    }

    private void setupListView() {
        // Получаем курсор с данными
        Cursor cursor = dbHelper.getAllProjectsWithFormattedDate();

        // Какие столбцы из курсора будем использовать
        String[] fromColumns = {"NAME", "formatted_date"};

        // В какие View будем выводить данные
        int[] toViews = {R.id.project_name, R.id.project_date};

        // Создаем адаптер
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.project_list_item, // макет строки
                cursor,
                fromColumns,
                toViews,
                0);

        // Назначаем адаптер ListView
        listView.setAdapter(adapter);

        // Обработчик кликов
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ProjectInfo project = dbHelper.select_project_info(id);
            if (project != null) {
                Toast.makeText(this, "Выбран: " + project.getName(), Toast.LENGTH_SHORT).show();
                // Можно открыть редактор с выбранным проектом
                Intent intent = new Intent(MainActivity.this, Editor.class);
                intent.putExtra("project_id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE_DIRECTORY && resultCode == RESULT_OK) {
            if (data != null) {
                selectedDirectoryUri = data.getData();
                startActivity(new Intent(MainActivity.this, Editor.class));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем список при возвращении на экран
        refreshProjectList();
    }

    private void refreshProjectList() {
        Cursor newCursor = dbHelper.getAllProjectsWithFormattedDate();
        adapter.changeCursor(newCursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрываем курсор
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
        // Не закрываем dbHelper - он синглтон и будет жить всё время работы приложения
    }
}