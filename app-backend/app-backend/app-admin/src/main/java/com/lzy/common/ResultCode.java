package com.lzy.common;

/**
 * API返回码封装类
 * Created by macro on 2019/4/19.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败！"),
    UNAUTHORIZED(401, "暂未登录或token已经过期！"),
    FORBIDDEN(403, "没有相关权限！"),
    NOTEXIST(404, "不存在该用户,请注册！"),
    ERROR_USER_OR_PWD(500,"用戶或密碼錯誤！"),
    MOVE_ERROR(501,"无法移动到子文件夹中！");

    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
