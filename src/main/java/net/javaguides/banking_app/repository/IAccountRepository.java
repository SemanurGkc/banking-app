package net.javaguides.banking_app.repository;

import net.javaguides.banking_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account,Long> {

}
