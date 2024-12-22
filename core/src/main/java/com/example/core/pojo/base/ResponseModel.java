package com.example.core.pojo.base;



import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@JsonIgnoreProperties({"resultObject"})
public class ResponseModel<T> implements Serializable {
    public static final int EXCEPTION = 1;
    public static final int PARAMER = 2;
    public static final int SUCCESS_CODE = 200;
    public static final int FAILURE_CODE = 500;
    public static final int FAILURE_AUTH = 401;
    private static final long serialVersionUID = -6071886404636157598L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseModel.class);
    private boolean success;
    private T data = null;
    @JsonIgnore
    private transient Optional<T> resultObject = Optional.empty();
    private ResponseModel<T>.Message message;
    private Object result;

    public ResponseModel() {
    }

    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Optional<T> getResultObject() {
        return this.resultObject;
    }

    public void setResultObject(Optional<T> resultObject) {
        this.resultObject = resultObject;
    }

    public ResponseModel<T>.Message getMessage() {
        return this.message;
    }

    public void setMessage(ResponseModel<T>.Message message) {
        this.message = message;
    }

    public String getMessageInfo() {
        return null != this.getMessage() ? this.getMessage().getMessage() : "";
    }

    public static <T> ResponseModel<T> getSuccess(T data) {
        return success(data);
    }

    public static <T> ResponseModel<List<T>> getSuccess(String message, Page<T> data) {
        ResponseModel<List<T>> responseResult = new ResponseModel();
        JSONObject rsMsg = new JSONObject();
        responseResult.setData(data.getContent());
        rsMsg.put("count", data.getTotalElements());
        rsMsg.put("totalPages", data.getTotalPages());
        rsMsg.put("totalElements", data.getTotalElements());
        rsMsg.put("last", data.isLast());
        rsMsg.put("size", data.getSize());
        rsMsg.put("number", data.getNumber());
        rsMsg.put("numberOfElements", data.getNumberOfElements());
        rsMsg.put("first", data.isFirst());
        rsMsg.put("sort", null != data.getSort() ? JSONObject.toJSONString(data.getSort()) : new JSONObject());
        responseResult.setResult(rsMsg);
        responseResult.setSuccess(true);
        responseResult.setMessage(responseResult.new Message(200, message));
        return responseResult;
    }

    public static <T> ResponseModel<Collection<T>> getSuccess(String message, Collection<T> data) {
        ResponseModel<Collection<T>> responseResult = new ResponseModel();
        JSONObject rsMsg = new JSONObject();
        responseResult.setData(data);
        rsMsg.put("count", data.size());
        responseResult.setResult(rsMsg);
        responseResult.setSuccess(true);
        responseResult.setMessage(responseResult.new Message(200, message));
        return responseResult;
    }

    public static <T> ResponseModel<T> getFailure(String message, T data) {
        return failure(500, message, data);
    }

    public static <T> ResponseModel<T> success(T data) {
        return success((String)null, data);
    }

    public static <T> ResponseModel success(String message, T data) {
        JSONObject rsMsg = new JSONObject();
        ResponseModel responseResult;
        if (data instanceof Page) {
            responseResult = getSuccess(message, (Page)data);
        } else if (data instanceof Collection) {
            responseResult = getSuccess(message, (Collection)data);
        } else {
            responseResult = new ResponseModel();
            if (null != data) {
                responseResult.setData(data);
                responseResult.setResultObject(Optional.of(data));
                rsMsg.put("count", data == null ? 0 : 1);
            } else {
                rsMsg.put("count", data == null ? 0 : 1);
            }

            responseResult.setResult(rsMsg);
            responseResult.setSuccess(true);
            responseResult.setMessage(responseResult.new Message(200, message));
        }

        return responseResult;
    }

    public static ResponseModel getFailure(String message) {
        return failure(500, message);
    }

    public static ResponseModel failure(String message) {
        return failure(500, message);
    }

    public static ResponseModel failure(Exception ex, String message) {
        return failure(500, ex.getMessage() + " " + message);
    }

    public static ResponseModel failure(JSONObject messageJson) {
        return failure(messageJson.getInteger("code"), messageJson.getString("message"));
    }

    public static ResponseModel failure(int code, String message) {
        return failure(code, message, (Object)null);
    }

    public static <T> ResponseModel failure(int code, String message, T data) {
        ResponseModel responseResult = new ResponseModel();
        responseResult.setData(data);
        responseResult.setSuccess(false);
        responseResult.setMessage(responseResult.new Message(code, message));
        JSONObject rsMsg = new JSONObject();
        rsMsg.put("count", 0);
        responseResult.setResult(rsMsg);
        return responseResult;
    }

    public static ResponseModel assertBindingResult(BindingResult bindingResult) {
        ResponseModel responseModel = success("");
        if (bindingResult.hasErrors()) {
            StringBuffer errorInfo = new StringBuffer();
            List<ObjectError> errors = bindingResult.getAllErrors();
            Iterator var4 = errors.iterator();

            while(var4.hasNext()) {
                ObjectError error = (ObjectError)var4.next();
                errorInfo.append(error.toString());
            }

            responseModel = getFailure(errorInfo.toString());
        }

        return responseModel;
    }

    public String toString() {
        String result = super.toString();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            result = mapper.writeValueAsString(this);
        } catch (IOException var3) {
            LOGGER.error("ResponseModel to String failure:", var3);
        }

        return result;
    }

    class Message {
        private int code;
        private String message;

        public Message() {
        }

        public Message(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return this.code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
