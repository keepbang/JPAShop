package jpabook.jpashop;

import lombok.Data;

@Data
public class ResultVO {
    public ResultCode result;
    public String message;

    public ResultVO(ResultCode result, String message){
        this.result = result;
        this.message = message;
    }
}
