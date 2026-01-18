package com.gitfactory.blogapi.repository;

import com.gitfactory.blogapi.entity.Post;
import com.gitfactory.blogapi.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 제목으로 검색
    List<Post> findByTitleContaining(String keyword);

    // ✨ 작성자 이름으로 검색 (연관 관계 쿼리)
    List<Post> findByAuthor_UsernameContaining(String keyword);

    // ✨ 특정 사용자의 게시글 조회
    List<Post> findByAuthor(User author);

    // ✨ 사용자 ID로 게시글 조회
    List<Post> findByAuthorId(Long authorId);

    // ✨ Fetch Join으로 N+1 문제 해결 (INNER JOIN)
    @Query("SELECT p FROM Post p JOIN FETCH p.author")
    List<Post> findAllWithAuthor();

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :id")
    Post findByIdWithAuthor(Long id);

    // ✨ @EntityGraph로 N+1 문제 해결 (OUTER JOIN, 간결한 코드)
    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT p FROM Post p")
    List<Post> findAllWithEntityGraph();

    @EntityGraph(attributePaths = {"author"})
    Optional<Post> findWithAuthorById(Long id);
}