package org.example.security.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AUTHORITY")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @GeneratedValue
    @Column(name = "authority_id")
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

}
