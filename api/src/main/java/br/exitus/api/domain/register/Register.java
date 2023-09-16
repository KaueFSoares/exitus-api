package br.exitus.api.domain.register;

import br.exitus.api.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registers")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "registerId")
    @Size(max = 36)
    private String id;

    @Enumerated(EnumType.STRING)
    private RegisterType type;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Register() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
