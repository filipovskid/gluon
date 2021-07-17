package com.filipovski.gluoncore.notebook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotebookCellRepository extends JpaRepository<NotebookCell, NotebookCellId> {

    List<NotebookCell> findAllByNotebookId(NotebookId notebookId);

    @Query("select c from notebook_cells c where c.notebook.id = :notebookId and c.id = :cellId")
    Optional<NotebookCell> findNotebookCell(@Param("notebookId") NotebookId notebookId,
                                            @Param("cellId") NotebookCellId cellId);
}
