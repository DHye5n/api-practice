package com.dgm.board.model.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final HttpStatus status;
    private final Object message;
}



