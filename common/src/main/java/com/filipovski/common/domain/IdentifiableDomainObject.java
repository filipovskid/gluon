package com.filipovski.common.domain;

import java.io.Serializable;

public interface IdentifiableDomainObject<ID extends DomainObjectId> {
    ID id();
}
