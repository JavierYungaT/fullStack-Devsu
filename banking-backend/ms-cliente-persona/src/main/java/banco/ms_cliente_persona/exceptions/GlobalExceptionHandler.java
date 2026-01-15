package banco.ms_cliente_persona.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import banco.ms_cliente_persona.dto.ErrorMessageDTO;
import banco.ms_cliente_persona.dto.ErrorValidationMessageDTO;
import banco.ms_cliente_persona.enums.ErrorCodesEnum;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        ErrorValidationMessageDTO message = new ErrorValidationMessageDTO(HttpStatus.BAD_REQUEST, ErrorCodesEnum.BAD_REQUEST_CODE.getMessage(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageDTO> handleCustomValidationException(CustomValidationException e) {
        ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.BAD_REQUEST, ErrorCodesEnum.BAD_REQUEST_CODE.getMessage(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessageDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.NOT_FOUND, ErrorCodesEnum.NOT_FOUND_CODE.getMessage(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
