package com.elaine.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.UUID;

public class UserFileDataAccessService implements UserDao{
    @Override
    public User[] getUsers() {
        /*
            Size 4 because I know there are 4 entries in src/users.csv
            If you add more rows in the file update the size of the initial array too
        */
        User[] users = new User[4];
        File file = new File("src/com/elaine/users.csv");
        try {
            int index = 0;
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()){
                String[] input = scanner.nextLine().trim().split(",");
                UUID id = UUID.fromString(input[0]);
                String name = input[1];
                users[index] = new User(id, name);
                index++;
            }
            return users;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
