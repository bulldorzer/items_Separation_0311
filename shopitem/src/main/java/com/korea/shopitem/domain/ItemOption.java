package com.korea.shopitem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_option_id")
    private Long id;
    
    // 옵션 H2 DB에서 option,value 예약어라 이름 바꿔야함
    @Column(name = "option_name")
    private String option;
    // 옵션값
    @Column(name = "option_value")
    private String value;
}
