package com.killerficha.mangaart.ProjectInstruments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project implements Serializable {
    // Добавляем serialVersionUID для контроля версий
    private static final long serialVersionUID = 1L;

    private List<PageHistory> pages;
    private int enabledPageIndex = 0;
    public String projectName;
    public Date projectCreateDate;

    public Project(String projectName) {
        this(projectName, new Date());
    }

    public Project(String projectName, Date projectCreateDate) {
        this.projectName = projectName;
        this.projectCreateDate = projectCreateDate;
        this.pages = new ArrayList<>();
        this.enabledPageIndex = 0;
    }

    // Все остальные методы остаются без изменений
    public void pageAdd(PageHistory pageHistory) {
        pages.add(pageHistory);
        enabledPageIndex = pages.size() - 1;
    }

    public void pageAdd() {
        PageHistory new_page_history = new PageHistory();
        pages.add(new_page_history);
        enabledPageIndex = pages.size() - 1;
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

    public void nextPage() {
        enabledPageIndex++;
        if (pages.size() - 1 < enabledPageIndex) {
            enabledPageIndex--;
        }
    }

    public void lastPage() {
        enabledPageIndex--;
        if (enabledPageIndex < 0) {
            enabledPageIndex = 0;
        }
    }
}