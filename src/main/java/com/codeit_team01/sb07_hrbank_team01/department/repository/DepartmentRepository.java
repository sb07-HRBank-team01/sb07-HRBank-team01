package com.codeit_team01.sb07_hrbank_team01.department.repository;

import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(String name);


    //1.키워드가 널이면 상관없이 전부 조회
    //name  > 표시 사전순으로 큰것
    //카운트는 내리면 추가하는 방식이 아니고 처음 키워드만으로 전체를 받는다
    //응답은 계속 총량을 원한다

    //부서명 오름차순
    @Query(value = """
  SELECT d
  FROM Department d
  WHERE (:kw IS NULL
         OR d.name LIKE %:kw%
         OR d.description LIKE %:kw%)
    AND (
         :cursorName IS NULL
         OR d.name > :cursorName
         OR (d.name = :cursorName AND (:idAfter IS NULL OR d.id > :idAfter))
    )
  ORDER BY d.name ASC, d.id ASC
  """,
            countQuery = """
  SELECT COUNT(d)
  FROM Department d
  WHERE (:kw IS NULL
         OR d.name LIKE %:kw%
         OR d.description LIKE %:kw%)
  """)
    Page<Department> findByNameAsc(@Param("kw") String kw,
                                   @Param("cursorName") String cursorName,
                                   @Param("idAfter") Long idAfter,
                                   Pageable pageable);

   //부서명 내림차순
    @Query(value = """
  SELECT d
  FROM Department d
  WHERE (:kw IS NULL
         OR d.name LIKE %:kw%
         OR d.description LIKE %:kw%)
    AND (
         :cursorName IS NULL
         OR d.name < :cursorName
         OR (d.name = :cursorName AND (:idAfter IS NULL OR d.id < :idAfter))
    )
  ORDER BY d.name DESC, d.id DESC
  """,
            countQuery = """
  SELECT COUNT(d)
  FROM Department d
  WHERE (:kw IS NULL
         OR d.name LIKE %:kw%
         OR d.description LIKE %:kw%)
  """)
    Page<Department> findByNameDesc(@Param("kw") String kw,
                                    @Param("cursorName") String cursorName,
                                    @Param("idAfter") Long idAfter,
                                    Pageable pageable);

  //설립일 오름차순
    @Query(value = """
  SELECT d
  FROM Department d
  WHERE (:kw IS NULL
         OR d.name LIKE %:kw%
         OR d.description LIKE %:kw%)
    AND (
         :cursorDate IS NULL
         OR d.establishedDate > :cursorDate
         OR (d.establishedDate = :cursorDate AND (:idAfter IS NULL OR d.id > :idAfter))
    )
  ORDER BY d.establishedDate ASC, d.id ASC
  """,
            countQuery = """
  SELECT COUNT(d)
  FROM Department d
  WHERE (:kw IS NULL
         OR d.name LIKE %:kw%
         OR d.description LIKE %:kw%)
  """)
    Page<Department> findByDateAsc(@Param("kw") String kw,
                                   @Param("cursorDate") LocalDate cursorDate,
                                   @Param("idAfter") Long idAfter,
                                   Pageable pageable);

    //설립일 내림차순
    @Query(value = """
  SELECT d
  FROM Department d
  WHERE (:kw IS NULL
         OR d.name LIKE %:kw%
         OR d.description LIKE %:kw%)
    AND (
         :cursorDate IS NULL
         OR d.establishedDate < :cursorDate
         OR (d.establishedDate = :cursorDate AND (:idAfter IS NULL OR d.id < :idAfter))
    )
  ORDER BY d.establishedDate DESC, d.id DESC
  """,
            countQuery = """
  SELECT COUNT(d)
  FROM Department d
  WHERE (:kw IS NULL
         OR d.name LIKE %:kw%
         OR d.description LIKE %:kw%)
  """)
    Page<Department> findByDateDesc(@Param("kw") String kw,
                                    @Param("cursorDate") LocalDate cursorDate,
                                    @Param("idAfter") Long idAfter,
                                    Pageable pageable);

}
