package ar.com.matiabossio.mattmdb.exception;

import ar.com.matiabossio.mattmdb.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    /*************************************
     *         400 - Bad Request         *
     *************************************/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            org.springframework.dao.DuplicateKeyException.class,
            org.springframework.web.HttpRequestMethodNotSupportedException.class,
            org.springframework.web.bind.MethodArgumentNotValidException.class,
            org.springframework.web.bind.MissingRequestHeaderException.class,
            org.springframework.web.bind.MissingServletRequestParameterException.class,
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
            org.springframework.http.converter.HttpMessageNotReadableException.class
    })
    public ResponseEntity<Message> handleArgumentNotValidException(MethodArgumentNotValidException notValidEx, HandlerMethod handlerMethod){

        BindingResult result = notValidEx.getBindingResult();

        Map<String, String> validations = new HashMap<>();

        result.getFieldErrors().forEach(validation -> {
            validations.put(validation.getField(), validation.getDefaultMessage());

        });

        Message body = new Message("Validations", "Validation Error", 400, false, validations);

        return ResponseEntity.badRequest().body(body);
    }


    /*************************************
     *          404 - Not Found          *
     *************************************/
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Message> handleNotFoundException(NotFoundException notFoundException, HandlerMethod handlerMethod){
        Message body;

        // log error:
        log.warn(notFoundException.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");

        body = new Message("Not Found", notFoundException.getMessage(), 404, false);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);

    }




    /*************************************
     *           409 - Conflict          *
     *************************************/
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Message> handleDataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException, HandlerMethod handlerMethod){

        Message body;

        // log error:
        log.warn(dataIntegrityViolationException.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");

        body = new Message("Data Integrity Violation", dataIntegrityViolationException.getMessage(), 409, false);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }


    /*************************************
     *  4XX - HttpClientErrorException   *
     *************************************/
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Message> handleHttpClientErrorException(HttpClientErrorException httpClientErrorException, HandlerMethod handlerMethod){

        Message body;

        // log error:
        log.warn(httpClientErrorException.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");

        body = new Message("Client Error", httpClientErrorException.getMessage(), httpClientErrorException.getStatusCode().value(), false);

        return ResponseEntity.status(httpClientErrorException.getStatusCode()).body(body);
    }

    /*************************************
     *        5XX - Server Error         *
     *************************************/

    @ExceptionHandler(JDBCConnectionException.class)
    public ResponseEntity<Message> handleJDBCConnectionException(JDBCConnectionException jdbcConnectionException, HandlerMethod handlerMethod){

        Message body;

        // log error:
        log.error(jdbcConnectionException.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");

        body = new Message("DB Connection Error", jdbcConnectionException.getMessage(), 503, false);

        return ResponseEntity.status(503).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Message> handleRuntimeException(RuntimeException runtimeException, HandlerMethod handlerMethod){

        Message body;

        // log error:
        log.error(runtimeException.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");

        body = new Message("Server Error", runtimeException.getMessage(), 500, false);

        return ResponseEntity.status(500).body(body);
    }

}
