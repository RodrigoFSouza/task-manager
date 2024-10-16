package br.com.rfs.tasksmanager.domain.dto.response;

import java.time.Instant;

public record LoginResponse(String accessToken, Instant expiresIn) {
}
