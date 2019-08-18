package cn.andios.majiangcommunity.dto;

import lombok.Data;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/22/17:20
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
