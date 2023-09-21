package com.hrmG3.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;


//@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ErrorMessage createError(ErrorType errorType, Exception exception) {
        //System.out.println();
        log.error("Hata olustu " + exception.getMessage());
        return ErrorMessage.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity.badRequest().body("beklenmeyen bir hata olustu: " + exception.getMessage());
    }

    @ExceptionHandler(AuthManagerException.class)
    public ResponseEntity<ErrorMessage> handleMovieAppException(AuthManagerException exception) {
        ErrorType errorType = exception.getErrorType();
        HttpStatus httpStatus = errorType.getStatus();
        ErrorMessage error = createError(errorType, exception);
        error.setMessage(exception.getMessage());
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        List<String> fields = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(e->fields.add(e.getField()+": "+ e.getDefaultMessage()));
        ErrorMessage errorMessage = createError(errorType, exception);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(errorMessage,errorType.status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ErrorMessage> handleMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getStatus());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public final ResponseEntity<ErrorMessage> handleInvalidFormatException(
            InvalidFormatException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getStatus());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentMisMatchException(
            MethodArgumentTypeMismatchException exception) {

        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getStatus());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentMisMatchException(
            MissingPathVariableException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getStatus());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorMessage> handlePsqlException(DataIntegrityViolationException exception){
        ErrorType errorType=ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType,exception),errorType.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleAllExceptions(Exception exception) {
        ErrorType errorType = ErrorType.INTERNAL_ERROR_SERVER;
        List<String> fields = new ArrayList<>();
        fields.add(exception.getMessage());
        ErrorMessage errorMessage = createError(errorType, exception);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(createError(errorType, exception), errorType.getStatus());
    }

}

