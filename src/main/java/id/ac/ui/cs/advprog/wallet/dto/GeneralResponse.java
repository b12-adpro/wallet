package id.ac.ui.cs.advprog.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralResponse {
    private String status;
    private String message;
    private Object data;
    
    public static GeneralResponse from(Object data, String status, String message) {
        GeneralResponse response = new GeneralResponse();
        response.setData(data);
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }
}