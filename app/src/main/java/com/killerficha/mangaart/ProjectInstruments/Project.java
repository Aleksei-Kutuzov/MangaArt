package com.killerficha.mangaart.ProjectInstruments;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project implements Serializable {
    private List<PageHistory> pages;
    private int enabledPageIndex;

    public String projectName;
    public Date projectCreateDate;

    public Project(){
        new Project("untitled_project");
    }

    public Project(String projectName){
        new Project(projectName, new Date());
    }

    public Project(String projectName, Date projectCreateDate){
        this.projectName = projectName;
        this.projectCreateDate = projectCreateDate;
        pages = new ArrayList<>();
        enabledPageIndex = 0;
    }
    public void pageAdd(PageHistory pageHistory){
        pages.add(pageHistory);
        enabledPageIndex = pages.size() - 1;
    }

    public void pageAdd(){
        pageAdd(new PageHistory());
    }

    public void pageDelete() {
        if (pages.size() != 1) {
            enabledPageIndex = pages.size() - 2;
            pages.remove(pages.size() - 1);
        }
    }

    public int getEnabledPageIndex() {
        return enabledPageIndex;
    }

    public List<PageHistory> getPages() {
        return pages;
    }

    public void setEnabledPageIndex(int enabledPageIndex) {
        this.enabledPageIndex = enabledPageIndex;
    }

    public PageHistory getEnabledPage() {
        return pages.get(getEnabledPageIndex());
    }

    public void nextPage(){
        enabledPageIndex++;
        if (pages.size() - 1 < enabledPageIndex){
            enabledPageIndex--;
        }
    }

    public void lastPage(){
        enabledPageIndex--;
        if (enabledPageIndex < 0){
            enabledPageIndex = 0;
        }
    }


}
