package com.awslambda.api.CrmContactApp.model;

import com.google.gson.Gson;

public class Contact {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private String subject;
    private String state;
    private enum State{
        CLOSED,
        NEGOTIATING,
        ON_HOLD
    }

    public Contact(int id, String name, String email, String phone, String company, String subject, String state) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.subject = subject;
        this.state = state;
    }

    public Contact(String json){
        Gson gson = new Gson();
        Contact tempContact = gson.fromJson(json, Contact.class);
        this.id = tempContact.id;
        this.name = tempContact.name;
        this.email = tempContact.email;
        this.phone = tempContact.phone;
        this.company = tempContact.company;
        this.subject = tempContact.subject;
        this.state = tempContact.state;
    }
    public String toString(){
        return new Gson().toJson(this);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
