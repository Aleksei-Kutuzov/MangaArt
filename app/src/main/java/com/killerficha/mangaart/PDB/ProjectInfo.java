package com.killerficha.mangaart.PDB;

import java.util.Date;

public class ProjectInfo {
    String name;
    Date date;

    ProjectInfo(String name, Date date){
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

}
