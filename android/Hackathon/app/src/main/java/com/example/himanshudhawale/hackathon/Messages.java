package com.example.himanshudhawale.hackathon;


import java.io.Serializable;

public class Messages implements Serializable {

    public String message, userName, timeStamp, userID, messageID, url;


    public Messages(String message, String userName, String timeStamp, String userID, String messageID, String url) {
        this.message = message;
        this.userName = userName;
        this.timeStamp = timeStamp;
        this.userID=userID;
        this.messageID=messageID;
        this.url=url;
    }


    public Messages()
    {

    }


}


