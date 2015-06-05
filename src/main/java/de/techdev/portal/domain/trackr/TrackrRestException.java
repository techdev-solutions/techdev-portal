package de.techdev.portal.domain.trackr;

/**
 * Generic exception used when an error occurs while accessing trackr via REST.
 */
public class TrackrRestException extends RuntimeException {

    public TrackrRestException() {}

    public TrackrRestException(String message, Throwable e) {
        super(message, e);
    }
}
