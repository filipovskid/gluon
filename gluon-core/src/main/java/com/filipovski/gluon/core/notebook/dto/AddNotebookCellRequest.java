package com.filipovski.gluon.core.notebook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AddNotebookCellRequest {

    @JsonProperty("input")
    private String cellInput;

    private String executor;

    private Double position;
}
