package de.techdev.portal.domain.trackr;

public interface TrackrService {

    CreateEmployeeAnswer createEmployee(CreateEmployeeRequest request) throws EmployeeAlreadyExistsException;

}
