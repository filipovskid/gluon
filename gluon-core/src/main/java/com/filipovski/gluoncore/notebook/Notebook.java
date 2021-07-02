package com.filipovski.gluoncore.notebook;

import com.filipovski.common.domain.AbstractEntity;

import javax.persistence.Entity;
import java.util.Objects;

@Entity(name = "notebooks")
public class Notebook extends AbstractEntity<NotebookId> {

    private String name;

    private String language;

    protected Notebook() { }

    private Notebook(String name, String language) {
        super(NotebookId.randomId(NotebookId.class));

        this.name = name;
        this.language = language;
    }

    public static Notebook from(String name, String language) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(language, "language must not be null");

        return new Notebook(name, language);
    }
}
