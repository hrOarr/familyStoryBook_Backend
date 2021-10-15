package com.astrodust.familyStoryBook_backend.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info("handleMethodArgumentNotValid------------->");
        List<String> errors = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse("Validation Failed", errors, LocalDateTime.now());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class, ResourceNotFoundException.class})
    public ResponseEntity<Object> handleValidationException(Exception ex, WebRequest request) {
        logger.info("ValidationException->method starts------------->");
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse("Validation Failed", errors, LocalDateTime.now());
        return new ResponseEntity<Object>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest webRequest){
        logger.info("handleAllExceptions--------------->");
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", errors, LocalDateTime.now());
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception e, WebRequest request) {
        logger.info("AccessDeniedException->method starts------------->");
        return new ResponseEntity<Object>("Access is denied", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String name = ex.getParameterName();
        logger.error(name + " parameter is missing");
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }
}
