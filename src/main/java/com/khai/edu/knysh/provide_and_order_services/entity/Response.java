package com.khai.edu.knysh.provide_and_order_services.entity;

import java.util.Objects;

public class Response {

    private Object body;
    private String message;
    private boolean error = false;

    public Response() {
    }

    public Response(Object body, String message) {
        this.body = body;
        this.message = message;
    }

    public Response(Object body, String message, boolean error) {
        this.body = body;
        this.message = message;
        this.error = error;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response response = (Response) o;
        return error == response.error && Objects.equals(body, response.body) && Objects.equals(message, response.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, message, error);
    }

    @Override
    public String toString() {
        return "Response{" +
                "body=" + body +
                ", message='" + message + '\'' +
                ", error=" + error +
                '}';
    }
}
