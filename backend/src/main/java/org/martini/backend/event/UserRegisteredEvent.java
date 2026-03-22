package org.martini.backend.event;

import lombok.Builder;

@Builder
public record UserRegisteredEvent(String token, String email) {
}
