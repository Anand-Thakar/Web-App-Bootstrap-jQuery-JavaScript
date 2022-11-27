package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Set;
@ApiModel(description = "Post model information")
@Data
public class PostDto {
@ApiModelProperty(value = "Blog Post Id")
    private long id;

    @NotEmpty
    @Size(min = 2, message = "post title should have at least two characters")
    @ApiModelProperty(value = "Blog Post title")
    private String title;
    @NotEmpty
    @Size(min = 10, message = "post description should have at least ten characters")
    @ApiModelProperty(value = "Blog Post description")
    private String description;
    @NotEmpty
    @ApiModelProperty(value = "Blog Post content")
    private String content;
    @ApiModelProperty(value = "Blog Post comments")
    private Set<CommentDto> comments;

}
