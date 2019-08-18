package cn.andios.majiangcommunity.dto;

import cn.andios.majiangcommunity.exception.CustomizeErrorCode;
import cn.andios.majiangcommunity.exception.CustomizeException;
import lombok.Data;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/22/19:56
 */
@Data
public class ResultDto<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDto error(Integer code,String  message){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }

    public static ResultDto error(CustomizeErrorCode errorCode) {
        return error(errorCode.getCode(),errorCode.getMessage());
    }
    public static ResultDto error(CustomizeException customizeException) {
        return error(customizeException.getCode(),customizeException.getMessage());
    }

    public static ResultDto success(){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");

        return resultDto;
    }

    public static <T> ResultDto successOf(T t){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setData(t);

        return resultDto;
    }
}
