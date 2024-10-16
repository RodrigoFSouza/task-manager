package br.com.rfs.tasksmanager.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(Long id,
                           String username,
                           @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
                           LocalDateTime createdAt,
                           @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
                           LocalDateTime updatedAt
) {

}
