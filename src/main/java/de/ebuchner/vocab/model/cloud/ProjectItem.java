package de.ebuchner.vocab.model.cloud;

public class ProjectItem {
    private String projectName;
    private String projectLocale;
    private long numberOfFiles;

    public long getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(long numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public String getProjectLocale() {
        return projectLocale;
    }

    public void setProjectLocale(String projectLocale) {
        this.projectLocale = projectLocale;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
