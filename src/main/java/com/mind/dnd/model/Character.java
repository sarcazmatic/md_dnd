package com.mind.dnd.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "characters")
@Data
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Long ownerId;
    private Long expPoints;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private CharClass charClass;
    private int str;
    private int dex;
    private int con;
    private int wis;
    private int inl;
    private int chr;

}
