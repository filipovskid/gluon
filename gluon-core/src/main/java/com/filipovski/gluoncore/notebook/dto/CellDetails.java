package com.filipovski.gluoncore.notebook.dto;

import com.filipovski.gluoncore.notebook.NotebookCell;
import lombok.Getter;

@Getter
public class CellDetails {

    private String id;

    private String language;

    private String code;

    private Double position;

    public CellDetails(String id, String language, String code, Double position) {
        this.id = id;
        this.language = language;
        this.code = code;
        this.position = position;
    }

    public static CellDetails from(NotebookCell cell) {
        return new CellDetails(
                cell.id().getId(),
                cell.getLanguage(),
                cell.getCode(),
                cell.getPosition()
        );
    }
}
