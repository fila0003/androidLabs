package com.cst2335.fila0003;

public class Message {
    private String text; // message string
    private boolean isSender;
    private long id;
    public Message(String text, boolean sender, long id) {
        this.text = text;
        this.isSender = isSender;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSender() {
        return this.isSender;
    }

    public void setSender() {
        this.isSender = isSender;
    }


    public long getId() {
        return  id;
    }
}



