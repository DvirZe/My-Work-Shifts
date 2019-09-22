package com.example.classes;

import com.example.database.Register;

import java.io.Serializable;

public class User implements UserInterface, Serializable {

    private String firstName;
    private String lastName;
    private String company;
    private String wage;
    private String email;

    public User(Register r) {
        firstName = r.firstName;
        lastName = r.lastName;
        company = r.company;
        wage = r.wage;
        email = r.email;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getCompany() {
        return company;
    }

    @Override
    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String getWage() {
        return wage;
    }

    @Override
    public void setWage(String wage) {
        this.wage = wage;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
