package com.filipovski.gluon.core.notebook.environment;

import com.filipovski.gluon.core.notebook.Notebook;
import com.filipovski.gluon.core.notebook.NotebookId;
import org.springframework.stereotype.Service;

@Service
public interface SessionProvider {

    String obtainSessionId(Notebook notebook);

    NotebookId getNotebookIdFromSession(String sessionId);
}
