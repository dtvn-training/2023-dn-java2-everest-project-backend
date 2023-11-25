package com.dtvn.springbootproject.repositories;

import com.dtvn.springbootproject.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);

//    @Query("SELECT new com.dtvn.springbootproject.Responses.AccountResponse(a.firstname, a.lastname, a.email, a.role, a.address, a.phone) FROM Account a")
//    Page<AccountResponse> findAllResponse(Pageable pageable);
}
