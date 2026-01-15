package banco.ms_cliente_persona.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import banco.ms_cliente_persona.model.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{

    Optional<Person> findByPersonDni(String personDni);
}
