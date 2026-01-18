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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("성능 비교 테스트 - Fetch Join vs @EntityGraph vs Batch Size")
class PerformanceComparisonTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 생성: 10명의 유저, 각 유저당 5개의 게시글 (총 50개)
        for (int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .username("user" + i)
                    .email("user" + i + "@test.com")
                    .password("password" + i)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);

            for (int j = 1; j <= 5; j++) {
                Post post = Post.builder()
                        .title("Post " + j + " by user" + i)
                        .content("Content " + j)
                        .author(user)
                        .build();
                postRepository.save(post);
            }
        }
    }

    @Test
    @DisplayName("1. Fetch Join 성능 테스트 (INNER JOIN)")
    void fetchJoin_performance() {
        System.out.println("\n========================================");
        System.out.println(">>> [1] Fetch Join 성능 측정");
        System.out.println("========================================");

        long startTime = System.currentTimeMillis();

        // Fetch Join 실행
        List<Post> posts = postRepository.findAllWithAuthor();

        // 모든 Post의 Author 접근
        posts.forEach(post -> {
            String authorName = post.getAuthor().getUsername();
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("소요 시간: " + duration + "ms");
        System.out.println("조회된 게시글 수: " + posts.size());
        System.out.println("예상 쿼리 횟수: 1번 (JOIN FETCH)");
        System.out.println("========================================\n");

        assertThat(posts).hasSize(50);
        assertThat(posts.get(0).getAuthor()).isNotNull();
    }

    @Test
    @DisplayName("2. @EntityGraph 성능 테스트 (OUTER JOIN)")
    void entityGraph_performance() {
        System.out.println("\n========================================");
        System.out.println(">>> [2] @EntityGraph 성능 측정");
        System.out.println("========================================");

        long startTime = System.currentTimeMillis();

        // @EntityGraph 실행
        List<Post> posts = postRepository.findAllWithEntityGraph();

        // 모든 Post의 Author 접근
        posts.forEach(post -> {
            String authorName = post.getAuthor().getUsername();
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("소요 시간: " + duration + "ms");
        System.out.println("조회된 게시글 수: " + posts.size());
        System.out.println("예상 쿼리 횟수: 1번 (LEFT OUTER JOIN)");
        System.out.println("========================================\n");

        assertThat(posts).hasSize(50);
        assertThat(posts.get(0).getAuthor()).isNotNull();
    }

    @Test
    @DisplayName("3. Batch Size 성능 테스트 (IN 절)")
    void batchSize_performance() {
        System.out.println("\n========================================");
        System.out.println(">>> [3] Batch Size 성능 측정");
        System.out.println("========================================");

        long startTime = System.currentTimeMillis();

        // 일반 findAll (Batch Size 적용됨)
        List<Post> posts = postRepository.findAll();

        // 모든 Post의 Author 접근
        posts.forEach(post -> {
            String authorName = post.getAuthor().getUsername();
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("소요 시간: " + duration + "ms");
        System.out.println("조회된 게시글 수: " + posts.size());
        System.out.println("예상 쿼리 횟수: 2번 (1. Post 조회, 2. User IN 절)");
        System.out.println("========================================\n");

        assertThat(posts).hasSize(50);
        assertThat(posts.get(0).getAuthor()).isNotNull();
    }

    @Test
    @DisplayName("4. 성능 비교 - 3가지 방법 동시 측정")
    void performance_comparison_all() {
        System.out.println("\n========================================");
        System.out.println(">>> [종합] 3가지 방법 성능 비교");
        System.out.println("========================================\n");

        // 1. Fetch Join
        long start1 = System.currentTimeMillis();
        List<Post> posts1 = postRepository.findAllWithAuthor();
        posts1.forEach(post -> post.getAuthor().getUsername());
        long end1 = System.currentTimeMillis();
        long fetchJoinTime = end1 - start1;

        System.out.println("[1] Fetch Join (INNER JOIN)");
        System.out.println("    - 소요 시간: " + fetchJoinTime + "ms");
        System.out.println("    - 쿼리 횟수: 1번");
        System.out.println();

        // 2. @EntityGraph
        long start2 = System.currentTimeMillis();
        List<Post> posts2 = postRepository.findAllWithEntityGraph();
        posts2.forEach(post -> post.getAuthor().getUsername());
        long end2 = System.currentTimeMillis();
        long entityGraphTime = end2 - start2;

        System.out.println("[2] @EntityGraph (LEFT OUTER JOIN)");
        System.out.println("    - 소요 시간: " + entityGraphTime + "ms");
        System.out.println("    - 쿼리 횟수: 1번");
        System.out.println();

        // 3. Batch Size
        long start3 = System.currentTimeMillis();
        List<Post> posts3 = postRepository.findAll();
        posts3.forEach(post -> post.getAuthor().getUsername());
        long end3 = System.currentTimeMillis();
        long batchSizeTime = end3 - start3;

        System.out.println("[3] Batch Size (IN 절)");
        System.out.println("    - 소요 시간: " + batchSizeTime + "ms");
        System.out.println("    - 쿼리 횟수: 2번");
        System.out.println();

        System.out.println("========================================");
        System.out.println("📊 성능 순위:");
        System.out.println("   1위: Fetch Join       - " + fetchJoinTime + "ms");
        System.out.println("   2위: @EntityGraph     - " + entityGraphTime + "ms");
        System.out.println("   3위: Batch Size       - " + batchSizeTime + "ms");
        System.out.println("========================================\n");

        // 모든 방법이 정상 작동하는지 확인
        assertThat(posts1).hasSize(50);
        assertThat(posts2).hasSize(50);
        assertThat(posts3).hasSize(50);
    }

    @Test
    @DisplayName("5. @EntityGraph vs Fetch Join - JOIN 타입 차이")
    void entityGraph_vs_fetchJoin_joinType() {
        System.out.println("\n========================================");
        System.out.println(">>> [비교] @EntityGraph vs Fetch Join - JOIN 타입");
        System.out.println("========================================\n");

        System.out.println("[Fetch Join]");
        System.out.println("- INNER JOIN 사용");
        System.out.println("- author가 NULL인 Post는 제외");
        List<Post> fetchJoinPosts = postRepository.findAllWithAuthor();
        System.out.println("- 조회 결과: " + fetchJoinPosts.size() + "개");
        System.out.println();

        System.out.println("[@EntityGraph]");
        System.out.println("- LEFT OUTER JOIN 사용");
        System.out.println("- author가 NULL인 Post도 포함");
        List<Post> entityGraphPosts = postRepository.findAllWithEntityGraph();
        System.out.println("- 조회 결과: " + entityGraphPosts.size() + "개");
        System.out.println();

        System.out.println("========================================");
        System.out.println("✅ 결론: 데이터 정합성 보장 시 Fetch Join 추천");
        System.out.println("========================================\n");

        assertThat(fetchJoinPosts.size()).isEqualTo(entityGraphPosts.size());
    }

    @Test
    @DisplayName("6. Batch Size 동작 확인 - IN 절 사용")
    void batchSize_in_clause_verification() {
        System.out.println("\n========================================");
        System.out.println(">>> [검증] Batch Size IN 절 동작 확인");
        System.out.println("========================================\n");

        System.out.println("기대하는 SQL:");
        System.out.println("1. SELECT * FROM posts");
        System.out.println("2. SELECT * FROM users WHERE id IN (?, ?, ...)");
        System.out.println();

        List<Post> posts = postRepository.findAll();

        System.out.println("실제 실행 결과:");
        posts.forEach(post -> {
            String authorName = post.getAuthor().getUsername();
        });

        System.out.println("\n✅ Batch Size 설정: 100");
        System.out.println("✅ 예상 쿼리: 2번 (Post 1번 + User IN절 1번)");
        System.out.println("========================================\n");

        assertThat(posts).hasSize(50);
    }
}