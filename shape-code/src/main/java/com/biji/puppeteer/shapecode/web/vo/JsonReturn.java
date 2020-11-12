package com.biji.puppeteer.shapecode.web.vo;

import java.io.Serializable;

/**
 * 返回json类型
 * @author hjf
 * Date: 2016-4-13
 * Date: 上午10:53:30
 */
public class JsonReturn implements Serializable {
    /**
     * 成功标识
     */
    private static final String SUCCESS = "0";
    /**
     * 错误标识
     */
    private static final String ERROR = "-1";
    /**
     * 异常标识
     */
    private static final String EXCEPTION = "-2";
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5701060215880308437L;

    /**
     * 错误代码
     */
    private String            code;

    /**
     * 错误信息
     */
    private String            msg;

    /**
     * 数据
     */
    private Object            data;

    /**
     * 返回 错误代码
     * @return 错误代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置错误代码
     * @param code 错误代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 返回 错误信息
     * @return 错误信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置错误信息
     * @param msg 错误信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 返回 数据
     * @return 数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置数据
     * @param data 数据
     */
    public void setData(Object data) {
        this.data = data;
    }


    public static JsonReturn successInstance(Object data){
        JsonReturn jsonReturn = new JsonReturn();
        jsonReturn.setCode(SUCCESS);
        jsonReturn.setData(data);
        return jsonReturn;
    }


    public static JsonReturn successInstance(){
        JsonReturn jsonReturn = new JsonReturn();
        jsonReturn.setCode(SUCCESS);

        return jsonReturn;
    }

    public static JsonReturn errorInstance(String msg){
        JsonReturn jsonReturn = new JsonReturn();
        jsonReturn.setCode(ERROR);
        jsonReturn.setMsg(msg);
        return jsonReturn;
    }

    public static JsonReturn exceptionInstance(String msg){
        JsonReturn jsonReturn = new JsonReturn();
        jsonReturn.setCode(EXCEPTION);
        jsonReturn.setMsg(msg);
        return jsonReturn;
    }

    public static JsonReturn createFailureResult(String code) {
        return createFailureResult(code, "");
    }

    public static JsonReturn createFailureResult(String code, String msg) {
        JsonReturn result = new JsonReturn();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static JsonReturn createSuccessResult(String code) {
        return createSuccessResult(code, null);
    }

    public static JsonReturn createSuccessResult(String code, Object data) {
        JsonReturn result = new JsonReturn();
        result.setCode(code);
        result.setData(data);
        return result;
    }

}
