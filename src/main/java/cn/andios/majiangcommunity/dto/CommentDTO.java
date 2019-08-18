package cn.andios.majiangcommunity.dto;

import cn.andios.majiangcommunity.model.User;
import lombok.Data;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/23/16:21
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
    private Integer commentCount;
}
