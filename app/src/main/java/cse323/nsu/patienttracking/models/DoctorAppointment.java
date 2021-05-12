package cse323.nsu.patienttracking.models;

public class DoctorAppointment {
    private String id;
    private String doctorName;
    private String doctorSex;
    private String status;
    private String type;
    private String date;

    public DoctorAppointment() {
        // empty constructor for firebase
    }

    public DoctorAppointment(String doctorName, String doctorSex, String status, String type, String date) {
        this.doctorName = doctorName;
        this.doctorSex = doctorSex;
        this.status = status;
        this.type = type;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorSex() {
        return doctorSex;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }
}
