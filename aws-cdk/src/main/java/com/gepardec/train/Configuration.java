package com.gepardec.train;

import java.time.LocalDateTime;

public record Configuration(
    String training,
    int instanceCount,
    String amiName,
    LocalDateTime executionTime
) {
}
