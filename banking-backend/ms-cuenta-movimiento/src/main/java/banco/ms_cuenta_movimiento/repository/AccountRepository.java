package banco.ms_cuenta_movimiento.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import banco.ms_cuenta_movimiento.model.Account;

import java.util.List;
import java.util.Optional;

//@Repository
//public interface AccountRepository extends JpaRepository<Account, Integer> {
//
//    Optional<Account> findByAccountNumber(String accountNumber);
//
//    List<Account> findByClientIdentification(String clientDni);
//}

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByAccountNumber(String accountNumber);

    // Usar el nombre del campo JAVA (clientIdentification), no el de la BD
    List<Account> findByClientIdentification(String clientDni);
}
