package dev.danilscheglov.quasarmedbot.model;

import java.time.LocalDate;

public class UserData {
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthdate;
    private String pressure;

    public UserData(String lastName, String firstName, String middleName, LocalDate birthdate) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthdate = birthdate;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
}