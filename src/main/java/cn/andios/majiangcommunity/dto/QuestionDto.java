package cn.andios.majiangcommunity.dto;

import cn.andios.majiangcommunity.model.User;
import lombok.Data;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/6/21:37
 */
@Data
public class QuestionDto {

    private Long id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;

    private User user;
}
