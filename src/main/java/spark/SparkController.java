package spark;

import base.controller.AbstractController;
import base.util.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * @author csieflyman
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/spark")
public class SparkController extends AbstractController {

    @Autowired
    private SparkService sparkService;

    @GetMapping(value = {"/query"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity query(@RequestParam() MultiValueMap<String, String> requestParam) {
        log.info("================= Spark Query =========================");

        if(!requestParam.containsKey("name")) {
            return ResponseEntity.badRequest().body("name parameter is required!");
        }

        return findEntities(sparkService::findSize, sparkService::find, Query.create(requestParam), null, null);
    }
}
