package com.epam.dmivapi.model;

import com.epam.dmivapi.service.impl.command.Command;

public class User extends Entity {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String localeName;
    private Role userRole;
    private boolean isBlocked;

    public enum Role {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocaleName() { return localeName; }

    public void setLocaleName(String localeName) { this.localeName = localeName; }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
