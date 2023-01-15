package me.tretyakovv.p3_lesson8.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ExceptionService extends RuntimeException{

    public ExceptionService(String message) {
        super(message);
    }

}
