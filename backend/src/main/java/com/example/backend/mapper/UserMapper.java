package com.example.backend.mapper;

import com.example.backend.model.entity.Address;
import com.example.backend.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    // Insert New User
    @Insert("INSERT INTO users (user_name, user_email, user_password,  create_time, update_time) " +
            "VALUES (#{user_name}, #{user_email}, #{user_password}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    // Find User by ID
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(int id);

    // Find user by username
    @Select("SELECT * FROM users WHERE user_name = #{user_name}")
    User findByUsername(String user_name);

    // update user info
    @Update("UPDATE users SET " +
            "user_name = #{user_name}, " +
            "user_email = #{user_email}, " +
            "user_phone = #{user_phone}, " +
            "user_birthday = #{user_birthday}, " +
            "user_gender = #{user_gender}, " +
            "update_time = NOW() " +
            "WHERE id = #{id}")
    int update(User user);

    // Update user password
    @Update("UPDATE users SET user_password = #{newPassword}, update_time = NOW() " +
            "WHERE id = #{id} AND user_password = #{oldPassword}")
    int updatePassword(@Param("id") int id,
                       @Param("oldPassword") String oldPassword,
                       @Param("newPassword") String newPassword);

    // delete user
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(int id);

    //  Get users with pagination
    @Select("SELECT * FROM users LIMIT #{offset}, #{limit}")
    List<User> findAllWithPagination(@Param("offset") int offset, @Param("limit") int limit);

    // check if user exist
    @Select("SELECT COUNT(*) FROM users WHERE user_name = #{user_name}")
    int checkUsernameExists(String user_name);

    // check if email exist
    @Select("SELECT COUNT(*) FROM users WHERE user_email = #{user_email}")
    int checkEmailExists(String user_email);

    // Find User
    @Select("SELECT * FROM users")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE user_email = #{user_email}")
    User findByUseremail(String useremail);

    // Update user Avatar
    @Update("UPDATE users SET user_avatar = #{avatarUrl} WHERE id = #{id}")
    void updateAvatar(@Param("id") int id, @Param("avatarUrl") String avatarUrl);

    //Fetch User Address
    @Select("SELECT * FROM user_address WHERE user_id = #{id}")
    List<Address> findAllAddress(int id);

    //Save Address
    @Insert("INSERT INTO user_address(user_id,consignee,phone,province,city,district,detail,is_default)"+
            "VALUES (#{user_id}, #{consignee}, #{phone}, #{province}, #{city}, #{district}, #{detail}, #{is_default})")
    void insertUserAddress(Address address);

    //Delete Address
    @Delete("DELETE FROM user_address WHERE id = #{addressId}")
    void deleteUserAddress(int addressId);

    @Update("UPDATE user_address SET is_default = true WHERE id = #{addressId}")
    void setDefaultAddress(int addressId);

    @Update("UPDATE user_address SET" +
            " user_id = #{user_id}," +
            " consignee = #{consignee}," +
            " phone = #{phone}," +
            " province = #{province}," +
            " city = #{city}," +
            " district = #{district}," +
            " detail = #{detail}," +
            " is_default = #{is_default} " +
            "WHERE id = #{id}")
    void updateUserAddress(Address address);

    @Insert("INSERT INTO users (user_name, user_email, user_password, user_avatar, create_time, update_time) " +
            "VALUES (#{user_name}, #{user_email}, #{user_password}, #{user_avatar}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertGoogle(User user);
}