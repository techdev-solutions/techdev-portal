package de.techdev.portal.domain.trackr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
class TrackrOauthService implements TrackrService {

    @Value("${trackr.apiUrl}")
    private String trackrApiUrl;

    @Autowired
    private OAuth2RestTemplate trackrTemplate;

    @Override
    public CreateEmployeeAnswer createEmployee(CreateEmployeeRequest request) throws EmployeeAlreadyExistsException {
        HttpEntity<CreateEmployeeRequest> restRequest = new HttpEntity<>(request);
        ResponseEntity<CreateEmployeeAnswer> response;
        try {
            response = trackrTemplate
                    .exchange(trackrApiUrl + "/employees", HttpMethod.POST, restRequest, CreateEmployeeAnswer.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new EmployeeAlreadyExistsException();
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new TrackrRestException("Invalid data while creating an employee " + e.getResponseBodyAsString(), e);
            } else {
                throw new TrackrRestException("Unknown error while creating employee", e);
            }
        }
        return response.getBody();
    }

}
