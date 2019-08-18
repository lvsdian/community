package cn.andios.majiangcommunity.enums;

/**
 * @description:通知类型枚举
 * @author:LSD
 * @when:2019/7/29/21:05
 */
public enum  NotificationEnum {

    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论");

    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    NotificationEnum(int type, String name){
        this.type = type;
        this.name = name;
    }

    public static  String nameOfType(int type){
        for (NotificationEnum notificationEnum:NotificationEnum.values()){
            if(notificationEnum.getType() == type){
                return notificationEnum.getName();
            }
        }
        return "";
    }
}
