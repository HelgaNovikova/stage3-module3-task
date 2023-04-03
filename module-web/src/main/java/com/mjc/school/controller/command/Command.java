package com.mjc.school.controller.command;

public interface Command {
    <R> R execute();
}
