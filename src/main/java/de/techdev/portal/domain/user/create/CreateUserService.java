package de.techdev.portal.domain.user.create;

import de.techdev.portal.domain.trackr.CreateEmployeeRequest;
import de.techdev.portal.domain.trackr.EmployeeAlreadyExistsException;
import de.techdev.portal.domain.trackr.TrackrRestException;
import de.techdev.portal.domain.trackr.TrackrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
class CreateUserService {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private TrackrService trackrService;

    /**
     * Create a new user and if wanted also a trackr employee.
     * @return The newly created Spring Security user
     * @throws UserExistsException If the user already exists in the portal
     * @throws EmployeeAlreadyExistsException If the employee already exists in trackr
     */
    User createNewUser(CreateUserRequest request) throws UserExistsException, EmployeeAlreadyExistsException {
        if (userDetailsManager.userExists(request.getEmail())) {
            throw new UserExistsException();
        }

        User user = new User(request.getEmail(), "", request.isEnabled(), true, true, true, request.getRoles());
        userDetailsManager.createUser(user);

        if (request.isWithTrackr()) {
            try {
                sendEmployeeToTrackr(request);
            } catch (TrackrRestException e) {
                // TODO some better error handling needed?
                userDetailsManager.deleteUser(request.getEmail());
                throw e;
            }
        }
        return user;
    }

    private void sendEmployeeToTrackr(CreateUserRequest request) {
        CreateEmployeeRequest employeeRequest =
                new CreateEmployeeRequest(request.getFirstName(), request.getLastName(), request.getFederalState(), request.getEmail());
        trackrService.createEmployee(employeeRequest);
    }
}
