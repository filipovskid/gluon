package com.filipovski.gluon.core.notebook.dto;

import com.filipovski.gluon.core.notebook.Notebook;
import com.filipovski.gluon.core.notebook.NotebookCell;
import com.filipovski.gluon.core.notebook.NotebookStatus;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotebookData {

    private String id;

    private NotebookStatus status;

    private String name;

    private String language;

    private List<CellDetails> cells;

    public NotebookData(String id, NotebookStatus status, String name, String language, List<CellDetails> cells) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.language = language;
        this.cells = cells;
    }

    public static NotebookData from(Notebook notebook, List<NotebookCell> notebookCells) {
        List<CellDetails> cells = notebookCells.stream()
                .map(CellDetails::from)
                .collect(Collectors.toList());

        return new NotebookData(
                notebook.id().getId(),
                notebook.getStatus(),
                notebook.getName(),
                notebook.getLanguage(),
                cells
        );
    }

}
