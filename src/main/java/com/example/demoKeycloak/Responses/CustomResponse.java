package com.example.demoKeycloak.Responses;


import lombok.Data;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class CustomResponse {
    private int statusCode;
    private String message;
    private Object errorMessages;
    private Object data;

    public static Builder getBuilder(){
        return new CustomResponse.Builder();
    }
    public ResponseEntity<CustomResponse>responseBuilder(){
        return ResponseEntity.status(this.statusCode).body(this);
    }

    public static class Builder{
        private int statusCode;
        private String message;
        private Object errorMessages;
        private Object data;

        public Builder(){}

        public Builder message(String message){
            this.message=message;
            return this;
        }
        public Builder statusCode(int statusCode){
            this.statusCode=statusCode;
            return this;
        }
        public Builder errorMessages(Object errorMessages){
            this.errorMessages=errorMessages;
            return this;
        }
        public Builder data(Object data){
            this.data=data;
            return this;
        }
        public CustomResponse build(){
            CustomResponse customResponse=new CustomResponse();

            customResponse.message=this.message;
            customResponse.errorMessages=this.errorMessages;
            customResponse.statusCode=this.statusCode;
            customResponse.data=this.data;

            return customResponse;
        }
        public ResponseEntity<CustomResponse> responseBuilder(){
            CustomResponse customResponse=this.build();
            return ResponseEntity.status(customResponse.statusCode).body(customResponse);
        }
    }

}
