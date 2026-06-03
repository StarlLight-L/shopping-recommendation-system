package com.example.backend.service;

import com.example.backend.model.entity.Address;
import com.example.backend.model.entity.User;
import com.example.backend.model.dto.UserLoginDTO;
import com.example.backend.model.dto.UserRegisterDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User register(UserRegisterDTO registerDTO);
    User login(UserLoginDTO loginDTO);
    User getUserById(int id);
    User updateUser(User user);
    User getUserByEmail(String email);
    void deleteUser(int id);
    User getCurrentUser();
    User insert(User user);
    User insertGoogle(User user);
    List<User> findAll();
    String updateAvatar(int id, MultipartFile avatarFile);
    List<Address> findAllAddress(int id);
    Address insertUserAddress(Address address);
    boolean deleteUserAddress(int addressId);

    boolean setDefaultAddress(int addressId);

    Address updateUserAddress(Address address);
}