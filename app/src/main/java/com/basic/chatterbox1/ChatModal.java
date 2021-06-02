package com.basic.chatterbox1;

import java.io.Serializable;

public class ChatModal implements Serializable {

    private  String chatID;
    private String chatName;
    private String chatPhno;

    public ChatModal(String chatID, String chatName, String chatPhno) {
        this.chatID = chatID;
        this.chatName = chatName;
        this.chatPhno = chatPhno;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getChatID() {
        return chatID;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatPhno() {
        return chatPhno;
    }

    public void setChatPhno(String chatPhno) {
        this.chatPhno = chatPhno;
    }
}
