package com.korea.shopitem.service;

import com.korea.shopitem.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemDTO createItem(ItemDTO itemDTO);
    List<ItemDTO> getAllItems();
    Page<ItemDTO> getAllItems(Pageable pageable);
    ItemDTO getOne(Long id);
    ItemDTO updateItem(Long id, ItemDTO itemDTO);
    void deleteItem(Long id);
}
