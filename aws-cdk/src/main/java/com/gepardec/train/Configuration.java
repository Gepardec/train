package com.gepardec.train;

import java.time.LocalDateTime;

public record Configuration(
    String training,
    int instanceCount,
    String amiName,
    LocalDateTime executionTime
) {

    public String indexedIdSuffix(String name, int i) {
        return idSuffix(name) + i;
    }

    public String idSuffix(String name) {
        return name + training;
    }
}
