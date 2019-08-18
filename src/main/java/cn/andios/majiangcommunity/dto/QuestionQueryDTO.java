package cn.andios.majiangcommunity.dto;

import lombok.Data;

/**
 * @description:
 * @author:LSD
 * @when:2019/8/1/10:09
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
