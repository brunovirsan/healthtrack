package com.brunovirsan.healthtrack.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.brunovirsan.healthtrack.domain.exception.InvalidPatientException;

/**
 * Patient é uma entidade de DOMÍNIO.
 * 
 * Características:
 * - Imutável (todos os campos final)
 * - Tem regras de negócio (método validate())
 * - NÃO conhece banco de dados, JPA, Spring, nada!
 * - É um POJO puro
 * 
 * Vamos usar Lombok para reduzir boilerplate.
 */
public class Patient {
    
    private final UUID id;
    private final String cpf;
    private final String name;
    private final LocalDate birthDate;
    private final String email;
    private final String phone;
    private final LocalDateTime createdAt;
    
    // Construtor privado - força uso do Builder
    private Patient(UUID id, String cpf, String name, LocalDate birthDate, 
                   String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getCpf() { return cpf; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * REGRA DE NEGÓCIO: Validação de domínio
     * 
     * Por que aqui? Porque SEMPRE que um Patient existir,
     * ele deve estar válido. Isso é regra de NEGÓCIO.
     */
    public void validate() {
        if (cpf == null || !isValidCpf(cpf)) {
            throw new InvalidPatientException("CPF inválido: " + cpf);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidPatientException("Nome é obrigatório");
        }
        if (email == null || !email.contains("@")) {
            throw new InvalidPatientException("Email inválido");
        }
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new InvalidPatientException("Data de nascimento inválida");
        }
    }
    
    /**
     * Validação de CPF - Algoritmo oficial
     */
    private boolean isValidCpf(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // CPF deve ter 11 dígitos
        if (cpf.length() != 11) return false;
        
        // Rejeita CPFs conhecidos como inválidos
        if (cpf.matches("(\\d)\\1{10}")) return false; // 000.000.000-00, 111.111.111-11, etc
        
        // Calcula primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) firstDigit = 0;
        
        // Calcula segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) secondDigit = 0;
        
        // Verifica se os dígitos calculados conferem
        return cpf.charAt(9) == Character.forDigit(firstDigit, 10) &&
               cpf.charAt(10) == Character.forDigit(secondDigit, 10);
    }
    
    /**
     * Builder Pattern - Facilita criação de objetos complexos
     * 
     * Por que Builder? Porque temos muitos campos e queremos
     * uma API fluente para criar objetos.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private UUID id;
        private String cpf;
        private String name;
        private LocalDate birthDate;
        private String email;
        private String phone;
        private LocalDateTime createdAt;
        
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public Builder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Patient build() {
            // Preenche valores default se necessário
            if (id == null) {
                id = UUID.randomUUID();
            }
            if (createdAt == null) {
                createdAt = LocalDateTime.now();
            }
            
            Patient patient = new Patient(id, cpf, name, birthDate, email, phone, createdAt);
            patient.validate(); // Valida antes de retornar
            return patient;
        }
    }
    
    @Override
    public String toString() {
        return "Patient{id=" + id + ", name=" + name + ", cpf=" + cpf + "}";
    }
}