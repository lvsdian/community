package cn.andios.majiangcommunity.dto;

import lombok.Data;

/**
 * @description:
 * @author:LSD
 * @when:2019/6/11/15:32
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;

}
