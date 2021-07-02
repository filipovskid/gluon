package com.filipovski.gluoncore.notebook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotebookCellRepository extends JpaRepository<NotebookCell, NotebookCellId> {

    List<NotebookCell> findAllByNotebookId(NotebookId notebookId);
}
