package com.gitfactory.blogapi.repository;

import com.gitfactory.blogapi.entity.Post;
import com.gitfactory.blogapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}