package org.lkcode.start_spring_security.dto;

public record RegisterRequest(
        String username,
        String password
) {
}
