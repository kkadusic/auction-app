package com.atlantbh.auctionapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomExceptionResponse body = getDefaultExceptionBody(status, request);
        System.out.println(request.toString() + ex.toString() + headers.toString() + status.toString());
        body.setMessage("JSON parse error");
        return new ResponseEntity<>(body, headers, status);
    }

    private CustomExceptionResponse getDefaultExceptionBody(HttpStatus status, WebRequest request) {
        CustomExceptionResponse body = new CustomExceptionResponse();
        body.setStatus(status.value());
        body.setError(status.getReasonPhrase());
        body.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        return body;
    }
}
