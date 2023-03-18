package com.example.studentschat.component;

import com.example.studentschat.entity.AlertMessage;
import com.example.studentschat.entity.ChangeGroupRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;

@Component
public class StatusProvider {

    public Integer getNewStatusByAction(String action) {
        return getNewStatusAndMessageCodeByAction(action,0);
    }

    public Integer getMessageCodeByAction(String action) {
        return getNewStatusAndMessageCodeByAction(action, 1);
    }

    public Integer getNewStatusAndMessageCodeByAction(String action, int listElement) {
        Integer newStatus = null;
        Integer messageCode = null;
        switch(action)
        {
            case "accepted_by_user":
                newStatus = ChangeGroupRequest.STATUS_ACCEPTED_BY_USER;
                messageCode = AlertMessage.ACCEPT_REQUEST_SUCCESS_MESSAGE_CODE;
                break;
            case "rejected_by_user":
                newStatus = ChangeGroupRequest.STATUS_REJECTED_BY_USER_INFO;
                messageCode = AlertMessage.REJECT_REQUEST_SUCCESS_MESSAGE_CODE;
                break;
            case "accepted_by_admin":
                newStatus = ChangeGroupRequest.STATUS_ACCEPTED_BY_ADMIN_INFO;
                messageCode = AlertMessage.ACCEPT_REQUEST_SUCCESS_MESSAGE_CODE;
                break;
            case "rejected_by_admin":
                newStatus = ChangeGroupRequest.STATUS_REJECTED_BY_ADMIN_INFO;
                messageCode = AlertMessage.REJECT_REQUEST_SUCCESS_MESSAGE_CODE;
                break;
            default:
                break;
        }
        LinkedList<Integer> statusList = new LinkedList<>(Arrays.asList(newStatus, messageCode));
        if (statusList.size() != 2)
            throw new IllegalStateException("An unexpected action has been handed over: " +action);
        return statusList.get(listElement);
}

}
