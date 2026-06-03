package com.example.backend.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("SearchDTO")
public class SearchDTO {

    @NotBlank(message = "UserID")
    private int user_id;

    @NotBlank(message = "Search Content")
    private String keyword;

}
