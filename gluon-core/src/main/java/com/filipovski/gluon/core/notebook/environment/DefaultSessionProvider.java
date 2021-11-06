package com.filipovski.gluon.core.notebook.environment;

import com.filipovski.gluon.core.notebook.Notebook;
import com.filipovski.gluon.core.notebook.NotebookId;
import org.springframework.stereotype.Service;

@Service
public class DefaultSessionProvider implements SessionProvider {

    @Override
    public String obtainSessionId(Notebook notebook) {
        return notebook.getId();
    }

    @Override
    public NotebookId getNotebookIdFromSession(String sessionId) {
        return NotebookId.from(sessionId);
    }
}
