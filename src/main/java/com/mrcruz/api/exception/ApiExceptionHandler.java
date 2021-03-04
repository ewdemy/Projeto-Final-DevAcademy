package com.mrcruz.api.exception;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(Negocioexception.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Erro> quebraRegraNegocio(Negocioexception ex) {
		Erro erro = new Erro();
		erro.setMensagem(ex.getMessage());
		return ResponseEntity.unprocessableEntity().body(erro);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void notFound() {
		
	}
	
	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public void unsuported() {
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
     
        });
        return ResponseEntity.badRequest().body(errors);
    }   
}
