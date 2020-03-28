package com.szymon.ffproject.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GenericErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404-Error";
            }
            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "error/401-Error";
            }
            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error/400-Error";
            }
        }
        return "error/error";
    }

    @Override
    public String getErrorPath() {
        return "/error/error";
    }
}




