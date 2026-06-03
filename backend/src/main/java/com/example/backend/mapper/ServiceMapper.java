package com.example.backend.mapper;

import com.example.backend.model.dto.SearchDTO;
import com.example.backend.model.entity.History;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceMapper {

    @Insert("INSERT INTO user_search_history (user_id,keyword,searched_at) " +
            "VALUES (#{user_id}, #{keyword}, NOW())")
    boolean addSearch(SearchDTO searchDTO);

    @Delete("DELETE FROM user_search_history WHERE user_id = #{userId} AND id NOT IN (" +
            "SELECT id FROM (" +
            "SELECT id FROM user_search_history WHERE user_id = #{userId} ORDER BY searched_at DESC LIMIT 20" +
            ") AS temp)")
    void deleteOldSearchHistory(Integer user_id);

    @Insert("INSERT INTO read_history (user_id,product_id,time) " +
            "VALUES (#{user_id}, #{product_id}, NOW())")
    boolean addHistory(History history);

    @Delete("DELETE FROM read_history WHERE user_id = #{user_id} AND id NOT IN (" +
            "SELECT id FROM (" +
            "SELECT id FROM read_history WHERE user_id = #{user_id} ORDER BY time DESC LIMIT 100" +
            ") AS temp)")
    void deleteOldHistory(Integer user_id);
}
