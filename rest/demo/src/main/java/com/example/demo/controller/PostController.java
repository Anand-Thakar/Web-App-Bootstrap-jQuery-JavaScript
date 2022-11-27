package com.example.demo.controller;

import com.example.demo.dto.PostDto;
import com.example.demo.dto.PostResponse;
import com.example.demo.entity.Post;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PostRepository;
import com.example.demo.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Api(value = "CRUD Rest Api for Post resources")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private PostRepository postRepository;
    private ModelMapper mapper;

    public PostController(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    //creating blog post
    @ApiOperation(value = "Create Post Rest Api")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        //getting the customer data dto, then convert it into post and saved in repository.
        Post post = dtoToEntity(postDto);
        Post newPost = postRepository.save(post);

        //convert this new entity to dto
        PostDto responsePostDto = entityToDto(newPost);

        return new ResponseEntity<>(responsePostDto, HttpStatus.CREATED);

    }

    @ApiOperation(value = "Get All Post Rest Api")
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts
            (@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
             @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
             @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
             @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        //get content from page object
        List<Post> postList = posts.getContent();

        //dto created to send as list contains
        List<PostDto> content = postList.stream().map(post -> entityToDto(post)).collect(Collectors.toList());

        //providing pageing sorting data -09
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);

        //getting everything from pageable
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        postResponse.setTotalElements(posts.getTotalElements());

        return ResponseEntity.ok(postResponse);
    }

    @ApiOperation(value = "Get Post by ID Rest Api")
    @GetMapping( "/{id}")
    public ResponseEntity<PostDto> getPostByID(@PathVariable("id") Long id) {

        Optional<Post> byId = postRepository.findById(id);
        if (byId.isPresent()) {
            Post post = byId.get();
            PostDto postDto = entityToDto(post);
            return ResponseEntity.ok(postDto);
        }

        throw new ResourceNotFoundException("employee", "id", "" + id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @ApiOperation(value = "Update Post by ID Rest Api")
    public ResponseEntity<PostDto> updatingPost(@PathVariable("id") Long id, @Valid @RequestBody PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "updating", "number " + id));

        //updated repo.
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        Post updatedPost = postRepository.save(post);

        //convert post
        PostDto dto = entityToDto(updatedPost);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Post by ID Rest Api")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("delete", "post", "" + id));
        postRepository.deleteById(id);
        return ResponseEntity.ok("Post deleted successfully");
    }

    private PostDto entityToDto(Post post) {
        PostDto postDto = mapper.map(post, PostDto.class);

//        postDto.setId(post.getId());
//        postDto.setContent(post.getContent());
//        postDto.setDescription(post.getDescription());
//        postDto.setTitle(post.getTitle());

        return postDto;
    }

    private Post dtoToEntity(PostDto postDto) {
        Post post = mapper.map(postDto, Post.class);
//        post.setId(postDto.getId());
//        post.setContent(postDto.getContent());
//        post.setDescription(postDto.getDescription());
//        post.setTitle(postDto.getTitle());

        return post;
    }
}
