package com.korea.shopitem.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"options", "imageList"})
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    
    private String name;
    private int price;
    private int stockQty;
    private boolean delFlag;

    // 구분자 상황에따라 사용
    // private String itemType;


    /*
     * 아이템 이미지
     * cascade = 부모데이터가 모든데이터변경작업시(CUD) 모든 자식까지 영향간다
     * orphanRemoval = true = 부모가 삭제될시 자식까지 삭제 시키겠다.
     *  엔티티 컨택스트의 상태
     * - 저장 : persist
     * - 병합 : merge
     * - 삭제 : remove
     * - 새로고침 : refresh
     * - 연관해재 : detach
     *
     * 엔티티에서 DB까지 적용되는 경로 => 엔티티 -> 영속성컨텍스트(EntityManager) -> DB
     * cascade 옵션 : 부모 엔티티에서 자식 엔티티에 대한 영속성이 전파(전이)가 일어나개 할것인지 사용여부
     * - 설정 X : 전파안됨
     * - 개별적으로 설정
     * - 전체설정 CascadeType.All
     *
     * ex) 전파가 되는 예시
     * 부모엔티티를 저장하면 자식 엔티티도 같이 저장된다.(자동저장)
     * -> 설정 안하면 : 부모엔티티 저장해도 자식은 저장되지 않는다.
     * */

    // 아이템옵션
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    @Builder.Default
    private List<ItemOption> options = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    @Builder.Default
    private List<ItemImage> imageList = new ArrayList<>();

    // 재고증가
    public void addStock(int qty){
        this.stockQty += qty;
    }

    // 재고삭제
    public void removeStock(int qty) throws Exception{
        int remainingStock = this.stockQty-qty;
        if (remainingStock<0){
            throw new Exception("need more stock");

        }

        this.stockQty = remainingStock;
    }

    // 옵션 추가
    public void addOption(ItemOption option){
        this.options.add(option);
    }

    // 상품에 이미지 추가
    public void addImage(ItemImage image){
        image.setOrd(this.imageList.size());
        this.imageList.add(image);
    }

    // 상품에 이미지 파일이름 관리
    public void addImageString(String fileName){

        ItemImage itemImage = ItemImage.builder().fileName(fileName).build();
        addImage(itemImage);
    }

    // 이미지 리스트 초기화
    public void clearList(){
        this.imageList.clear();
    }

    // 이름변경
    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeStockQuantity(int stockQty) {
        this.stockQty = stockQty;
    }

    public void changeDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }
}
