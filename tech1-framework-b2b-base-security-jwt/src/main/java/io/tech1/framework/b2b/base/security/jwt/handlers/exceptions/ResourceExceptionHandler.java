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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceExceptionHandler {

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
    public ResponseEntity<ExceptionEntity> cookiesUnauthorizedExceptions(Exception ex) {
        var response = ExceptionEntity.of(ex);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<ExceptionEntity> accessDeniedException(AccessDeniedException ex) {
        var response = ExceptionEntity.of(ex);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            RegistrationException.class
    })
    public ResponseEntity<ExceptionEntity> registerException(RegistrationException ex) {
        var response = ExceptionEntity.of(
                ExceptionEntityType.ERROR,
                contactDevelopmentTeam("Registration Failure"),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<ExceptionEntity> internalServerError(IllegalArgumentException ex) {
        var response = ExceptionEntity.of(ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
