package com.filipovski.gluoncore.notebook;

import com.filipovski.common.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.util.Objects;

@Entity(name = "notebook_cells")
public class NotebookCell extends AbstractEntity<NotebookCellId> {

    @Version
    private Long version;

    private String code;

    private Double position;

    private String language;

    @ManyToOne
    private Notebook notebook;

    protected NotebookCell() { }

    private NotebookCell(
            Notebook notebook,
            String code,
            Double position) {
        super(NotebookCellId.randomId(NotebookCellId.class));

        this.notebook = notebook;
        this.code = code;
        this.position = position;
    }

    public static NotebookCell from(Notebook notebook, String code, Double position) {
        Objects.requireNonNull(notebook, "notebook must not be null");
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(position, "position must not be null");

        return new NotebookCell(notebook, code, position);
    }
}
