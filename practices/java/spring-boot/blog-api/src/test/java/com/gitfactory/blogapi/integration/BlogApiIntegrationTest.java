package com.gitfactory.blogapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitfactory.blogapi.dto.PostRequest;
import com.gitfactory.blogapi.entity.User;
import com.gitfactory.blogapi.entity.UserRole;
import com.gitfactory.blogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("BlogApi 통합 테스트")
class BlogApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Long userId;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(testUser);
        userId = savedUser.getId();
    }

    @Test
    @DisplayName("통합 테스트: 게시글 전체 CRUD 플로우")
    void 게시글_전체_CRUD_플로우_테스트() throws Exception {
        // 1. 게시글 생성 (POST)
        PostRequest createRequest = new PostRequest(
                "통합 테스트 제목",
                "통합 테스트 내용입니다.",
                userId
        );

        String response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("통합 테스트 제목"))
                .andExpect(jsonPath("$.content").value("통합 테스트 내용입니다."))
                .andExpect(jsonPath("$.authorName").value("testuser"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long postId = objectMapper.readTree(response).get("id").asLong();

        // 2. 생성된 게시글 조회 (GET)
        mockMvc.perform(get("/api/posts/{id}", postId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("통합 테스트 제목"));

        // 3. 게시글 수정 (PUT)
        PostRequest updateRequest = new PostRequest(
                "수정된 제목",
                "수정된 내용입니다.",
                userId
        );

        mockMvc.perform(put("/api/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.content").value("수정된 내용입니다."));

        // 4. 게시글 삭제 (DELETE)
        mockMvc.perform(delete("/api/posts/{id}", postId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // 5. 삭제 확인 (GET - 404 예상)
        mockMvc.perform(get("/api/posts/{id}", postId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("통합 테스트: 여러 게시글 생성 후 전체 조회")
    void 여러_게시글_생성_후_전체_조회() throws Exception {
        // Given - 3개 게시글 생성
        for (int i = 1; i <= 3; i++) {
            PostRequest request = new PostRequest(
                    "제목 " + i,
                    "내용 " + i,
                    userId
            );
            mockMvc.perform(post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // When & Then - 전체 조회
        mockMvc.perform(get("/api/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("제목 1"))
                .andExpect(jsonPath("$[1].title").value("제목 2"))
                .andExpect(jsonPath("$[2].title").value("제목 3"));
    }

    @Test
    @DisplayName("통합 테스트: 제목으로 게시글 검색")
    void 제목으로_게시글_검색() throws Exception {
        // Given - 여러 게시글 생성
        PostRequest post1 = new PostRequest("Spring Boot 학습", "내용1", userId);
        PostRequest post2 = new PostRequest("JPA 학습", "내용2", userId);
        PostRequest post3 = new PostRequest("Spring Security", "내용3", userId);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post3)))
                .andExpect(status().isCreated());

        // When & Then - "Spring" 키워드로 검색
        mockMvc.perform(get("/api/posts/search")
                        .param("keyword", "Spring"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Spring Boot 학습"))
                .andExpect(jsonPath("$[1].title").value("Spring Security"));
    }

    @Test
    @DisplayName("통합 테스트: 작성자로 게시글 검색")
    void 작성자로_게시글_검색() throws Exception {
        // Given - 두 명의 사용자 생성
        User user1 = User.builder()
                .username("홍길동")
                .email("hong@example.com")
                .password("password")
                .role(UserRole.USER)
                .build();
        Long user1Id = userRepository.save(user1).getId();

        User user2 = User.builder()
                .username("김철수")
                .email("kim@example.com")
                .password("password")
                .role(UserRole.USER)
                .build();
        Long user2Id = userRepository.save(user2).getId();

        // 게시글 생성
        PostRequest post1 = new PostRequest("제목1", "내용1", user1Id);
        PostRequest post2 = new PostRequest("제목2", "내용2", user1Id);
        PostRequest post3 = new PostRequest("제목3", "내용3", user2Id);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post3)))
                .andExpect(status().isCreated());

        // When & Then - "홍길동" 작성자 검색
        mockMvc.perform(get("/api/posts/search/author")
                        .param("keyword", "홍길동"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].authorName").value("홍길동"))
                .andExpect(jsonPath("$[1].authorName").value("홍길동"));
    }

    @Test
    @DisplayName("통합 테스트: 존재하지 않는 게시글 조회 시 404 에러")
    void 존재하지_않는_게시글_조회_404() throws Exception {
        // When & Then - 존재하지 않는 ID로 조회
        mockMvc.perform(get("/api/posts/{id}", 99999L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Post not found with id: 99999"));
    }
}