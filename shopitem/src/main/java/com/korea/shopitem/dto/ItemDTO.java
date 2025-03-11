package com.korea.shopitem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.korea.shopitem.domain.Item;
import com.korea.shopitem.domain.ItemImage;
import com.korea.shopitem.domain.ItemOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    private Long id;
    private String name;
    private int price;
    private int stockQty;
    private boolean delFlag;


    @Builder.Default
    private Map<String, List<String>> options = new HashMap<>();

    // 파일 + 이미지명 따로 날아옴
    @Builder.Default
    @JsonIgnore // 포스트 맨에서 데이터 안나옴
    private List<MultipartFile> files = new ArrayList<>();

    // 파일 이름만 저장
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    // 생성자
    public ItemDTO (Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQty = item.getStockQty();
        this.delFlag = item.isDelFlag(); // boolean값은 is로 시작

        //ItemOptoin 엔티티 안에 getOption 메서드가 있음
        // 옵션을 Map<String, String>로 변환
        // ItemOption에서 보면 필드가 -> Map<option,value>
        // 현재 getOptions에는 : id, option, value
        // 값이 이렇게 존재할 경우가 있음 - "size": "m", "size" : "s" --> 최신값으로 값을 덮음 => "size" : "s" 만 남음
        // 동일한 key값에 대해 추가되지않고 덮어쓰게 됨
        this.options = (item.getOptions() != null && !item.getOptions().isEmpty())
                ? item.getOptions().stream()
                    .collect(Collectors.groupingBy(
                            ItemOption::getOption,
                            Collectors.mapping(ItemOption::getValue, Collectors.toList() ) )
                    )
                : Map.of();
        
        // 파일이름 List<String> 변환
        this.uploadFileNames = (item.getImageList()!=null && !item.getImageList().isEmpty())
                ? item.getImageList().stream().map(ItemImage::getFileName).toList()
                : List.of("default.png");
    }

}
