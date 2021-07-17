package com.filipovski.gluoncore.notebook;

import com.filipovski.gluoncore.notebook.dto.AddNotebookCellRequest;
import com.filipovski.gluoncore.notebook.dto.CellDetails;
import com.filipovski.gluoncore.notebook.dto.CreateNotebookRequest;
import com.filipovski.gluoncore.notebook.dto.NotebookData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/notebook")
public class NotebookController {

    private final NotebookService notebookService;

    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    @PostMapping
    public ResponseEntity<Notebook> createNotebook(@RequestBody CreateNotebookRequest request) {
        return ResponseEntity.ok(notebookService.createNotebook(request));
    }

    @GetMapping("/{notebook-id}")
    public ResponseEntity<NotebookData> getNotebook(@PathVariable("notebook-id") UUID notebookId) throws Exception {
        return ResponseEntity.ok(notebookService.getNotebook(notebookId));
    }

    @PostMapping("/{notebook-id}/cell")
    public ResponseEntity<CellDetails> addNotebookCell(@PathVariable("notebook-id") UUID notebookId,
                                                       @RequestBody AddNotebookCellRequest request) throws Exception {
        return ResponseEntity.ok(notebookService.addNotebookCell(notebookId, request));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{notebook-id}/cell/{cell-id}")
    public void removeNotebookCell(@PathVariable("notebook-id") UUID notebookId,
                                   @PathVariable("cell-id") UUID notebookCellId) throws Exception {
        notebookService.removeNotebookCell(notebookId, notebookCellId);
    }

    @GetMapping("/{notebook-id}/cell/{cell-id}/run")
    public void runNotebookCell(@PathVariable("notebook-id") UUID notebookId,
                                @PathVariable("cell-id") UUID notebookCellId) throws Exception {
        notebookService.runNotebookCell(notebookId, notebookCellId);
    }
}
