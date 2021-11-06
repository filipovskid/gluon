package com.filipovski.gluon.core.notebook;

import com.filipovski.common.domain.AbstractEntity;
import com.filipovski.gluon.core.notebook.events.NotebookStartingEvent;
import com.filipovski.gluon.core.notebook.events.NotebookStoppingEvent;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Version;
import java.util.Objects;

@Getter
@Entity(name = "notebooks")
public class Notebook extends AbstractEntity<NotebookId> {

    @Version
    private Long version;

    private String name;

    private String language;

    @Enumerated(EnumType.STRING)
    private NotebookStatus status;

    protected Notebook() { }

    private Notebook(String name, String language) {
        super(NotebookId.randomId(NotebookId.class));

        this.name = name;
        this.language = language;
        this.status = NotebookStatus.STOPPED;
    }

    public void start(String sessionId) {
        transitionState(NotebookStatus.STARTING);
        registerEvent(NotebookStartingEvent.from(this, sessionId));
    }

    public void stop(String sessionId) {
        transitionState(NotebookStatus.STOPPING);
        registerEvent(NotebookStoppingEvent.from(this, sessionId));
    }

    public boolean isStarted() {
        return status == NotebookStatus.STARTED;
    }

    public boolean transitionState(NotebookStatus status) {
        // Can transition to any state
        this.status = status;

        return true;
    }

    public String getId() {
        return id().getId();
    }

    public static Notebook from(String name, String language) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(language, "language must not be null");

        return new Notebook(name, language);
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "id='" + id().getId() + '\'' +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
