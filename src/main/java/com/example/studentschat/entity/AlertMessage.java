package com.example.studentschat.entity;

import java.util.HashMap;

public class AlertMessage {
    public static final String CREATE_REQUEST_SUCCESS_MESSAGE = "Pomyślnie wysłano zapytanie o zmianę grupy";
    public static final String ACCEPT_REQUEST_SUCCESS_MESSAGE = "Pomyślnie zaakceptowano zmianę grupy";
    public static final String CREATE_REQUEST_FAILED_MESSAGE = "Wystąpił błąd podczas wysłania wniosku";
    public static final String ACCEPT_REQUEST_FAILED_MESSAGE = "Wystąpił błąd podczas akceptowania zmiany grupy";
    public static final String REJECT_REQUEST_SUCCESS_MESSAGE = "Pomyślnie odrzucono wniosek";

    public static final int CREATE_REQUEST_SUCCESS_MESSAGE_CODE = 100;
    public static final int ACCEPT_REQUEST_SUCCESS_MESSAGE_CODE = 101;
    public static final int REJECT_REQUEST_SUCCESS_MESSAGE_CODE = 102;
    public static final int CREATE_REQUEST_FAILED_MESSAGE_CODE = 200;
    public static final int ACCEPT_REQUEST_FAILED_MESSAGE_CODE = 201;

    final static HashMap<Integer,String> alertsMessagesMap = new HashMap<>();
    static{
        alertsMessagesMap.put(CREATE_REQUEST_SUCCESS_MESSAGE_CODE,CREATE_REQUEST_SUCCESS_MESSAGE);
        alertsMessagesMap.put(ACCEPT_REQUEST_SUCCESS_MESSAGE_CODE,ACCEPT_REQUEST_SUCCESS_MESSAGE);
        alertsMessagesMap.put(REJECT_REQUEST_SUCCESS_MESSAGE_CODE,REJECT_REQUEST_SUCCESS_MESSAGE);
        alertsMessagesMap.put(CREATE_REQUEST_FAILED_MESSAGE_CODE,CREATE_REQUEST_FAILED_MESSAGE);
        alertsMessagesMap.put(ACCEPT_REQUEST_FAILED_MESSAGE_CODE,ACCEPT_REQUEST_FAILED_MESSAGE);
    }

    Integer code;
    String message;

    int type;
    public AlertMessage(Integer code) {
        this.code = code;
        setMessage(code);
    }

    private void setMessage(Integer code) {
        type = Integer.parseInt(code.toString().substring(0,1));
        message = alertsMessagesMap.get(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }
}
