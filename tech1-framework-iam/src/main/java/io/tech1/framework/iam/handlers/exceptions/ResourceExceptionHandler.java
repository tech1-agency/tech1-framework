package io.tech1.framework.iam.handlers.exceptions;

import io.tech1.framework.foundation.domain.exceptions.ExceptionEntity;
import io.tech1.framework.foundation.domain.exceptions.ExceptionEntityType;
import io.tech1.framework.foundation.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.foundation.domain.exceptions.cookies.CookieNotFoundException;
import io.tech1.framework.foundation.domain.exceptions.tokens.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;
import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.unexpectedErrorOccurred;
import static java.util.Objects.isNull;

// WARNING: @Order by default uses Ordered.LOWEST_PRECEDENCE
@Slf4j
@Order
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceExceptionHandler {

    // =================================================================================================================
    // DEDICATED EXCEPTIONS
    // =================================================================================================================

    @ExceptionHandler({
            RegistrationException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionEntity> registerException(RegistrationException ex) {
        var response = new ExceptionEntity(
                ExceptionEntityType.ERROR,
                contactDevelopmentTeam("Registration Failure"),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionEntity> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new ExceptionEntity(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // =================================================================================================================
    // GROUPED EXCEPTIONS
    // =================================================================================================================

    @ExceptionHandler({
            CookieNotFoundException.class,
            AccessTokenNotFoundException.class,
            AccessTokenInvalidException.class,
            AccessTokenExpiredException.class,
            AccessTokenDbNotFoundException.class,
            RefreshTokenNotFoundException.class,
            RefreshTokenInvalidException.class,
            RefreshTokenExpiredException.class,
            RefreshTokenDbNotFoundException.class,
            TokenUnauthorizedException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionEntity> unauthorizedExceptions(Exception ex) {
        return new ResponseEntity<>(new ExceptionEntity(ex), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionEntity> forbiddenExceptions(Exception ex) {
        return new ResponseEntity<>(new ExceptionEntity(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            HttpMessageConversionException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionEntity> badRequestExceptions(Exception ex) {
        var response = new ExceptionEntity(
                ExceptionEntityType.ERROR,
                contactDevelopmentTeam("Malformed request syntax"),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionEntity> internalServerError(Exception ex) {
        return new ResponseEntity<>(new ExceptionEntity(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            Exception.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionEntity> generalException(Exception ex) {
        if (isNull(ex) || isNull(ex.getMessage())) {
            var response = new ExceptionEntity(
                    ExceptionEntityType.ERROR,
                    unexpectedErrorOccurred(),
                    unexpectedErrorOccurred()
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            LOGGER.error("Unexpected error occurred", ex);
            var response = new ExceptionEntity(
                    ExceptionEntityType.ERROR,
                    unexpectedErrorOccurred(),
                    ex.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
