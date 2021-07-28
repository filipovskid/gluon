package com.filipovski.gluoncore.job;

import com.filipovski.common.domain.DomainEvent;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "jobs")
public abstract class Job {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String sessionId;

    @Transient
    private final List<DomainEvent> domainEvents = new LinkedList<>();

    public Job(String sessionId) {
        if(Strings.isNullOrEmpty(sessionId))
            throw new IllegalArgumentException("sessionId must not be null");

        this.sessionId = sessionId;
    }

    public abstract void completed();

    @DomainEvents
    public Collection<DomainEvent> events() {
        return domainEvents;
    }

    public void register(DomainEvent event) {
        this.domainEvents.add(event);
    }

    @AfterDomainEventPublication
    public void clearEvents() {
        domainEvents.clear();
    }
}
