package com.example.backend.controller;

import com.example.backend.model.entity.Address;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Api(tags = "User management API")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation("Create User")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.insert(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update User Info")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
        user.setId(id);
        user.setUpdateTime(LocalDateTime.now());
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete User")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @ApiOperation("Fetch User Info")
    public ResponseEntity<User> getUser(@PathVariable("id")  int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @ApiOperation("Fetch All User")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/avatar")
    @ApiOperation("update User Avatar")
    public ResponseEntity<Map<String, String>> updateAvatar(
            @PathVariable int id,
            @RequestParam("user_avatar") MultipartFile user_avatar) {
            String avatarUrl = userService.updateAvatar(id, user_avatar);
            Map<String, String> response = new HashMap<>();
            response.put("user_avatar", avatarUrl);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/address/list/{id}")
    @ApiOperation("Fetch User Address")
    public ResponseEntity<List<Address>> getUserAddress(@PathVariable("id") int id){
        List<Address> userAddress = userService.findAllAddress(id);
        return ResponseEntity.ok(userAddress);
    }

    @PostMapping("/address/insert/")
    @ApiOperation("Save User Address")
    public ResponseEntity<Address> insertUserAddress(
            @RequestBody Address address
    ){
        Address userAddress = userService.insertUserAddress(address);
        return ResponseEntity.ok(userAddress);
    }

    @DeleteMapping("/address/delete/{addressId}")
    @ApiOperation("Delete Address")
    public ResponseEntity<Boolean> deleteUserAddress(
            @PathVariable("addressId") int addressId
    ){
        boolean success = userService.deleteUserAddress(addressId);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/address/default/{addressId}")
    @ApiOperation("Set Default Address")
    public ResponseEntity<Boolean> setDefaultAddress(
            @PathVariable("addressId") int addressId
    ){
        boolean success = userService.setDefaultAddress(addressId);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/address/update/")
    @ApiOperation("Update user address")
    public ResponseEntity<Address> updateUserAddress(
            @RequestBody Address address
    ){
        Address userAddress = userService.updateUserAddress(address);
        return ResponseEntity.ok(userAddress);
    }
}