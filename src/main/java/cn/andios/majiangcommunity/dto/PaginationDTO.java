package cn.andios.majiangcommunity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/8/0:03
 */
@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    /** 当前页 */
    private Integer page;
    /** 在页面显示的页码 */
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPages;

    public void setPagination(Integer totalPages, Integer page) {
        this.totalPages = totalPages;
        this.page = page;

        pages.add(page);

        for(int i = 1;i <3;i ++){
            if(page - i > 0){
                pages.add(0,page - i);
            }
            if(page + i <= totalPages){
                pages.add(page +i);
            }
        }

        //是否展示上一页
        if(page == 1){
            showPrevious = false;
        }else {
            showPrevious = true;
        }
        //是否展示下一页
        if(page.intValue() == totalPages.intValue()){
            showNext = false;
        }else {
            showNext = true;
        }
        //是否展示第一页
        if(pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }
        //是否展示最后一页
        if(pages.contains(totalPages)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }

    }

}
