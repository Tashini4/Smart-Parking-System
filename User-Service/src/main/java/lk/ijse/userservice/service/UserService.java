package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.UserDTO;

import java.util.List;

public interface UserService {
    int saveUser(UserDTO user);

    int updateUser(UserDTO user);

    int deleteUser(String email , String password);

    List<UserDTO> getAllUsers();

    UserDTO getUserByEmail(String email);

}
