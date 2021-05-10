package cse323.nsu.patienttracking.models;

public class User {

    private String email;
    private String password;
    private String type;

    public User() {
        // empty constructor for firebase
    }

    public String getType() {
        return type;
    }
}
