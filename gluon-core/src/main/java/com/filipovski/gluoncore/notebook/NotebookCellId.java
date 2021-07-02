package com.filipovski.gluoncore.notebook;

import com.filipovski.common.domain.DomainObjectId;
import com.google.common.base.Strings;

public class NotebookCellId extends DomainObjectId {

    public NotebookCellId() {
        super("");
    }

    public NotebookCellId(String id) {
        super(id);
    }

    public static NotebookCellId from(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        return new NotebookCellId(id);
    }
}
