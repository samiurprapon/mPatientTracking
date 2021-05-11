package cse323.nsu.patienttracking.models;

public class AvailableDoctor {
    private String uid;
    private String name;
    private String degree;
    private String workplace;
    private String expertise;
    private String sex;

    public AvailableDoctor() {
        // empty constructor for firebase database
    }

    public AvailableDoctor(String name, String degree, String workplace, String expertise, String sex) {
        this.name = name;
        this.degree = degree;
        this.workplace = workplace;
        this.expertise = expertise;
        this.sex = sex;
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

    public void setUid(String uid) {
        this.uid = uid;
    }
}
