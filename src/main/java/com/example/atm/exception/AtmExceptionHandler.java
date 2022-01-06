package com.example.atm.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AtmExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AmountException.class)
    public ResponseEntity<Object> handleAmountException(AmountException exception, WebRequest request) {
        return handleExceptionInternal(exception, "bad amount", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(CashStorageContentException.class)
    public ResponseEntity<Object> handleCashStorageContentException(CashStorageContentException exception, WebRequest request) {
        return handleExceptionInternal(exception, "cash problem", new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

}
