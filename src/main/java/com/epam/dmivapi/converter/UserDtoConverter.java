package com.epam.dmivapi.converter;

import com.epam.dmivapi.dto.*;
import com.epam.dmivapi.model.Loan;
import com.epam.dmivapi.model.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserDtoConverter extends AbstractConverter<User, UserDtoList> {
    public UserDto convert(User user) {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("Can't convert user to userDto, user is  null");
        }

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setLocaleName(user.getLocaleName());
        userDto.setUserRole(user.getUserRole());
        userDto.setBlocked(user.isBlocked());
 
        return userDto;
    }

    public User convertDtoCreate(UserDtoCreate dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Program DTO cannot be null");
        }

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setLocaleName(dto.getLocaleName());
        user.setUserRole(dto.getUserRole());

        return user;
    }

    @Override
    public UserDtoList convertForList(User user) {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("Can't convert user to userDtoList, user is  null");
        }

        UserDtoList userDtoList = new UserDtoList();

        userDtoList.setId(user.getId());
        userDtoList.setEmail(user.getEmail());
        userDtoList.setFirstName(user.getFirstName());
        userDtoList.setLastName(user.getLastName());
        userDtoList.setLocaleName(user.getLocaleName());
        userDtoList.setBlocked(user.isBlocked());

        return userDtoList;
    }

}
