package cn.andios.majiangcommunity.dto;

import lombok.Data;

/**
 * @description:
 * @author:LSD
 * @when:2019/6/11/15:10
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
