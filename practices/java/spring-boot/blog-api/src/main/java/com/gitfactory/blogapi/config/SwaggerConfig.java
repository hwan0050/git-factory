package com.gitfactory.blogapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 설정 클래스
 *
 * Swagger UI는 /swagger-ui/index.html 에서 접근 가능
 * OpenAPI 스펙은 /v3/api-docs 에서 JSON 형식으로 제공됨
 *
 * @author Git Factory
 * @since 2025-12-06
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 3.0 설정
     *
     * API 문서의 메타데이터를 정의합니다.
     * - 제목, 설명, 버전
     * - 연락처 정보
     * - 라이선스 정보
     * - 서버 정보
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        localServer(),
                        productionServer()
                ));
    }

    /**
     * API 정보 설정
     */
    private Info apiInfo() {
        return new Info()
                .title("Blog API Documentation")
                .description("""
                        ## Blog REST API
                        
                        이 API는 블로그 게시글을 관리하기 위한 RESTful API입니다.
                        
                        ### 주요 기능
                        - 게시글 CRUD (생성, 조회, 수정, 삭제)
                        - 게시글 검색 (제목, 작성자)
                        - JPA Auditing을 통한 생성일시/수정일시 자동 관리
                        
                        ### 기술 스택
                        - Spring Boot 3.4.12
                        - Spring Data JPA
                        - H2 Database (개발)
                        - PostgreSQL (프로덕션)
                        
                        ### 참고
                        - 모든 날짜는 ISO 8601 형식(yyyy-MM-dd'T'HH:mm:ss)을 따릅니다.
                        - 에러 응답은 표준 HTTP 상태 코드를 사용합니다.
                        """)
                .version("1.0.0")
                .contact(contact())
                .license(license());
    }

    /**
     * 연락처 정보
     */
    private Contact contact() {
        return new Contact()
                .name("Git Factory")
                .email("akma0050@naver.com")
                .url("https://github.com/hwan0050/git-factory");
    }

    /**
     * 라이선스 정보
     */
    private License license() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     * 로컬 서버 정보
     */
    private Server localServer() {
        return new Server()
                .url("http://localhost:8080")
                .description("로컬 개발 서버");
    }

    /**
     * 프로덕션 서버 정보
     */
    private Server productionServer() {
        return new Server()
                .url("https://api.gitfactory.com")
                .description("프로덕션 서버 (예시)");
    }
}