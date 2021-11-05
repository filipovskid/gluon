package com.filipovski.gluon.core.notebook;

import com.filipovski.gluon.core.notebook.dto.AddNotebookCellRequest;
import com.filipovski.gluon.core.notebook.dto.CellDetails;
import com.filipovski.gluon.core.notebook.dto.CreateNotebookRequest;
import com.filipovski.gluon.core.notebook.dto.NotebookData;
import com.filipovski.gluon.core.notebook.environment.SessionProvider;
import com.filipovski.gluon.core.notebook.events.NotebookCellOutputEvent;
import com.filipovski.gluon.core.notebook.events.NotebookCellStateUpdateEvent;
import com.filipovski.gluon.core.notebook.events.NotebookStartingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotebookService {

    private final Logger logger = LoggerFactory.getLogger(NotebookService.class);
    private final ApplicationEventPublisher publisher;
    private final NotebookRepository notebookRepository;
    private final NotebookCellRepository notebookCellRepository;
    private final SessionProvider sessionProvider;

    public NotebookService(ApplicationEventPublisher publisher,
                           NotebookRepository notebookRepository,
                           NotebookCellRepository notebookCellRepository,
                           SessionProvider sessionProvider) {
        this.publisher = publisher;
        this.notebookRepository = notebookRepository;
        this.notebookCellRepository = notebookCellRepository;
        this.sessionProvider = sessionProvider;
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
        List<NotebookCell> cells = notebookCellRepository.findAllByNotebookId(notebook.id());

        return NotebookData.from(notebook, cells);
    }

    public NotebookData startNotebook(UUID notebookId) throws Exception {
        // TODO: Probably the logic needs to be in a domain service, but I will take a shortcut
        Notebook notebook = notebookRepository.findById(NotebookId.from(notebookId.toString()))
                .orElseThrow(() -> new Exception("Notebook not found"));
        List<NotebookCell> cells = notebookCellRepository.findAllByNotebookId(notebook.id());
        String sessionId = sessionProvider.obtainSessionId(notebook);

        notebook.start(sessionId);
        notebookRepository.save(notebook);

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

    @EventListener
    public void onNotebookCellStateUpdate(NotebookCellStateUpdateEvent event) throws Exception {
        NotebookCell cell = findNotebookCell(event.getNotebookId(), event.getCellId());
        cell.transitionState(event.getStatus());
        notebookCellRepository.save(cell);
    }

    @EventListener
    public void onNotebookCellOutputArrival(NotebookCellOutputEvent event) throws Exception {
        NotebookCell cell = findNotebookCell(event.getNotebookId(), event.getCellId());
        cell.appendOutput(event.getData());
        notebookCellRepository.save(cell);
    }

    private NotebookCell findNotebookCell(NotebookId notebookId, NotebookCellId cellId) throws Exception {
        return notebookCellRepository.findNotebookCell(notebookId, cellId)
                .orElseThrow(() -> new Exception("Notebook cell could not be found!"));
    }
}
