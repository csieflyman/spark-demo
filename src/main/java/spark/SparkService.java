package spark;

import base.util.Json;
import base.util.query.Query;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author csieflyman
 */
@Slf4j
@Service
public class SparkService<T extends JsonNode>{

    @Autowired
    private Environment env;

    @Autowired
    private SparkSession sparkSession;

    public List<JsonNode> find(Query query) {
        JavaRDD<String> rdd = getJavaRDD((String) query.getParam("name"));
        // TODO convert query object to filter
        return rdd.collect().stream().map(Json::parse).collect(Collectors.toList());
    }

    public long findSize(Query query) {
        JavaRDD<String> rdd = getJavaRDD((String) query.getParam("name"));
        return rdd.collect().stream().map(Json::parse).count();
    }

    private JavaRDD<String> getJavaRDD(String jsonFileName) {
        if(!jsonFileName.endsWith(".json"))
            jsonFileName += ".json";
        String jsonFilePath = env.getProperty("spring.hadoop.fsUri") + "/user/" + env.getProperty("app.hadoop.user") + "/demo/" + jsonFileName;
        log.info("load file from hadoop: " + jsonFilePath);
        return sparkSession.sparkContext().textFile(jsonFilePath, 1).toJavaRDD();
    }
}