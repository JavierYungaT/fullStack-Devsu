package banco.ms_cliente_persona.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor

public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_person_seq")
    @SequenceGenerator(name = "t_person_seq", sequenceName = "t_person_seq", allocationSize = 1)
    @Column(name = "per_id")
    private Long personId;

    @Column(name = "per_dni", unique = true, nullable = false)
    @NotNull(message = "La cedula es requerida")
    private String personDni;

    @Column(name = "per_name", nullable = false)
    @NotNull(message = "El nombre es requerido")
    private String personName;

    @Column(name = "per_genre", nullable = false)
    @NotNull(message = "El genero es requerido")
    private String personGenre;

    @Column(name = "per_age", nullable = false)
    @NotNull(message = "La edad es requerida")
    private Integer personAge;

    @Column(name = "per_address", nullable = false)
    @NotNull(message = "La direccion es requerida")
    private String personAddress;

    @Column(name = "per_phone", nullable = false)
    @Pattern(regexp = "[0-9]+", message = "El telefono solo debe contener numeros")
    @NotNull(message = "El telefono es requerido")
    private String personPhoneNumber;


}
