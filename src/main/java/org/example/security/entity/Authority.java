package org.example.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AUTHORITY")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    @Enumerated(EnumType.STRING)
    private AuthorityEnum authorityEnum;

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL)
    private Set<UserAuthority> Users = new HashSet<>();

}
