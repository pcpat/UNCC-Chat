package com.example.himanshudhawale.hackathon;

public class Events  {
    String name, link, descritpionl, eventID;

    @Override
    public String toString() {
        return "Events{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", descritpionl='" + descritpionl + '\'' +
                '}';
    }

    public Events(String name, String link, String descritpionl, String eventID) {
        this.name = name;
        this.link = link;
        this.descritpionl = descritpionl;
        this.eventID=eventID;
    }

    public Events()
    {

    }
}
