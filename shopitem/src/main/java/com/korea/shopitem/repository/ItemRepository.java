package com.korea.shopitem.repository;

import com.korea.shopitem.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    /*
    * JPA에서 제공, 메소드 네이밍 규칙
    * - 쿼리를 작성하지 않아도 메서드 이름 기반으로 쿼리를 시행해줌
    *
    * - 필드 기준으로 조회
    * [Select 기능]
    * findBy필드명
    * readBy필드명
    * getBy필드명
    * queryBy필드명
    *
    * ex) List<User> findByName(String name);
    *
    * [갯수 조회]
    * CountBy필드명 (Long타입 반환)
    * ex) long countByAge(int age);
    *
    * [존재여부] existsBy (Boolean타입 반환)
    * ex) boolean existsByEmail(String email);
    *
    * [삭제기능] deleteBy~ (void타입 반환)
    * ex) void deleteById(Long id);
    * */

    List<Item> findByNameLike(String name);

    @Query("SELECT i FROM Item i WHERE i.delFlag = false")
    Page<Item> getAllItems(Pageable pageable);
}
