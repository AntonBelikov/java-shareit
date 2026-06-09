package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundObject;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;


    public Item findByIdOrThrow(Long itemId) {
        return itemRepository.findItemById(itemId)
                .orElseThrow(() -> new NotFoundObject("Вещь не найдена"));
    }

    public ItemDto create(Long userId, ItemDto itemDto) {
        User owner = userMapper.toModel(userService.findByIdOrThrow(userId));
        ItemDto itemDtoNew = itemMapper.toDto(itemRepository.create(itemMapper.toModel(itemDto, owner, null)));
        return itemDtoNew;
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = findByIdOrThrow(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundObject("Только владелец может обновлять статус вещи");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item updated = itemRepository.update(item);
        return itemMapper.toDto(updated);
    }

    public ItemDto findById(Long itemId) {
        return itemMapper.toDto(findByIdOrThrow(itemId));
    }

    public List<ItemDto> findByOwner(Long ownerId) {
        userService.findByIdOrThrow(ownerId);
        return itemRepository.findByOwner(ownerId).stream()
                .map(itemMapper::toDto)
                .toList();
    }

    public List<ItemDto> searchAvailable(String text) {
        return itemRepository.findAvailable(text).stream()
                .map(itemMapper::toDto)
                .toList();
    }
}
