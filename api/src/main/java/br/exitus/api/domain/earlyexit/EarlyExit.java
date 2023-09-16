package br.exitus.api.domain.earlyexit;

import br.exitus.api.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "earlyExits")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EarlyExit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "earlyExitId")
    @Size(max = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "guarded")
    private User guarded;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalTime time;

}
