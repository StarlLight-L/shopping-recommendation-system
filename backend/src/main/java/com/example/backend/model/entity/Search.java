package com.example.backend.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "user_search_history")
@Data
@ApiModel("Search")
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private int id;

    @ApiModelProperty(value = "UserID")
    private int user_id;

    @ApiModelProperty(value = "Search Content")
    private String keyword;

    @ApiModelProperty(value = "Search Time")
    private LocalDateTime searched_at;
}
