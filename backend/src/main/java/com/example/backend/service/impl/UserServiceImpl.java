package com.example.backend.service.impl;

import com.example.backend.model.entity.Address;
import com.example.backend.util.FileStorageService;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entity.User;
import com.example.backend.model.dto.UserLoginDTO;
import com.example.backend.model.dto.UserRegisterDTO;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public User register(UserRegisterDTO registerDTO) {

        User existingUser = userMapper.findByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUser_name(registerDTO.getUsername());
        user.setUser_password(passwordEncoder.encode(registerDTO.getPassword()));
        user.setUser_email(registerDTO.getEmail());
        
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(UserLoginDTO loginDTO) {
        User user = userMapper.findByUseremail(loginDTO.getEmail());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getUser_password())) {
            throw new RuntimeException("Incorrect email or password");
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        return userMapper.findById(id);
    }

    @Override
    public User updateUser(User user) {
        userMapper.update(user);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        userMapper.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        // TODO: 实现获取当前登录用户的逻辑
        return null;
    }

    @Override
    public User insert(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public User insertGoogle(User user) {
        userMapper.insertGoogle(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public String updateAvatar(int id, MultipartFile avatarFile) {
        // Find user
        User user = userMapper.findById(id);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Validate file type
        if (!avatarFile.getContentType().startsWith("image/")) {
            throw new RuntimeException("Uploaded file must be an image");
        }

        // Validate file size (5 MB limit)
        if (avatarFile.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size cannot exceed 5 MB");
        }

        // Upload avatar and return the avatar URL
        try {
            // Call FileStorageService
            String avatarUrl = fileStorageService.storeFile(avatarFile);

            // Update avatar only
            user.setUser_avatar(avatarUrl);
            userMapper.updateAvatar(id, avatarUrl);

            return avatarUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar: " + e.getMessage(), e);
        }
    }

    // Find user addresses
    @Override
    public List<Address> findAllAddress(int id) {
        return userMapper.findAllAddress(id);
    }

    // Save user address
    @Override
    public Address insertUserAddress(Address address) {
        userMapper.insertUserAddress(address);
        return address;
    }

    // Delete user address
    @Override
    public boolean deleteUserAddress(int addressId) {
        userMapper.deleteUserAddress(addressId);
        return true;
    }

    @Override
    public boolean setDefaultAddress(int addressId) {
        userMapper.setDefaultAddress(addressId);
        return false;
    }

    @Override
    public Address updateUserAddress(Address address) {
        userMapper.updateUserAddress(address);
        return address;
    }

    @Override
    public User getUserByEmail(String email){
        return userMapper.findByUseremail(email);
    }
}