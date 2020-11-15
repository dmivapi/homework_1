package com.epam.dmivapi.dto;

import com.epam.dmivapi.service.impl.command.Command;

import java.io.Serializable;

public enum Role implements Serializable {
    ADMIN(Command.LIST_USERS_READERS_FOR_ADMIN.getPath()),
    LIBRARIAN(Command.LIST_USERS_READERS_FOR_LIBRARIAN.getPath()),
    READER(Command.LIST_BOOKS.getPath()),
    GUEST(Command.LIST_BOOKS.getPath());

    private String defaultPage;

    private Role(String defaultCommand) {
        this.defaultPage = defaultCommand;
    }

    public String getDefaultPage() {
        return defaultPage;
    }
}
