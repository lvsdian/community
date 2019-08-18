package cn.andios.majiangcommunity.enums;

public enum NotificationStatusEnum {

    UNREAD(0),
    READ(1);

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    NotificationStatusEnum(int status){
        this.status = status;
    }
}
