package com.example.triptogether;

public class model
{
  String trips,course,email,purl;
    model()
    {

    }
    public model(String name, String course, String email, String purl) {
        this.trips = name; // toCity
        this.course = course; //fromCity
        this.email = email; // date
        this.purl = purl; // time
    }

    public String getName() {
        return trips;
    }

    public void setName(String name) {
        this.trips = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
