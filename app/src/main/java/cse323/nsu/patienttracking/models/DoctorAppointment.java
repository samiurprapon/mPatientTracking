package cse323.nsu.patienttracking.models;

public class DoctorAppointment {
    private String id;
    private String doctorName;
    private String doctorSex;
    private String status;
    private String type;
    private String date;
    private String message;
    private String patientName;
    private String patientSex;
    private String patientLocation;

    private String doctorUid;
    private String patientUid;

    public DoctorAppointment() {
        // empty constructor for firebase
    }

    public DoctorAppointment(String doctorName, String doctorSex, String doctorUid, String status, String type, String date, String message, String patientName, String patientSex, String patientLocation, String patientUid) {
        this.doctorName = doctorName;
        this.doctorUid = doctorUid;
        this.patientUid = patientUid;
        this.doctorSex = doctorSex;
        this.status = status;
        this.patientName = patientName;
        this.patientSex = patientSex;
        this.patientLocation = patientLocation;
        this.message = message;
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

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public String getPatientUid() {
        return patientUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public String getPatientLocation() {
        return patientLocation;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
