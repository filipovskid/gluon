package com.filipovski.gluon.core.notebook.environment;

import com.filipovski.gluon.core.notebook.Notebook;
import org.springframework.stereotype.Service;

@Service
public class DefaultSessionProvider implements SessionProvider {

    @Override
    public String obtainSessionId(Notebook notebook) {
        return notebook.getId();
    }
}
