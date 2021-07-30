package com.filipovski.gluon.core.notebook.dto;

import com.filipovski.gluon.core.notebook.Notebook;
import com.filipovski.gluon.core.notebook.NotebookCell;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotebookData {

    private String id;

    private String name;

    private String language;

    private List<CellDetails> cells;

    public NotebookData(String id, String name, String language, List<CellDetails> cells) {
        this.id = id;
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
                notebook.getName(),
                notebook.getLanguage(),
                cells
        );
    }

}
