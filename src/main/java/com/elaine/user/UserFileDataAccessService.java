package com.elaine.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class UserFileDataAccessService implements UserDao{
    @Override
    public List<User> getUsers() {

        File file = new File(getClass().getClassLoader().getResource("users.csv").getPath());
        List<User> users = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()){
                String[] input = scanner.nextLine().trim().split(",");
                users.add(new User(UUID.fromString(input[0]), input[1]));
            }
            return users;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
