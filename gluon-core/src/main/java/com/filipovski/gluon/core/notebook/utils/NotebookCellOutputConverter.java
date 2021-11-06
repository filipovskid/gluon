package com.filipovski.gluon.core.notebook.utils;

import com.filipovski.gluon.core.notebook.NotebookCellOutput;
import com.google.gson.Gson;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class NotebookCellOutputConverter implements AttributeConverter<NotebookCellOutput, String> {

    private static final Gson gson = new Gson();

    @Override
    public String convertToDatabaseColumn(NotebookCellOutput attribute) {
        return gson.toJson(attribute);
    }

    @Override
    public NotebookCellOutput convertToEntityAttribute(String dbData) {
        return gson.fromJson(dbData, NotebookCellOutput.class);
    }
}
