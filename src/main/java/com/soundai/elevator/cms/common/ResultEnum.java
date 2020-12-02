package com.soundai.elevator.cms.common;

public enum ResultEnum implements CommonResult{

    SUCCESS(0, "成功。"),

    PARAM_ERROR(1001,"参数验证失败。"),

    UNKNOWN_ERROR(9999,"未知错误。"),

    GET_TOKEN_ERROR(8000,"获取token出错。"),

    NO_TOKEN(8001,"没有token。"),

    NO_LOGIN(8002,"请登陆。"),

    UPLOAD_ERROR(8003,"上传通讯出错。"),

    INSERT_DATA_ERROR(8004,"插入数据出错。"),

    QUERY_DATA_ERROR(8005,"查询数据出错。"),

    DIND_DEVICE_ERROR(8006,"绑定设备sn出错。"),

    REQUEST_ERROR(8007,"调用服务器出错。"),

    TYPE_ERROR(8008,"文件类型不正确。"),

    ADD_TIME_ERROR(8009,"所选时间与已存在的任务时间冲突。"),

    GET_USER_ID(8009,"获取urrogate userId失败"),

    ADD_DEVICE_SN_ERROR(8010,"设备sn码重复添加。"),

    DEL_POWER_ERROR(8011,"权限不足，无法删除。"),

    POWER_ERROR(8012,"权限不足,无法操作。"),

    UPLOAD_ALREADY_ERROR(8013,"该图片已存在,无法上传。"),
    ;

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
