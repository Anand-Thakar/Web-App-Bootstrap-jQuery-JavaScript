package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "Comment Model information")
public class CommentDto {
    @ApiModelProperty(value = "Comment Id")
    private long id;
    @NotEmpty(message = "name shouldn't be empty or null")
    @ApiModelProperty(value = "Comment name")
    private String name;
    @NotEmpty(message = "name shouldn't be empty or null")
    @Email
    @ApiModelProperty(value = "Comment email")
    private String email;
    @NotEmpty
    @Size(min = 10,message = "Comment must have at least add 10 words inside body.")
    @ApiModelProperty(value = "Comment body")
    private String body;
}
