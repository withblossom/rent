package cn.ujn.rent.utils;

import cn.ujn.rent.bean.User;
import lombok.Data;
import lombok.Getter;

public class TokenUtil {

    public static String encrypt2Token(User user) {
        return user.getId()+"-"+user.getPhoneNumber();
    }
    public static Token decryptToken(String token) {
        String[] strings = token.split("-");
        Token tokenObj = new Token();
        tokenObj.id = Integer.parseInt(strings[0]);
        tokenObj.phoneNumber = strings[1];
        return tokenObj;
    }
    public static class Token{
        Integer id;
        String phoneNumber;
    }
}
