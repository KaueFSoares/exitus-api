package br.exitus.api.domain.role;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Size(max = 36)
    private String id;

    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

}
