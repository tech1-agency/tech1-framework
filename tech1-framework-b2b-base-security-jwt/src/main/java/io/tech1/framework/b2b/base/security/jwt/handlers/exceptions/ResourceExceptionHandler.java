package io.tech1.framework.b2b.base.security.jwt.handlers.exceptions;

import io.tech1.framework.domain.exceptions.ExceptionEntity;
import io.tech1.framework.domain.exceptions.ExceptionEntityType;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.domain.exceptions.cookies.CookieNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceExceptionHandler {

    // =================================================================================================================
    // DEDICATED EXCEPTIONS
    // =================================================================================================================

    @ExceptionHandler({
            RegistrationException.class
    })
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
    public ResponseEntity<ExceptionEntity> unauthorizedExceptions(Exception ex) {
        return new ResponseEntity<>(new ExceptionEntity(ex), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<ExceptionEntity> forbiddenExceptions(AccessDeniedException ex) {
        return new ResponseEntity<>(new ExceptionEntity(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<ExceptionEntity> internalServerError(Exception ex) {
        return new ResponseEntity<>(new ExceptionEntity(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
