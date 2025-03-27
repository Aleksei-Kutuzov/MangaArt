package com.killerficha.mangaart.ProjectInstruments;

import android.content.Context;

import com.killerficha.mangaart.PDB.ProjectsDataBaseOpener;

import java.io.IOException;

public class ProjectLoader {
    Long projectID;
    ProjectsDataBaseOpener projectsDataBaseOpener;
    public ProjectLoader(Long projectID, Context context){
        this.projectID = projectID;
        projectsDataBaseOpener = ProjectsDataBaseOpener.getInstance(context.getApplicationContext());
    }

    public Long getProjectID() {
        return projectID;
    }

    public Project loadProject() throws IOException, ClassNotFoundException {
        return ProjectSerializerOrDeserializer.deserializeProject(projectsDataBaseOpener.select_project(projectID));
    }
}