package ru.drsk.httptest2.pojo;

/**
 * Created by sergei on 11.02.2016.
 */
public class TextAddFile {
    private String loadFile;
    private String statusLoadFile;
    private String fileName = "";

    public TextAddFile(String loadFile, String statusLoadFile) {
        this.loadFile = loadFile;
        this.statusLoadFile = statusLoadFile;
    }

    public String getLoadFile() {
        return loadFile;
    }

    public void setLoadFile(String loadFile) {
        this.loadFile = loadFile;
    }

    public String getStatusLoadFile() {
        return statusLoadFile;
    }

    public void setStatusLoadFile(String statusLoadFile) {
        this.statusLoadFile = statusLoadFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
