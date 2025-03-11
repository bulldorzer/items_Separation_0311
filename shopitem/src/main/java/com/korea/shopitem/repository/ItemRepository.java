package com.korea.shopitem.repository;

import com.korea.shopitem.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /*
        select * from Item i
                left join Itemlmage img on i.item id = img.item id
                left join ItemOption opt on i.item_id = opt.item_id
         where i.item id = 1 and i.delFalg = false;


         select i from Item i
                left join i.imageList img
                left join i.options opt
                where i.id = 1 and i.delFlag = false;

        =>
        // FETCH 키워드
        - JPA 내부적으로 한번에 데이터를 가져오기 위한 최적화 역할을함
        - SQL에서는 일반 LEFT JOIN으로 변환됨

        select i from Item i
                left join FETCH i.imageList img
                left join FETCH i.options opt
                where i.id = 1 and i.delFlag = false;

        // 첫번째 단점 중복된 데이터가 발견되어 자원 낭비가 될수기에 DISTINCT를 사용
        select DISTINCT i from Item i
                left join FETCH i.imageList img
                left join FETCH i.options opt
                where i.id = 1 and i.delFlag = false;

        // 두번째 단점 LEFT JOIN
    */
    // 아이템 1개 조회
    // 필요할떄만 연관데이터로딩
    @EntityGraph(attributePaths = {"imageList", "options"})
    @Query("SELECT i FROM Item i WHERE i.id = :id AND i.delFlag = false")
    Item findItemWithDetatils(@Param("id") Long id);
}
