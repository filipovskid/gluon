package com.filipovski.gluoncore.notebook;

import com.filipovski.common.domain.DomainObjectId;
import com.google.common.base.Strings;

import javax.persistence.Embeddable;

@Embeddable
public class NotebookId extends DomainObjectId {

    public NotebookId() {
        super("");
    }

    public NotebookId(String id) {
        super(id);
    }

    public static NotebookId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new NotebookId(id);
    }
}
