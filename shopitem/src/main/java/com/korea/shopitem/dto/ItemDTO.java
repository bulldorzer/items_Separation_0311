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
import java.util.List;

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
    private List<String> options = new ArrayList<>();

    // 파일 + 이미지명 따로 날아옴
    @Builder.Default
    @JsonIgnore // 포스트 맨에서 데이터 안나옴
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    // 생성자
    public ItemDTO (Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQty = item.getStockQty();
        this.delFlag = item.isDelFlag(); // boolean값은 is로 시작
        this.options = (item.getOptions()!=null)
                ? item.getOptions().stream().map(ItemOption::getOption).toList()
                : List.of();
        this.uploadFileNames = (item.getImageList()!=null && !item.getImageList().isEmpty())
                ? item.getImageList().stream().map(ItemImage::getFileName).toList()
                : List.of("default.png");
    }

}
