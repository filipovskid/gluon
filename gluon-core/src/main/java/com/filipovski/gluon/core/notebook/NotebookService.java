package com.filipovski.gluon.core.notebook;

import com.filipovski.gluon.core.notebook.dto.AddNotebookCellRequest;
import com.filipovski.gluon.core.notebook.dto.CellDetails;
import com.filipovski.gluon.core.notebook.dto.CreateNotebookRequest;
import com.filipovski.gluon.core.notebook.dto.NotebookData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotebookService {

    private final NotebookRepository notebookRepository;
    private final NotebookCellRepository notebookCellRepository;
    private final Logger logger = LoggerFactory.getLogger(NotebookService.class);

    public NotebookService(NotebookRepository notebookRepository,
                           NotebookCellRepository notebookCellRepository) {
        this.notebookRepository = notebookRepository;
        this.notebookCellRepository = notebookCellRepository;
    }

    public Notebook createNotebook(CreateNotebookRequest request) {
        Notebook notebook = Notebook.from(request.getName(), request.getLanguage());
        notebook = notebookRepository.save(notebook);
        logger.info("Notebook created [{}]", notebook);

        return notebook;
    }

    public NotebookData getNotebook(UUID notebookId) throws Exception {
        Notebook notebook = notebookRepository.findById(NotebookId.from(notebookId.toString()))
                .orElseThrow(() -> new Exception("Notebook not found"));
        List<NotebookCell> cells = notebookCellRepository.findAllByNotebookId(NotebookId.from(notebookId.toString()));

        return NotebookData.from(notebook, cells);
    }

    public CellDetails addNotebookCell(UUID notebookId, AddNotebookCellRequest request) throws Exception {
        Notebook notebook = notebookRepository.findById(NotebookId.from(notebookId.toString()))
                .orElseThrow(() -> new Exception("Notebook not found"));
        NotebookCell notebookCell = NotebookCell.from(
                notebook,
                notebook.getLanguage(),
                request.getCellInput(),
                request.getPosition()
        );
        notebookCell = notebookCellRepository.save(notebookCell);
        logger.info("Notebook cell added [{}] [{}]", notebook.id().getId(), notebookCell);

        return CellDetails.from(notebookCell);
    }

    public void removeNotebookCell(UUID notebookId, UUID notebookCellId) throws Exception {
        Notebook notebook = notebookRepository.findById(NotebookId.from(notebookId.toString()))
                .orElseThrow(() -> new Exception("Notebook not found"));

        notebookCellRepository.deleteById(NotebookCellId.from(notebookCellId.toString()));
    }

    public void runNotebookCell(UUID notebookId, UUID notebookCellId) throws Exception {
        NotebookCell cell = notebookCellRepository.findNotebookCell(
                        NotebookId.from(notebookId.toString()),
                        NotebookCellId.from(notebookCellId.toString())
                ).orElseThrow(() -> new Exception("Notebook cell could not be found"));

        cell.run();
        notebookCellRepository.save(cell);
    }
}
