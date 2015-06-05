package de.techdev.portal.domain.user.create;

import de.techdev.portal.domain.trackr.CreateEmployeeRequest;
import de.techdev.portal.domain.trackr.EmployeeAlreadyExistsException;
import de.techdev.portal.domain.trackr.TrackrRestException;
import de.techdev.portal.domain.trackr.TrackrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
class CreateUserService {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserService.class);

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
            } catch (EmployeeAlreadyExistsException e) {
                logger.info("Employee {} already existed but keeping newly created user.", request.getEmail());
                throw e;
            } catch (TrackrRestException e) {
                logger.warn("Other error while creating a trackr employee, deleting user {}", request.getEmail());
                userDetailsManager.deleteUser(request.getEmail());
                throw e;
            }
        }
        logger.info("User {} created.", request.getEmail());
        return user;
    }

    private void sendEmployeeToTrackr(CreateUserRequest request) {
        CreateEmployeeRequest employeeRequest =
                new CreateEmployeeRequest(request.getFirstName(), request.getLastName(), request.getFederalState(), request.getEmail());
        trackrService.createEmployee(employeeRequest);
    }
}
