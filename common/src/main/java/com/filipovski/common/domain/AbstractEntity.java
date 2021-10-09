package com.filipovski.common.domain;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.util.ProxyUtils;
import org.springframework.util.Assert;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
public abstract class AbstractEntity<ID extends DomainObjectId> implements IdentifiableDomainObject<ID> {

    @EmbeddedId
    ID id;

    @Transient
    private transient final List<Object> domainEvents = new ArrayList<>();

    public AbstractEntity() {}

    public AbstractEntity(ID id) {
        this.id = id;
    }

    @Override
    public ID id() {
        return id;
    }

    protected <T> T registerEvent(T event) {

        Assert.notNull(event, "Domain event must not be null!");

        this.domainEvents.add(event);
        return event;
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    @DomainEvents
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) return false;

        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? super.hashCode() : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), id);
    }
}
