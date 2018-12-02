package com.example.himanshudhawale.hackathon;



import java.io.Serializable;

public class User implements Serializable {

    public String first, last, email, uID, password;


    public User(String first, String last, String email, String uID, String password) {
        this.first = first;
        this.last = last;
        this.email = email;
        this.uID = uID;
        this.password = password;
    }

    public User()
    {

    }


}