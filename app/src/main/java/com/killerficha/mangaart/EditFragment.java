package com.killerficha.mangaart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class EditFragment extends Fragment {

    private EditorDraw editorDraw;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance() {
        return new EditFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Инициализируем custom View для рисования
        editorDraw = new EditorDraw(getContext());
        return editorDraw;
    }

    // Пример вызова методов для очистки или удаления линии
    public void clearCanvas() {
        editorDraw.clear();
    }

    public void removeLastLine() {
        editorDraw.removeLastLine();
    }
}
