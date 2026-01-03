package com.gitfactory.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitfactory.blogapi.dto.PostRequest;
import com.gitfactory.blogapi.dto.PostResponse;
import com.gitfactory.blogapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Spring REST Docs를 활용한 API 문서 자동 생성 테스트
 *
 * @AutoConfigureRestDocs: REST Docs 자동 설정
 * @ExtendWith(RestDocumentationExtension.class): REST Docs 확장 기능
 */
@WebMvcTest(PostController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class PostControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private PostResponse sampleResponse;

    @BeforeEach
    void setup() {
        sampleResponse = new PostResponse(
                1L,
                "테스트 제목",
                "테스트 내용",
                "테스트 작성자",
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void 전체_게시글_조회_API_문서화() throws Exception {
        // Given
        List<PostResponse> posts = Arrays.asList(sampleResponse);
        given(postService.getAllPosts()).willReturn(posts);

        // When & Then
        mockMvc.perform(get("/api/posts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("posts-get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("게시글 ID"),
                                fieldWithPath("[].title").description("게시글 제목"),
                                fieldWithPath("[].content").description("게시글 내용"),
                                fieldWithPath("[].authorName").description("작성자 이름"),
                                fieldWithPath("[].authorId").description("작성자 ID"),
                                fieldWithPath("[].createdAt").description("생성일시"),
                                fieldWithPath("[].updatedAt").description("수정일시")
                        )
                ));
    }

    @Test
    void 게시글_ID로_조회_API_문서화() throws Exception {
        // Given
        given(postService.getPostById(anyLong())).willReturn(sampleResponse);

        // When & Then
        mockMvc.perform(get("/api/posts/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("posts-get-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("authorName").description("작성자 이름"),
                                fieldWithPath("authorId").description("작성자 ID"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("updatedAt").description("수정일시")
                        )
                ));
    }

    @Test
    void 게시글_생성_API_문서화() throws Exception {
        // Given
        PostRequest request = new PostRequest("새 게시글", "새 내용", 1L);
        given(postService.createPost(any(PostRequest.class))).willReturn(sampleResponse);

        // When & Then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("posts-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("userId").description("작성자 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("authorName").description("작성자 이름"),
                                fieldWithPath("authorId").description("작성자 ID"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("updatedAt").description("수정일시")
                        )
                ));
    }

    @Test
    void 게시글_수정_API_문서화() throws Exception {
        // Given
        PostRequest request = new PostRequest("수정된 제목", "수정된 내용", 1L);
        PostResponse updatedResponse = new PostResponse(
                1L,
                "수정된 제목",
                "수정된 내용",
                "테스트 작성자",
                1L,
                sampleResponse.createdAt(),
                LocalDateTime.now()
        );
        given(postService.updatePost(anyLong(), any(PostRequest.class))).willReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/api/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("posts-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("수정할 게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("수정할 제목"),
                                fieldWithPath("content").description("수정할 내용"),
                                fieldWithPath("userId").description("사용자 ID (수정 시 무시됨)")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("수정된 제목"),
                                fieldWithPath("content").description("수정된 내용"),
                                fieldWithPath("authorName").description("작성자 이름"),
                                fieldWithPath("authorId").description("작성자 ID"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("updatedAt").description("수정일시")
                        )
                ));
    }

    @Test
    void 게시글_삭제_API_문서화() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/posts/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(document("posts-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("삭제할 게시글 ID")
                        )
                ));
    }

    @Test
    void 제목으로_검색_API_문서화() throws Exception {
        // Given
        List<PostResponse> posts = Arrays.asList(sampleResponse);
        given(postService.searchByTitle(any(String.class))).willReturn(posts);

        // When & Then
        mockMvc.perform(get("/api/posts/search")
                        .param("keyword", "테스트")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("posts-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("keyword").description("검색할 키워드 (제목)")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("게시글 ID"),
                                fieldWithPath("[].title").description("게시글 제목"),
                                fieldWithPath("[].content").description("게시글 내용"),
                                fieldWithPath("[].authorName").description("작성자 이름"),
                                fieldWithPath("[].authorId").description("작성자 ID"),
                                fieldWithPath("[].createdAt").description("생성일시"),
                                fieldWithPath("[].updatedAt").description("수정일시")
                        )
                ));
    }

    @Test
    void 작성자로_검색_API_문서화() throws Exception {
        // Given
        List<PostResponse> posts = Arrays.asList(sampleResponse);
        given(postService.getPostsByAuthor(any(String.class))).willReturn(posts);

        // When & Then
        mockMvc.perform(get("/api/posts/search/author")
                        .param("keyword", "테스트작성자")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("posts-search-by-author",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("keyword").description("검색할 작성자명")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("게시글 ID"),
                                fieldWithPath("[].title").description("게시글 제목"),
                                fieldWithPath("[].content").description("게시글 내용"),
                                fieldWithPath("[].authorName").description("작성자 이름"),
                                fieldWithPath("[].authorId").description("작성자 ID"),
                                fieldWithPath("[].createdAt").description("생성일시"),
                                fieldWithPath("[].updatedAt").description("수정일시")
                        )
                ));
    }
}