package base.dto;

import lombok.Getter;

/**
 * @author csieflyman
 */
public enum BaseResponseCode implements ResponseCode{

    SUCCESS(200, "0000", false),
    INTERNAL_SERVER_ERROR(500, "9999", true),
    REQUEST_BAD_REQUEST(400, "1000", false);

    @Getter
    private int statusCode;
    @Getter
    private String code;
    @Getter
    private String message;
    @Getter
    private boolean logError;

    BaseResponseCode(int statusCode, String code, boolean logError){
        this.statusCode = statusCode;
        this.code = code;
        this.message = name();
        this.logError = logError;
    }
}
