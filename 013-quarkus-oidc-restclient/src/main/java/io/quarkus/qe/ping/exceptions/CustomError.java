package io.quarkus.qe.ping.exceptions;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class CustomError {
    private int code;
    private String msg;

    private CustomError(Builder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
    }

    public CustomError() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Builder {

        private int code;
        private String msg;

        public Builder() {
        }

        public Builder withCode(int code) {
            this.code = code;
            return this;
        }

        public Builder withMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public CustomError build() {
            CustomError customError = new CustomError(this);
            return customError;
        }
    }

}
