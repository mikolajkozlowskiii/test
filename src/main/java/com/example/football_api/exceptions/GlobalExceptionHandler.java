package com.example.football_api.exceptions;

import com.example.football_api.exceptions.football.DuplicateLeagueException;
import com.example.football_api.exceptions.football.LeagueNotFoundException;
import com.example.football_api.exceptions.users.EmailConfirmationException;
import com.example.football_api.exceptions.users.JwtTokenException;
import com.example.football_api.exceptions.users.RoleNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseStatusException handleResourceConflictException(RuntimeException ex) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
    }

    @ResponseBody
    @ExceptionHandler({UsernameNotFoundException.class,
                       RoleNotFoundException.class,
                        LeagueNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseStatusException handleUsernameNotFoundException(RuntimeException ex) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    @ResponseBody
    @ExceptionHandler({AppException.class,
                       IllegalStateException.class,
                       EmailConfirmationException.class,
                       DuplicateLeagueException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseStatusException handleAppException(RuntimeException ex) {
        return new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class,
                        ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ResponseBody
    @ExceptionHandler({JwtTokenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseStatusException handleForbidden(RuntimeException ex) {
        return new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }
}



