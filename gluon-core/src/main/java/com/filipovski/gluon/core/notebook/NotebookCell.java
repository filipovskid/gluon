package com.filipovski.gluon.core.notebook;

import com.filipovski.common.domain.AbstractEntity;
import com.filipovski.gluon.core.notebook.events.CellExecutionStartedEvent;
import com.filipovski.gluon.core.notebook.utils.NotebookCellOutputConverter;
import lombok.Getter;

import javax.persistence.*;
import java.util.Collections;
import java.util.Objects;

// TODO: Track cell execution lifecycle.
// TODO: Should have a service that knows the isolation level of a cell execution.
//       This service should be able to create a sessionId which identifies the
//       environment within which the code will be executed.
// TODO: State transition event for frontend.

@Getter
@Entity(name = "notebook_cells")
public class NotebookCell extends AbstractEntity<NotebookCellId> {

    @Version
    private Long version;

    private String language;

    @Column(columnDefinition = "TEXT")
    private String code;

    private Double position;

    @Enumerated(EnumType.STRING)
    private NotebookCellStatus status;

    @Column(columnDefinition = "TEXT")
    @Convert(converter= NotebookCellOutputConverter.class)
    private NotebookCellOutput output;

    @ManyToOne(fetch = FetchType.LAZY)
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
        this.status = NotebookCellStatus.CREATED;
        this.output = NotebookCellOutput.from(Collections.emptyList());
    }

    public void run(String sessionId) {
        if (!notebook.isStarted())
            return;
        
        transitionState(NotebookCellStatus.PENDING);
        this.output = NotebookCellOutput.from(Collections.emptyList());
        registerEvent(CellExecutionStartedEvent.from(this, sessionId));
    }

    public boolean transitionState(NotebookCellStatus status) {
        // TODO: Check whether all transitions are possible. Current setup does not prevent any transitions.
        this.status = status;

        return true;
    }

    public void appendOutput(String data) {
        this.output.appendOutput(data);
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
