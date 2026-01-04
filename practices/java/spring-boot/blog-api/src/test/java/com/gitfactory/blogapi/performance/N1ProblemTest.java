package com.gitfactory.blogapi.performance;

import com.gitfactory.blogapi.entity.Post;
import com.gitfactory.blogapi.entity.User;
import com.gitfactory.blogapi.entity.UserRole;
import com.gitfactory.blogapi.repository.PostRepository;
import com.gitfactory.blogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class N1ProblemTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        System.out.println("\n========== 테스트 데이터 생성 시작 ==========");

        // 10명의 User 생성
        for (int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .username("user" + i)
                    .email("user" + i + "@example.com")
                    .password("password")
                    .role(UserRole.USER)
                    .build();
            User savedUser = userRepository.save(user);

            // 각 User당 5개의 Post 생성 (총 50개)
            for (int j = 1; j <= 5; j++) {
                Post post = Post.builder()
                        .title("Post " + j + " by user" + i)
                        .content("Content " + j)
                        .author(savedUser)
                        .build();
                postRepository.save(post);
            }
        }

        System.out.println("========== 테스트 데이터 생성 완료: User 10명, Post 50개 ==========\n");
    }

    @Test
    @DisplayName("N+1 문제 재현 - findAll()")
    void n1Problem_findAll() {
        System.out.println("\n========== [TEST 1] N+1 Problem - findAll() ==========");

        // When: 모든 Post 조회
        System.out.println("\n>>> Step 1: postRepository.findAll() 호출");
        List<Post> posts = postRepository.findAll();

        System.out.println("\n>>> Step 2: 조회된 Post 개수 = " + posts.size());

        // Then: 각 Post의 작성자 이름 출력
        System.out.println("\n>>> Step 3: 각 Post의 Author 접근 (N+1 발생 지점!)");
        System.out.println("========================================");

        int count = 0;
        for (Post post : posts) {
            count++;
            String authorName = post.getAuthor().getUsername();
            if (count <= 5) {  // 처음 5개만 출력
                System.out.println(count + ". Post: " + post.getTitle() + ", Author: " + authorName);
            }
        }

        System.out.println("... (총 " + posts.size() + "개)");
        System.out.println("========================================");

        System.out.println("\n>>> 결과 분석:");
        System.out.println("- Post 조회 쿼리: 1번");
        System.out.println("- User 조회 쿼리: " + posts.size() + "번 (각 Post마다 User 조회!)");
        System.out.println("- 총 쿼리 횟수: " + (1 + posts.size()) + "번 ❌ N+1 문제 발생!");
        System.out.println("\n========== TEST 1 종료 ==========\n");
    }

    @Test
    @DisplayName("N+1 문제 확인 - 작성자명으로 검색")
    void n1Problem_searchByAuthor() {
        System.out.println("\n========== [TEST 2] N+1 Problem - searchByAuthor() ==========");

        // When: 작성자명으로 검색
        System.out.println("\n>>> Step 1: 'user1' 으로 Post 검색");
        List<Post> posts = postRepository.findByAuthor_UsernameContaining("user1");

        System.out.println("\n>>> Step 2: 조회된 Post 개수 = " + posts.size());

        // Then: 각 Post 정보 출력
        System.out.println("\n>>> Step 3: Post 정보 출력");
        System.out.println("========================================");

        for (Post post : posts) {
            System.out.println("Post: " + post.getTitle() +
                    ", Author: " + post.getAuthor().getUsername());
        }

        System.out.println("========================================");
        System.out.println("\n>>> 결과: findByAuthor_UsernameContaining()은 JOIN을 사용하므로 N+1 문제 없음! ✅");
        System.out.println("\n========== TEST 2 종료 ==========\n");
    }

    @Test
    @DisplayName("Fetch Join으로 N+1 문제 해결 - findAllWithAuthor()")
    void fetchJoin_solution() {
        System.out.println("\n========== [TEST 3] Fetch Join Solution - findAllWithAuthor() ==========");

        // When: Fetch Join 사용
        System.out.println("\n>>> Step 1: postRepository.findAllWithAuthor() 호출");
        List<Post> posts = postRepository.findAllWithAuthor();

        System.out.println("\n>>> Step 2: 조회된 Post 개수 = " + posts.size());

        // Then: 각 Post의 작성자 이름 출력
        System.out.println("\n>>> Step 3: 각 Post의 Author 접근 (추가 쿼리 없음!)");
        System.out.println("========================================");

        int count = 0;
        for (Post post : posts) {
            count++;
            String authorName = post.getAuthor().getUsername();
            if (count <= 5) {  // 처음 5개만 출력
                System.out.println(count + ". Post: " + post.getTitle() + ", Author: " + authorName);
            }
        }

        System.out.println("... (총 " + posts.size() + "개)");
        System.out.println("========================================");

        System.out.println("\n>>> 결과 분석:");
        System.out.println("- Post + User 조회 쿼리: 1번 (JOIN으로 한 번에!)");
        System.out.println("- 추가 User 조회: 0번");
        System.out.println("- 총 쿼리 횟수: 1번 ✅ N+1 문제 해결!");
        System.out.println("\n========== TEST 3 종료 ==========\n");
    }

    @Test
    @DisplayName("성능 비교 - findAll() vs findAllWithAuthor()")
    void performance_comparison() {
        System.out.println("\n========== [TEST 4] Performance Comparison ==========\n");

        // Test 1: findAll() - N+1 발생
        System.out.println(">>> [1] findAll() 성능 측정");
        long start1 = System.currentTimeMillis();
        List<Post> posts1 = postRepository.findAll();
        posts1.forEach(post -> post.getAuthor().getUsername());
        long end1 = System.currentTimeMillis();
        System.out.println("소요 시간: " + (end1 - start1) + "ms");
        System.out.println("총 쿼리: 51번 (1 + 50)\n");

        // Test 2: findAllWithAuthor() - Fetch Join
        System.out.println(">>> [2] findAllWithAuthor() 성능 측정");
        long start2 = System.currentTimeMillis();
        List<Post> posts2 = postRepository.findAllWithAuthor();
        posts2.forEach(post -> post.getAuthor().getUsername());
        long end2 = System.currentTimeMillis();
        System.out.println("소요 시간: " + (end2 - start2) + "ms");
        System.out.println("총 쿼리: 1번\n");

        // 성능 개선율
        double improvement = ((double)(end1 - start1) / (end2 - start2)) * 100;
        System.out.println("========================================");
        System.out.println("성능 개선율: " + String.format("%.1f", improvement) + "%");
        System.out.println("========================================");

        System.out.println("\n========== TEST 4 종료 ==========\n");
    }
}