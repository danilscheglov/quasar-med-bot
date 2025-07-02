package dev.danilscheglov.quasarmedbot.model;

import java.time.LocalDate;

public class UserData {

    private String name;
    private LocalDate birthdate;

    public UserData(String name, LocalDate birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }
}
