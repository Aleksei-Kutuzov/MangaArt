package com.killerficha.mangaart.ProjectInstruments;

import android.content.Context;

import com.killerficha.mangaart.PDB.ProjectsDataBaseOpener;

import java.io.IOException;

public class ProjectSaver {
    private final Project project;
    private final ProjectsDataBaseOpener dbHelper;

    public ProjectSaver(Project project, Context context) {
        this.project = project;
        // Используем синглтон вместо создания нового экземпляра
        this.dbHelper = ProjectsDataBaseOpener.getInstance(context.getApplicationContext());
    }

    public Project getProject() {
        return project;
    }

    public void saveProject() throws IOException {
        byte[] serializedProject = ProjectSerializerOrDeserializer.serializeProject(project);
        long insertedId = dbHelper.insert(
                serializedProject,
                project.projectName,
                project.projectCreateDate
        );

        if (insertedId == -1) {
            throw new IOException("Failed to save project to database");
        }
    }
}