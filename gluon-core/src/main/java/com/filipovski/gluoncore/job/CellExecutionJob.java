package com.filipovski.gluoncore.job;

import com.filipovski.gluoncore.notebook.NotebookCellId;
import com.filipovski.gluoncore.notebook.NotebookId;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class CellExecutionJob extends Job {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "id", column = @Column(name = "notebook_id"))
    })
    private NotebookId notebookId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "id", column = @Column(name = "notebook_cell_id"))
    })
    private NotebookCellId cellId;

    public CellExecutionJob(String sessionId,
                            NotebookId notebookId,
                            NotebookCellId cellId) {
        super(sessionId);

        this.notebookId = notebookId;
        this.cellId = cellId;
    }

    @Override
    public void completed() {
        // TODO: Dispatch an event specific to the job type that carries its result.
    }
}
