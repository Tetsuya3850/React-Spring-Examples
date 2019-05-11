package com.example.todoserver;

public class FormTodo {

    private String text;

    public FormTodo() {
    }

    public FormTodo(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
