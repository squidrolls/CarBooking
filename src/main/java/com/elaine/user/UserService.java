package com.elaine.user;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserDao userDao; // should use UserDao, instead of UserFileDataAccessService

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User getUserById(UUID id) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
