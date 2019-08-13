package config;

import base.dto.BaseResponseCode;
import base.dto.Response;
import base.dto.ResponseCode;
import base.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author csieflyman
 */
@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleBaseException(BaseException ex, HttpServletRequest request) {
        log.error(String.format("[%s] ", ex.getResponseCode().getMessage()) + request.getMethod() + " " + request.getRequestURI(), ex);
        ResponseCode responseCode = ex.getResponseCode();
        return ResponseEntity.status(HttpStatus.valueOf(responseCode.getStatusCode())).body(new Response(ex));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleDefaultException(Throwable ex, HttpServletRequest request) {
        log.error("[INTERNAL SERVER ERROR] " + request.getMethod() + " " + request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(BaseResponseCode.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
