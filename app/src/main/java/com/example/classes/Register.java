package com.example.classes;

public class Register {
    public String email;
    public String firstName;
    public String lastName;
    public String company;
    public String wage;

    public Register(String email, String firstName, String lastName, String company, String wage) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.wage = wage;
    }

    public Register() {}
}
