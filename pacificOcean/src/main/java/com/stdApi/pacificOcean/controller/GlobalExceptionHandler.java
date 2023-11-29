package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.util.SlackNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private SlackNotifier slackNotifier;

    public GlobalExceptionHandler(SlackNotifier slackNotifier) {
        this.slackNotifier = slackNotifier;
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        slackNotifier.sendErrorNotification(e.getMessage());
    }
}
