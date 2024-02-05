package org.example.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CUSER") // 예약어 제외용
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 50, unique = true)
    private String name;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @OneToMany(mappedBy = "user")
    private List<UserAuthority> authorities = new ArrayList<UserAuthority>();

}
