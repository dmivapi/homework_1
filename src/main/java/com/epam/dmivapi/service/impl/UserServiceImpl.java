package com.epam.dmivapi.service.impl;

import com.epam.dmivapi.converter.UserDtoConverter;
import com.epam.dmivapi.dto.Role;
import com.epam.dmivapi.dto.UserDtoList;
import com.epam.dmivapi.model.User;
import com.epam.dmivapi.repository.UserRepository;
import com.epam.dmivapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDtoConverter userDtoConverter) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
    }

    @Override
    public List<UserDtoList> getUsersByRole(Role role, int currentPage, int recordsPerPage) {
        if (recordsPerPage == 0) {
            throw new IllegalArgumentException("The number of records per page can not be 0");
        }
        List<User> users = userRepository.findUsersByRole(role, currentPage, recordsPerPage);
        return userDtoConverter.convert(users);
    }

    @Override
    public int countUsersPagesByRole(Role role, int recordsPerPage) {
        if (recordsPerPage == 0) {
            throw new IllegalArgumentException("The number of records per page can not be 0");
        }
        return ServiceUtils.calculateNumOfPages(
                userRepository.countUsersByRole(role),
                recordsPerPage
        );
    }
}
