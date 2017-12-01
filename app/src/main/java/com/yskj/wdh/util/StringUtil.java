package com.yskj.wdh.util;

/**
 * 作者： 闄堝畽娲�
 * 时间： 2016/9/26.
 */
public class StringUtil {
    public static String getmess(String code){
        String mess = null;
        if(code.equals("1")){
            mess = "参数错误";
        }else if(code.equals("2")){
            mess = "删除收货地址失败";
        }else if(code.equals("3")){
            mess = "更新默认收货地址";
        }else if(code.equals("4")){
            mess = "获取收货地址失败";
        }else if(code.equals("5")){
            mess = "参数为空";
        }else if(code.equals("6")){
            mess = "库存为空";
        }else if(code.equals("7")){
            mess = "密钥错误";
        }else if(code.equals("8")){
            mess = "用户为空";
        }else if(code.equals("0")){
            mess = "操作成功";
        }
        return mess;
    }
    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) &&
                codePoint <= 0xD7FF))|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
