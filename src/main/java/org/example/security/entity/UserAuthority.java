package org.example.security.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_AUTHORITY")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthority {

    @Id
    @GeneratedValue
    @Column(name = "user_authority_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "authority_name")
    private Authority authority;

}
