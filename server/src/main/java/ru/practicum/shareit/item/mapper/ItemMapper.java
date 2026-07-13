package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ItemMapper {
    public ItemDto toDto(Item item) {
        if (item == null) {
            return null;
        }

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());

        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId());
        }

        return dto;
    }

    public Item toModel(ItemDto dto, User owner, ItemRequest request) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);
        item.setRequest(request);
        return item;
    }

    public Item toEntity(ItemDto dto) {
        if (dto == null) {
            return null;
        }

        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public void update(ItemDto dto, Item item) {
        if (dto == null || item == null) {
            return;
        }

        if (dto.getName() != null) {
            item.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }

        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
    }

    public List<ItemDto> toDtoList(List<Item> items) {
        if (items == null) {
            return Collections.emptyList();
        }

        List<ItemDto> dtos = new ArrayList<>(items.size());
        for (Item item : items) {
            dtos.add(toDto(item));
        }
        return dtos;
    }
}
