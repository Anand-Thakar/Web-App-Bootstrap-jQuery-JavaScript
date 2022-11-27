package com.example.demo.controller;
import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.exception.BlogApiException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts/")
@Api(value = "CRUD Rest Api for Comment resources")
public class CommentController {

    public CommentRepository commentRepository;
    public PostRepository postRepository;
    private ModelMapper mapper;

    public CommentController(CommentRepository commentRepository, PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @ApiOperation(value  ="Create Comment Rest Api")
    @PostMapping("{postID}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postID") Long id, @Valid @RequestBody CommentDto commentDto) {
        Comment comment = dtoToEntity(commentDto);

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "for post id", " " + id));

        //set post to comment
        comment.setPost(post);
        //save comment
        Comment newComment = commentRepository.save(comment);
        return new ResponseEntity<>(entityToDto(newComment), HttpStatus.CREATED);
    }

    @ApiOperation(value  ="Get All Comments Rest Api")
    @GetMapping("{postID}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable("postID") Long id) {

        //getting all comments from comment repo by custom created method.
        List<Comment> comments = commentRepository.findByPostId(id);

        //getting Dto
        List<CommentDto> collect = comments.stream().map(comment -> entityToDto(comment)).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @ApiOperation(value  ="Get Comment By Id Rest Api")
    @GetMapping("{postID}/comments/{commentID}")
    public ResponseEntity<CommentDto> getCommentBYID(@PathVariable("postID") Long postID, @PathVariable("commentID") Long commentID) {

        //post by ID
        Post post = postRepository.findById(postID).orElseThrow(() -> new ResourceNotFoundException("Post", "for post id", " " + postID));

        //comment by ID
        Comment comment = commentRepository.findById(commentID).orElseThrow(() -> new ResourceNotFoundException("comment", "comment id", " " + commentID));

        //logic
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "This comment not belongs to the post Id you have provided");
        }
        return new ResponseEntity<>(entityToDto(comment), HttpStatus.OK);

    }

    @ApiOperation(value  ="Update Comment By Id Rest Api")
    @PutMapping("{postID}/comments/{commentID}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postID") Long postID, @PathVariable("commentID") Long commentID,
                                                    @Valid @RequestBody CommentDto commentDto) {
        //post by ID
        Post post = postRepository.findById(postID).orElseThrow(() -> new ResourceNotFoundException("Post", "for post id", " " + postID));
        //comment by ID
        Comment comment = commentRepository.findById(commentID).orElseThrow(() -> new ResourceNotFoundException("comment", "comment id", " " + commentID));
        //logic
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "This comment not belongs to the post Id you have provided");
        }

        //already found comment so lets just update its parameter that why no ID required here to be set
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        //saved updated comment
        Comment updatedComment = commentRepository.save(comment);

        // sending back responseEntity
        return new ResponseEntity<>(entityToDto(updatedComment), HttpStatus.OK);
    }

    @ApiOperation(value  ="Delete Comment By Id Rest Api")
    @DeleteMapping("{postID}/comments/{commentID}")
    public ResponseEntity<?> deleteComment(@PathVariable("postID") Long postID, @PathVariable("commentID") Long commentID) {
        //post by ID
        Post post = postRepository.findById(postID).orElseThrow(() -> new ResourceNotFoundException("Post", "for post id", " " + postID));
        //comment by ID
        Comment comment = commentRepository.findById(commentID).orElseThrow(() -> new ResourceNotFoundException("comment", "comment id", " " + commentID));
        //logic
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "This comment not belongs to the post Id you have provided");
        }
        // sending back responseEntity
        commentRepository.deleteById(commentID);
        return new ResponseEntity<>("Comment for id "+commentID+" deleted succesfully",HttpStatus.OK);
    }

    private Comment dtoToEntity(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
//        comment.setBody(commentDto.getBody());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setId(commentDto.getId());
        return comment;
    }

    private CommentDto entityToDto(Comment comment) {

        CommentDto commentDto = mapper.map(comment, CommentDto.class);
//        commentDto.setId(comment.getId());
//        commentDto.setBody(comment.getBody());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setName(comment.getName());

        return commentDto;
    }
}
