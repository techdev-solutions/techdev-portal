package de.techdev.portal.domain.trackr;

public class CreateEmployeeRequest {

    private final String firstName;

    private final String lastName;

    private final String federalState;

    private final String email;

    public CreateEmployeeRequest(String firstName, String lastName, String federalState, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.federalState = federalState;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFederalState() {
        return federalState;
    }

    public String getEmail() {
        return email;
    }
}
