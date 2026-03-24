package org.martini.backend.event;

import lombok.Builder;

@Builder
public record UserRegisteredEvent(String link, String email) {
}
