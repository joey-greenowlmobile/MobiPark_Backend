package com.greenowl.callisto.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenowl.callisto.exception.ErrorResponse.Builder;
import com.greenowl.callisto.util.ApiUtil;
import org.joda.time.DateTime;

/**
 * Created by Ahmed on 2015-05-28.
 * simple POJO representation of a Server Error Response.
 */
public class ErrorResponse {

    private Long timestamp;

    private Integer status;

    private String error;

    private String exception;

    private String message;

    private String path;
    
    private Integer errorCode;
    
    private String apiVersion;

    @JsonIgnore
    private Builder builder;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public static Builder getBuidler() {
        return new Builder();
    }

    static class Builder {

        private ErrorResponse response;

        private Builder() {
            response = new ErrorResponse();
            response.setApiVersion(ApiUtil.getVersion().name());
        }

        public Builder message(String message) {
            response.setMessage(message);
            return this;
        }

        public Builder path(String path) {
        	String p = "/api/" + ApiUtil.getVersion().getValue() + path;
            response.setPath(p);
            return this;
        }

        public Builder error(String error) {
            response.setError(error);
            return this;
        }

        public Builder exception(String exception) {
            response.setException(exception);
            return this;
        }

        public Builder status(Integer status) {
            response.setStatus(status);
            return this;
        }

		public Builder errorCode(Integer errorCode) {
			response.setErrorCode(errorCode);
			return this;
		}

        public ErrorResponse build() {
            response.setTimestamp(DateTime.now().getMillis());
            return response;
        }



    }
}
