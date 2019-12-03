package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.Gson;
        import java.io.IOException;
        import okhttp3.ResponseBody;

/*
    Clase utilizada para mostrar errores de la API
*/
public class ApiError {

    private int status;
    private int code;
    private String message;
    private String moreInfo;
    private String developerMessage;

    public ApiError(int status, int code, String message, String moreInfo, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.moreInfo = moreInfo;
        this.developerMessage = developerMessage;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public static ApiError fromResponseBody(ResponseBody responseBody) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(responseBody.string(), ApiError.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
