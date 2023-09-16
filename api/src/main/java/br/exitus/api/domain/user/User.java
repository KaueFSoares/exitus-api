package br.exitus.api.domain.user;

import br.exitus.api.domain.earlyexit.EarlyExit;
import br.exitus.api.domain.register.Register;
import br.exitus.api.domain.role.Role;
import br.exitus.api.domain.user.dto.SignupRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(onlyExplicitlyIncluded = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Size(max = 36)
    @ToString.Include
    private String id;

    @Column(unique = true)
    @ToString.Include
    private String email;

    private String password;

    private Boolean active = true;

    private String name;

    @Column(unique = true)
    @Size(max = 36)
    @ToString.Include
    private String fingerprint = UUID.randomUUID().toString();

    private String enrollment;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Shift shift;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "guardian_guarded",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "guarded_id", referencedColumnName = "id"))
    private Set<User> guardeds = new HashSet<>();

    @ManyToMany(mappedBy = "guardeds", cascade = CascadeType.ALL)
    private Set<User> guardians = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Register> registers = new HashSet<>();

    @OneToMany(mappedBy = "guarded", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EarlyExit> earlyExits = new HashSet<>();

    public User (SignupRequestDTO dto) {
        this.email = dto.email();
        this.name = dto.name();
        this.enrollment = dto.enrollment();
        this.birthDate = dto.birth();
        this.shift = dto.shift();
    }


    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(Role::getRoleEnum)
                .map(roleEnum -> (GrantedAuthority) roleEnum::name).toList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
