package exceptions;

import org.springframework.http.HttpStatus;

public record ErrorDto(
        String msg,
        String code
) {
}
