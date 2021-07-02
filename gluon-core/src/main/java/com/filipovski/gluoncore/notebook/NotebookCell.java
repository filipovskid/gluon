package com.filipovski.gluoncore.notebook;

import com.filipovski.common.domain.AbstractEntity;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.util.Objects;

@Getter
@Entity(name = "notebook_cells")
public class NotebookCell extends AbstractEntity<NotebookCellId> {

    @Version
    private Long version;

    private String language;

    private String code;

    private Double position;

    @ManyToOne
    private Notebook notebook;

    protected NotebookCell() { }

    private NotebookCell(
            Notebook notebook,
            String language,
            String code,
            Double position) {
        super(NotebookCellId.randomId(NotebookCellId.class));

        this.notebook = notebook;
        this.language = language;
        this.code = code;
        this.position = position;
    }

    public String getId() {
        return id().getId();
    }

    public static NotebookCell from(Notebook notebook, String language, String code, Double position) {
        Objects.requireNonNull(notebook, "notebook must not be null");
        Objects.requireNonNull(language, "language must not be null");
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(position, "position must not be null");

        return new NotebookCell(notebook, language, code, position);
    }

    @Override
    public String toString() {
        return "NotebookCell{" +
                "id = '" + id().getId() + '\'' +
                ", version=" + version +
                ", language='" + language + '\'' +
                ", code='" + code + '\'' +
                ", position=" + position +
                ", notebook=" + notebook +
                '}';
    }
}
