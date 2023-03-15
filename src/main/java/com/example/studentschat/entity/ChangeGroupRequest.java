package com.example.studentschat.entity;

import com.example.studentschat.entity.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "changeGroupRequests")
public class ChangeGroupRequest {

    public static final int STATUS_NEW = 1;
    public static final int STATUS_ACCEPTED_BY_USER = 2;
    public static final int STATUS_REJECTED = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="request_id")
    private Long id;
    @OneToOne
    @JoinColumn(name="user_id", nullable=false)
    private User requestedByUser;
    @OneToOne
    @JoinColumn(name="user_id2", nullable=false)
    private User requestedToUser;
    /**
     * 1 - NEW,
     * 2 - ACCEPTED BY USER,
     * 3 - REJECTED
     */
    private Integer status;

    public ChangeGroupRequest(User requestedByUser, User requestedToUser){
        this.requestedByUser = requestedByUser;
        this.requestedToUser = requestedToUser;
        this.status = 1;
    }
}
