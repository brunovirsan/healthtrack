package com.brunovirsan.healthtrack.domain.exception;


/**
 * Exceção de DOMÍNIO - representa violação de regra de negócio
 * 
 * Por que RuntimeException? Porque erros de validação de domínio
 * são erros de programação (não deveria chegar aqui com dados inválidos).
 */
public class InvalidPatientException extends RuntimeException {
    
    public InvalidPatientException(String message) {
        super(message);
    }
    
    public InvalidPatientException(String message, Throwable cause) {
        super(message, cause);
    }
}