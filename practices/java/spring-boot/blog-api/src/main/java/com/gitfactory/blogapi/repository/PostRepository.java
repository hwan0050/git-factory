package com.gitfactory.blogapi.repository;

import com.gitfactory.blogapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 게시글 Repository
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 제목으로 게시글 검색 (대소문자 구분 없음)
     *
     * @param title 검색할 제목 키워드
     * @return 제목에 키워드가 포함된 게시글 목록
     */
    List<Post> findByTitleContainingIgnoreCase(String title);

    /**
     * 제목으로 게시글 검색 (대소문자 구분)
     *
     * @param title 검색할 제목 키워드
     * @return 제목에 키워드가 포함된 게시글 목록
     */
    List<Post> findByTitleContaining(String title);

    /**
     * 작성자로 게시글 검색
     *
     * @param author 작성자명
     * @return 해당 작성자의 모든 게시글
     */
    List<Post> findByAuthor(String author);
}