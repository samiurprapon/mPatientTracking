package cse323.nsu.patienttracking.utils;

public class Constants {

    public static String[] BLOOD_GROUPS = {
            " ",
            "A+(ve)", "B+(ve)", "O+(ve)", "AB+(ve)",
            "A-(ne)", "B-(ne)", "O-(ne)", "AB-(ne)"
    };

    public static String[] APPOINTMENT_TYPE = {
            "consultation",
            "hospitalization",
            "Observation"
    };

    public static int getPosition(String group) {

        int result = 0;

        for (int i = 0; i < BLOOD_GROUPS.length; i++) {
            if(BLOOD_GROUPS[i].equals(group)) {
                result = i;
                break;
            }
        }

        return result;
    }
}