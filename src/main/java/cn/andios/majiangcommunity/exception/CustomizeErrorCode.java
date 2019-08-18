package cn.andios.majiangcommunity.exception;

/**
 * @description:自定义异常
 * @author:LSD
 * @when:2019/7/21/21:21
 */
public enum CustomizeErrorCode implements  ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你要找的问题不在了，要不换个试试"),
    TARGET_PARAM_NOT_FOUND(2002,"未选择任何问题或内容进行回复"),
    NOT_LOGIN(2003,"请登录后再评论"),
    SYSTEM_ERROR(2004,"服务冒烟了，要不稍后试试"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"评论不存在"),
    CONTENT_IS_EMPTY(2007,"评论内容不能为空"),
    READ_NOTIFICATION_FAIL(2008,"兄弟，你这是读别人的消息呢!!!"),
    NOTIFICATION_NOT_FOUND(2009,"消息不见了"),
    PIC_UPLOAD_FAIL(2010,"图片上传失败");

    private String message;
    private Integer code;

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
