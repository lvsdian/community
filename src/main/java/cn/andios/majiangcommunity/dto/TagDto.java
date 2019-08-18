package cn.andios.majiangcommunity.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/28/10:42
 */
@Data
public class TagDto {
    private String categoryName;
    private List<String> tagLists;
}
