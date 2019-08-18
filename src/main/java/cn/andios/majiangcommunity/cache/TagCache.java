package cn.andios.majiangcommunity.cache;

import cn.andios.majiangcommunity.dto.TagDto;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/28/10:42
 */
public class TagCache {
    public static List<TagDto> get(){
        List<TagDto> tagDtoLists = new ArrayList<>();

        TagDto program = new TagDto();
        program.setCategoryName("开发语言");
        program.setTagLists(Arrays.asList("javascript","php","css","html","html5","java","node.js","python","c++","c","golang","objective-c","typescript","shell","c#","swift","sass","bash","ruby","less","asp.net","lua","scala","coffeescript","actionscript","rust","erlang","perl"));

        TagDto framework = new TagDto();
        framework.setCategoryName("平台架构");
        framework.setTagLists(Arrays.asList("laravel","spring","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));

        TagDto server = new TagDto();
        server.setCategoryName("服务器");
        server.setTagLists(Arrays.asList("linux","nginx","docker","apache","ubuntu","centos","缓存","tomcat","负载均衡","unix","hadoop","windows-server"));

        TagDto db = new TagDto();
        db.setCategoryName("数据库及缓存");
        db.setTagLists(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql","memcached","sqlserver","postgresql","sqlite"));

        TagDto tool = new TagDto();
        tool.setCategoryName("开发工具");
        tool.setTagLists(Arrays.asList("git","github","visual-studio-code","vim","sublime-text","xcode","intellij-idea","eclipse","maven","ide","svn","visual-studio","atom","emacs","textmate","hg"));

        tagDtoLists.add(program);
        tagDtoLists.add(framework);
        tagDtoLists.add(server);
        tagDtoLists.add(db);
        tagDtoLists.add(tool);

        return tagDtoLists;
    }
    public static String filterInvalid(String tags){
        //将传过来的标签根据,分割为一个数组
        String [] split = StringUtils.split(tags,",");
        //得到所有的系统标签
        List<TagDto> tagDtoLists = get();
        //将系统标签转为一个list
        List<String> tagsLists = tagDtoLists.stream().flatMap(tag -> tag.getTagLists().stream()).collect(Collectors.toList());
        //根据系统标签过滤用户输入的标签
        String invalid = Arrays.stream(split).filter(t -> !tagsLists.contains(t)).collect(Collectors.joining(","));
        //将不一致的标签返回
        return invalid;
    }
}
