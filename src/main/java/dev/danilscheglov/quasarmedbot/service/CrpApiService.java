package dev.danilscheglov.quasarmedbot.service;

import org.springframework.stereotype.Service;

@Service
public class CrpApiService {

    public String searchByFioAndBirthdate(String lastName, String firstName, String middleName, String birthdate) {
        return "test-id-123";
    }
}
