package cse323.nsu.patienttracking.models;

import java.io.Serializable;

public class AvailableDoctor implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String degree;
    private String workplace;
    private String expertise;
    private String sex;
    private String phone;
    private String about;

    public AvailableDoctor() {
        // empty constructor for firebase database
    }

    public AvailableDoctor(String name, String email, String degree, String workplace, String expertise, String sex, String phone, String about) {
        this.name = name;
        this.email = email;
        this.degree = degree;
        this.workplace = workplace;
        this.expertise = expertise;
        this.sex = sex;
        this.phone = phone;
        this.about = about;
    }

    public String getName() {
        return name;
    }

    public String getDegree() {
        return degree;
    }

    public String getWorkplace() {
        return workplace;
    }

    public String getExpertise() {
        return expertise;
    }

    public String getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAbout() {
        return about;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
