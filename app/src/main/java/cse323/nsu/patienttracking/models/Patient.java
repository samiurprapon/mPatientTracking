package cse323.nsu.patienttracking.models;

public class Patient {
    private int age;
    private String blood;
    private String email;
    private String location;
    private String name;
    private String phone;
    private String sex;

    public Patient() {
        // empty constructor for firebase
    }

    public Patient(int age, String blood, String email, String location, String name, String phone, String sex) {
        this.age = age;
        this.blood = blood;
        this.email = email;
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public String getBlood() {
        return blood;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSex() {
        return sex;
    }

}
