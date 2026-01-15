package banco.ms_cuenta_movimiento.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_account_seq")
    @SequenceGenerator(name = "t_account_seq", sequenceName = "t_account_seq", allocationSize = 1)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "account_balance", nullable = false)
    private BigDecimal accountBalance;

    @Column(name = "account_client_identification", nullable = false)
    private String clientIdentification;

    @Column(name = "account_client_name", nullable = false)
    private String clientName;

    @Column(name = "account_state")
    private Boolean accountState;


    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Movement> movements;
}
