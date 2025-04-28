package com._4dconcept.evaluation.controller;

public class DeveloperView {

    private String id; // used in listDevelopers
    private String name; // used in listDevelopers and createDeveloper
    private String projectId; // used for createDeveloper
    private String projectName; // used in listDevelopers

    public DeveloperView() {
    }

    public DeveloperView(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
