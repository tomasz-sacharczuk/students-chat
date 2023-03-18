package com.example.studentschat.entity;

import com.example.studentschat.entity.user.User;
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
    public static final int STATUS_REJECTED_BY_USER_INFO = 3;
    public static final int STATUS_ACCEPTED_BY_ADMIN_INFO = 4;
    public static final int STATUS_REJECTED_BY_ADMIN_INFO  = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="request_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User requestedByUser;
    @ManyToOne
    @JoinColumn(name="user_id2", nullable=false)
    private User requestedToUser;
    private Integer status;

    public ChangeGroupRequest(User requestedByUser, User requestedToUser){
        this.requestedByUser = requestedByUser;
        this.requestedToUser = requestedToUser;
        this.status = 1;
    }

    public boolean statusForDelete() {
        return status == ChangeGroupRequest.STATUS_REJECTED_BY_USER_INFO
                || status == ChangeGroupRequest.STATUS_ACCEPTED_BY_ADMIN_INFO
                || status == ChangeGroupRequest.STATUS_REJECTED_BY_ADMIN_INFO;
    }
}
