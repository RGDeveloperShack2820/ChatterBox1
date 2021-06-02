package com.basic.chatterbox1;

public class MessageModal {

    String chatId,creator,text;

    public MessageModal(String chatId, String creator, String text) {
        this.chatId = chatId;
        this.creator = creator;
        this.text = text;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
