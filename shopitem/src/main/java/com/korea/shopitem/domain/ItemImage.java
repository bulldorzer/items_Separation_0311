package com.korea.shopitem.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;
    private String fileName;

    @Setter
    private int ord; // 그림순번

}
