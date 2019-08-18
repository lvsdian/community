package cn.andios.majiangcommunity.dto;

import cn.andios.majiangcommunity.model.User;
import lombok.Data;

/**
 * @description:通知消息
 * @author:LSD
 * @when:2019/7/30/9:23
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    /** 这个属性拼错了 */
    private Long nofifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;

}
