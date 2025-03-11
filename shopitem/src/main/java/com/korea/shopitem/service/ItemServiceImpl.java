package com.korea.shopitem.service;

import com.korea.shopitem.domain.Item;
import com.korea.shopitem.domain.ItemImage;
import com.korea.shopitem.domain.ItemOption;
import com.korea.shopitem.dto.ItemDTO;
import com.korea.shopitem.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    @Override
    public ItemDTO createItem(ItemDTO itemDTO) {

        Item item = Item.builder()
                .name(itemDTO.getName())
                .price(itemDTO.getPrice())
                .stockQty(itemDTO.getStockQty())
                .delFlag(itemDTO.isDelFlag())
                .build();

        // 데이터 Map<String, List<String>>
        if (itemDTO.getOptions() != null && !itemDTO.getOptions().isEmpty()) {
            itemDTO.getOptions().forEach((option, values) -> {
                values.forEach(value -> {
                    ItemOption opt = ItemOption.builder().option(option).value(value).build();
                    item.addOption(opt);
                });
            });
        }

        // 이미지 저장 - List<String>
        if (itemDTO.getUploadFileNames() != null && !itemDTO.getUploadFileNames().isEmpty()){
            itemDTO.getUploadFileNames().forEach(fileName ->
                    item.addImage(
                            ItemImage.builder().fileName(fileName).build()
                    ));
        }
        Item savedItem = itemRepository.save(item);

        return new ItemDTO(savedItem);
    }

    @Override
    public List<ItemDTO> getAllItems() {

        return itemRepository.findAll().stream().map(ItemDTO::new).collect(Collectors.toList());
    }

    @Override
    public Page<ItemDTO> getAllItems(Pageable pageable) {
        Page<Item> result = itemRepository.getAllItems(pageable);
        return result.map(
                item -> {
                    Map<String,List<String>> options = item.getOptions().stream()
                            .collect(Collectors.groupingBy(
                            ItemOption::getOption,
                            Collectors.mapping(ItemOption::getValue, Collectors.toList() )));

                    List<String> images = item.getImageList().stream().map(ItemImage::getFileName).toList();

                    return ItemDTO.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .price(item.getPrice())
                            .stockQty(item.getStockQty())
                            .options(options)
                            .uploadFileNames(images).build();
                }
        );
    }

    @Override
    public ItemDTO getOne(Long id) {
        Item item = itemRepository.findItemWithDetatils(id);
        if (item == null){
            throw new IllegalArgumentException("해당 ID의 아이템이 존재하지 않습니다. "+id);
        }
        return new ItemDTO(item);
    }

    @Override
    public ItemDTO updateItem(Long id, ItemDTO itemDTO) {

        Item item = itemRepository.findById(id).orElseThrow();

        item.changeName(itemDTO.getName());
        item.changePrice(itemDTO.getPrice());
        item.changeStockQuantity(itemDTO.getStockQty());
        item.changeDelFlag(item.isDelFlag());
        return new ItemDTO(item);
    }

    @Override
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow();
        item.changeDelFlag(true); // false->true로 바꿈
        itemRepository.save(item);

    }
}
