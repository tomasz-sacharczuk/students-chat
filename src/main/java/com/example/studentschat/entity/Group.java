package com.example.studentschat.entity;

import com.example.studentschat.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="group_id")
    private Long id;

    @Column(name="group_limit")
    private Integer limit;
    @Column(name="group_name")
    private String name;

    @OneToMany(mappedBy="group")
    private Set<User> groupUsers = new HashSet<>();

    public Group(String name, Integer limit){
        this.name = name;
        this.limit = limit;
    }
}
