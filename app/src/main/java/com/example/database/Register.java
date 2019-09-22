package com.example.database;

import com.example.classes.User;

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

    public Register(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.company = user.getCompany();
        this.wage = user.getWage();
        this.email = user.getEmail();
    }

    public Register() {}
}
