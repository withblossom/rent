package cn.ujn.rent.utils;

import cn.ujn.rent.error.RentException;

public class Checker {

    public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    public static boolean isPhoneInvalid(String phone) {
        return mismatch(phone, PHONE_REGEX);
    }

    public static boolean isEmailInvalid(String email) {
        return mismatch(email, EMAIL_REGEX);
    }
    // 校验是否不符合正则格式
    private static boolean mismatch(String str, String regex) {
        return false;
//        return !StringUtils.hasLength(str) || !str.matches(regex);
    }
    public static void assertAdmin(){
        if (!UserHolder.getUser().getVipStatus()) {
            RentException.cast("无权访问！！！");
        }
    }
}
