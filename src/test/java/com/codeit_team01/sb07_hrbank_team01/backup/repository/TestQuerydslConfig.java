// TestQuerydslConfig.java (테스트 전용 설정)
package com.codeit_team01.sb07_hrbank_team01.backup.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration // 테스트 전용 설정 파일임을 명시
public class TestQuerydslConfig {

  // @DataJpaTest가 로드하는 EntityManager를 사용하여 JPAQueryFactory를 빈으로 등록
  @Bean
  public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);

  }
}