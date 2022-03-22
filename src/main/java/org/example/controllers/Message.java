package org.example.controllers;

import javafx.beans.property.SimpleStringProperty;

public class Message {
    private SimpleStringProperty message;

    public Message (String word) {
        this.message = new SimpleStringProperty (word);
    }

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

}
