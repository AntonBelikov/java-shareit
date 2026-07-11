package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID) Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findByOwner(@RequestHeader(USER_ID) Long ownerId) {
        return itemService.findByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailable(@RequestParam String text) {
        return itemService.searchAvailable(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestHeader(USER_ID) Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentCreateDto commentCreateDto
    ) {
        return itemService.addComment(userId, itemId, commentCreateDto);
    }
}
