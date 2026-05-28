package net.javaguides.banking_app.dto;

public record AccountDto(Long id,
                         String accountHolderName,
                         Long balance) {
}
