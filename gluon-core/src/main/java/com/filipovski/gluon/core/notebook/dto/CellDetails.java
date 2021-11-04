package com.filipovski.gluon.core.notebook.dto;

import com.filipovski.gluon.core.notebook.NotebookCell;
import com.filipovski.gluon.core.notebook.NotebookCellData;
import com.filipovski.gluon.core.notebook.NotebookCellStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CellDetails {

    private String id;

    private String language;

    private String code;

    private Double position;

    private NotebookCellStatus status;

    private List<String> output;

    public CellDetails(String id,
                       String language,
                       String code,
                       Double position,
                       NotebookCellStatus status,
                       List<String> output) {
        this.id = id;
        this.language = language;
        this.code = code;
        this.position = position;
        this.status = status;
        this.output = output;
    }

    public static CellDetails from(NotebookCell cell) {
        List<String> output = cell.getOutput().getOutput().stream()
                .map(NotebookCellData::getData)
                .collect(Collectors.toList());

        return new CellDetails(
                cell.id().getId(),
                cell.getLanguage(),
                cell.getCode(),
                cell.getPosition(),
                cell.getStatus(),
                output
        );
    }
}
