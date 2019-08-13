package base.controller;

import base.dto.PagingQueryResponse;
import base.util.Json;
import base.util.query.Query;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author csieflyman
 */
@Slf4j
@RestController
public abstract class AbstractController {

    @Autowired
    protected HttpServletRequest request;

    protected <T> ResponseEntity findEntities(Function<Query, Long> findSizeFunction, Function<Query, List<T>> findFunction,
                                                                  Query query, Consumer<T> entityConsumer, Function<T, JsonNode> toJsonFunction) {
        if(query == null) {
            query = Query.create(request.getParameterMap());
        }

        if (query.isOnlySize()) {
            return ResponseEntity.ok(String.valueOf(findSizeFunction.apply(query)));
        }
        else {
            List<T> entities = findFunction.apply(query);
            if(entityConsumer != null) {
                entities.forEach(entityConsumer);
            }
            List<JsonNode> nodes = entities.stream().map(e -> toJsonFunction == null ? Json.toJsonNode(e) : toJsonFunction.apply(e)).collect(Collectors.toList());
            if(query.isPagingQuery()) {
                long total = findSizeFunction.apply(query);
                return ResponseEntity.ok(Json.toJsonNode(new PagingQueryResponse<>(total, query.getPageSize(), query.getPageNo(), nodes)));
            }
            else {
                return ResponseEntity.ok(Json.newArray().addAll(nodes));
            }
        }
    }
}