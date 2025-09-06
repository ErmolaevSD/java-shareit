package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreatedDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long ownerId, ItemCreatedDto itemCreatedDto) {
        return post("", ownerId, itemCreatedDto);
    }

    public ResponseEntity<Object> get(Integer itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> update(Integer ownerId, Integer itemId, ItemUpdateDto itemUpdateDto) {
        return patch("/" + itemId, ownerId, itemUpdateDto);
    }

    public ResponseEntity<Object> getAllItem(Integer ownerId) {
        return get("", ownerId.longValue(), null);
    }

    public ResponseEntity<Object> createComment(Integer ownerId, Integer itemId, CommentCreatedDto commentCreatedDto) {
        return post("/" + itemId + "/comment", ownerId, commentCreatedDto);
    }

    public ResponseEntity<Object> searchItem(String text) {
        Map<String, Object> params = new HashMap<>();
        params.put("text", text);
        return get("?text={text}", null, params);
    }
}