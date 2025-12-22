package com.gitfactory.blogapi.service;

import com.gitfactory.blogapi.dto.PostRequest;
import com.gitfactory.blogapi.dto.PostResponse;
import com.gitfactory.blogapi.entity.Post;
import com.gitfactory.blogapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService 테스트")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post post;
    private PostRequest postRequest;

    @BeforeEach
    void setUp() {
        // Record 타입이므로 생성자 사용
        postRequest = new PostRequest(
                "테스트 제목",
                "테스트 내용",
                "테스트 작성자"
        );

        // Post Entity는 id 제외하고 생성
        post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .author("테스트 작성자")
                .build();
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    void createPost() {
        // Given
        given(postRepository.save(any(Post.class))).willReturn(post);

        // When
        PostResponse result = postService.createPost(postRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("테스트 제목");
        assertThat(result.content()).isEqualTo("테스트 내용");
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    void getAllPosts() {
        // Given
        List<Post> posts = Arrays.asList(post);
        given(postRepository.findAll()).willReturn(posts);

        // When
        List<PostResponse> result = postService.getAllPosts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("테스트 제목");
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("ID로 게시글 조회 성공 테스트")
    void getPostById_Success() {
        // Given
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // When
        PostResponse result = postService.getPostById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("테스트 제목");
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("ID로 게시글 조회 실패 테스트")
    void getPostById_NotFound() {
        // Given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.getPostById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post not found with id: 999");
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updatePost() {
        // Given
        PostRequest updateRequest = new PostRequest(
                "수정된 제목",
                "수정된 내용",
                "수정된 작성자"
        );

        Post updatedPost = Post.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .author("수정된 작성자")
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(updatedPost);

        // When
        PostResponse result = postService.updatePost(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("수정된 제목");
        assertThat(result.content()).isEqualTo("수정된 내용");
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePost() {
        // Given
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // When
        postService.deletePost(1L);

        // Then
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("제목으로 검색 테스트")
    void searchByTitle() {
        // Given
        List<Post> posts = Arrays.asList(post);
        given(postRepository.findByTitleContaining("테스트")).willReturn(posts);

        // When
        List<PostResponse> result = postService.searchByTitle("테스트");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("테스트 제목");
        verify(postRepository, times(1)).findByTitleContaining("테스트");
    }

    @Test
    @DisplayName("작성자로 검색 테스트")
    void getPostsByAuthor() {
        // Given
        List<Post> posts = Arrays.asList(post);
        given(postRepository.findByAuthor("테스트 작성자")).willReturn(posts);

        // When
        List<PostResponse> result = postService.getPostsByAuthor("테스트 작성자");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).author()).isEqualTo("테스트 작성자");
        verify(postRepository, times(1)).findByAuthor("테스트 작성자");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정 시도 테스트")
    void updatePost_NotFound() {
        // Given
        PostRequest updateRequest = new PostRequest(
                "수정된 제목",
                "수정된 내용",
                "수정된 작성자"
        );
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.updatePost(999L, updateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post not found with id: 999");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 시도 테스트")
    void deletePost_NotFound() {
        // Given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.deletePost(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post not found with id: 999");
    }
}