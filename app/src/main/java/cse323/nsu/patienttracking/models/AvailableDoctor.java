package cse323.nsu.patienttracking.models;

public class AvailableDoctor {
    private String name;
    private String degree;
    private String workAt;
    private String expertise;
    private String sex;

    public AvailableDoctor() {
        // empty constructor for firebase database
    }

    public AvailableDoctor(String name, String degree, String workAt, String expertise, String sex) {
        this.name = name;
        this.degree = degree;
        this.workAt = workAt;
        this.expertise = expertise;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public String getDegree() {
        return degree;
    }

    public String getWorkAt() {
        return workAt;
    }

    public String getExpertise() {
        return expertise;
    }

    public String getSex() {
        return sex;
    }
}
