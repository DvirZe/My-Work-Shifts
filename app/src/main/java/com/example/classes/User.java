package com.example.classes;

import java.io.Serializable;

public class User implements Serializable {

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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getEmail() {
        return email;
    }
}
