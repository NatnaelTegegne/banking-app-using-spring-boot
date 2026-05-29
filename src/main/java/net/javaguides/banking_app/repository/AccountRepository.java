package net.javaguides.banking_app.repository;

import net.javaguides.banking_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> id(Long id);
}
