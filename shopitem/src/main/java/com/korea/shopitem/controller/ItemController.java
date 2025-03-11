package com.korea.shopitem.controller;

import com.korea.shopitem.dto.ItemDTO;
import com.korea.shopitem.service.ItemService;
import com.korea.shopitem.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/items")
public class ItemController {

    private final CustomFileUtil fileUtil;
    private final ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<?> register(ItemDTO itemDTO){

        try {
            List<MultipartFile> files = itemDTO.getFiles();
            List<String> uploadFileNames = fileUtil.saveFiles(files);
            itemDTO.setUploadFileNames(uploadFileNames);
            ItemDTO dto = itemService.createItem(itemDTO); // 엔티티에 저장후 DTO 반환함

            /*
            * 201 created 코드 + 데이터 함께 반환
            * 200 : 처리성공(단순 데이터 조회 및 반환)
            * 201 : 새로운 데이터 생성(Created)
            */
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            // INTERNAL_SERVER_ERROR : 500번에러 : 서버내부오류
            log.error("상품 등록중 오류 발생: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "상품등록실패", "message", e.getMessage() )
            );
        }


    }
}
