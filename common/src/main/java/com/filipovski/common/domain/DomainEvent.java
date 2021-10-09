package com.filipovski.common.domain;

import java.time.Instant;

public interface DomainEvent {
    Instant occuredOn();
}
