package com.gitfactory.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitfactory.blogapi.config.JpaAuditingConfig;
import com.gitfactory.blogapi.dto.PostRequest;
import com.gitfactory.blogapi.dto.PostResponse;
import com.gitfactory.blogapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(
        controllers = PostController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JpaAuditingConfig.class
        )
)
@DisplayName("PostController 통합 테스트")
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    private PostResponse postResponse;
    private PostRequest postRequest;

    @BeforeEach
    void setUp() {
        // Record 타입이므로 생성자 사용
        postRequest = new PostRequest(
                "테스트 제목",
                "테스트 내용",
                "테스트 작성자"
        );

        postResponse = new PostResponse(
                1L,
                "테스트 제목",
                "테스트 내용",
                "테스트 작성자",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("GET /api/posts - 전체 포스트 조회")
    void getAllPosts() throws Exception {
        // Given
        List<PostResponse> posts = Arrays.asList(postResponse);
        given(postService.getAllPosts()).willReturn(posts);

        // When & Then
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("테스트 제목")))
                .andExpect(jsonPath("$[0].content", is("테스트 내용")));
    }

    @Test
    @DisplayName("GET /api/posts/{id} - ID로 포스트 조회 성공")
    void getPostById_Success() throws Exception {
        // Given
        given(postService.getPostById(1L)).willReturn(postResponse);

        // When & Then
        mockMvc.perform(get("/api/posts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("테스트 제목")))
                .andExpect(jsonPath("$.content", is("테스트 내용")));
    }

    @Test
    @DisplayName("GET /api/posts/{id} - 존재하지 않는 ID로 조회 실패")
    void getPostById_NotFound() throws Exception {
        // Given
        given(postService.getPostById(999L))
                .willThrow(new RuntimeException("Post not found with id: 999"));

        // When & Then
        mockMvc.perform(get("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Post not found with id: 999"));
    }

    @Test
    @DisplayName("POST /api/posts - 포스트 생성 성공")
    void createPost() throws Exception {
        // Given
        given(postService.createPost(any(PostRequest.class))).willReturn(postResponse);

        // When & Then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("테스트 제목")))
                .andExpect(jsonPath("$.content", is("테스트 내용")));
    }

    @Test
    @DisplayName("PUT /api/posts/{id} - 포스트 수정 성공")
    void updatePost() throws Exception {
        // Given - Record 타입 생성자 사용
        PostRequest updateRequest = new PostRequest(
                "수정된 제목",
                "수정된 내용",
                "테스트 작성자"
        );

        PostResponse updateResponse = new PostResponse(
                1L,
                "수정된 제목",
                "수정된 내용",
                "테스트 작성자",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(postService.updatePost(eq(1L), any(PostRequest.class)))
                .willReturn(updateResponse);

        // When & Then
        mockMvc.perform(put("/api/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("수정된 제목")))
                .andExpect(jsonPath("$.content", is("수정된 내용")));
    }

    @Test
    @DisplayName("DELETE /api/posts/{id} - 포스트 삭제 성공")
    void deletePost() throws Exception {
        // Given
        doNothing().when(postService).deletePost(1L);

        // When & Then
        mockMvc.perform(delete("/api/posts/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/posts/search - 제목으로 검색")
    void searchByTitle() throws Exception {
        // Given
        List<PostResponse> posts = Arrays.asList(postResponse);
        given(postService.searchByTitle("테스트")).willReturn(posts);

        // When & Then
        mockMvc.perform(get("/api/posts/search")
                        .param("keyword", "테스트"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("테스트 제목")));
    }

    @Test
    @DisplayName("GET /api/posts/author/{author} - 작성자로 검색")
    void getPostsByAuthor() throws Exception {
        // Given
        List<PostResponse> posts = Arrays.asList(postResponse);
        given(postService.getPostsByAuthor("테스트 작성자")).willReturn(posts);

        // When & Then
        mockMvc.perform(get("/api/posts/author/{author}", "테스트 작성자"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is("테스트 작성자")));
    }
}