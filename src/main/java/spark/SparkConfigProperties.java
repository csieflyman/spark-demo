package spark;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author csieflyman
 */
@Setter
@Getter
@ConfigurationProperties("spark")
public class SparkConfigProperties {

    private String appName;

    private String home;

    private String masterUri;
}
