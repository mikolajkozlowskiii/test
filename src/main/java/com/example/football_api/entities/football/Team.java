package com.example.football_api.entities.football;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table(	name = "teams")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, nullable = false, unique = true)
    @Size(min = 3, max = 30)
    private String name;
}
