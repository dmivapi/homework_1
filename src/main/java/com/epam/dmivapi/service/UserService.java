package com.epam.dmivapi.service;

import com.epam.dmivapi.dto.Role;
import com.epam.dmivapi.dto.UserDtoList;

import java.util.List;

public interface UserService {
    List<UserDtoList> getUsersByRole(Role role, int currentPage, int recordsPerPage);

    int countUsersPagesByRole(Role role, int recordsPerPage);
}
