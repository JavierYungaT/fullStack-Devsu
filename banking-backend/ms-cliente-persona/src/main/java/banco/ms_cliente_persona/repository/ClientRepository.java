package banco.ms_cliente_persona.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import banco.ms_cliente_persona.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>
{

}
