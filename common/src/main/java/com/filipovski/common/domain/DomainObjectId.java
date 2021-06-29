package com.filipovski.common.domain;

import org.springframework.lang.NonNull;

import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public class DomainObjectId {

    private final String id;

    public DomainObjectId(String id) {
        this.id = id;
    }

    @NonNull
    public static <ID extends DomainObjectId> ID randomId(@NonNull Class<ID> idClass) {
        Objects.requireNonNull(idClass, "idClass must not be null!");

        try {
            return idClass.getConstructor(String.class).newInstance(UUID.randomUUID().toString());
        } catch (Exception e) {
            throw new RuntimeException("Could not create a new instance of " + idClass, e);
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObjectId)) return false;
        DomainObjectId that = (DomainObjectId) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
