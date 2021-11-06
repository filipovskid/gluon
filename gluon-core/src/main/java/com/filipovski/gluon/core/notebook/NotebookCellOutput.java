package com.filipovski.gluon.core.notebook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotebookCellOutput {

    private List<NotebookCellData> output;

    public NotebookCellOutput() {
        this.output = new ArrayList<>();
    }

    private NotebookCellOutput(List<NotebookCellData> data) {
        this.output = data;
    }

    public static NotebookCellOutput from(List<String> output) {
        List<NotebookCellData> data = output.stream()
                .map(NotebookCellData::from)
                .collect(Collectors.toList());

        return new NotebookCellOutput(data);
    }

    public List<NotebookCellData> getOutput() {
        return List.copyOf(output);
    }

    public void appendOutput(String output) {
        this.output.add(NotebookCellData.from(output));
    }

    public void appendOutput(List<String> output) {
        this.output.addAll(output.stream()
                .map(NotebookCellData::from)
                .collect(Collectors.toList()));
    }
}
