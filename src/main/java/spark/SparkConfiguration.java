package spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author csieflyman
 */
@EnableConfigurationProperties(SparkConfigProperties.class)
@Configuration
public class SparkConfiguration {

    @Autowired
    private SparkConfigProperties sparkConfigProperties;

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName(sparkConfigProperties.getAppName())
                .setSparkHome(sparkConfigProperties.getHome())
                .setMaster(sparkConfigProperties.getMasterUri());
    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkSession sparkSession(){
        return SparkSession
                .builder()
                .sparkContext(javaSparkContext().sc())
                .appName(sparkConfigProperties.getAppName())
                .getOrCreate();
    }
}
