package com.filipovski.gluon.core.notebook;

public class NotebookCellData {

    private String data;

    private NotebookCellData(String data) {
        this.data = data;
    }

    public static NotebookCellData from(String data) {
        return new NotebookCellData(data);
    }

    public String getData() {
        return data;
    }
}
