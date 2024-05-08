package cn.ujn.rent.utils;

public class SystemConstants {
    public static final String IMAGE_PATH = "F:/BaiduNetdiskDownload/Java_project/PROJECT/rent/target/classes/images/";

    public static final String USER_CACHE_SUFFIX = "rent-user-";

    public static final String HOUSE_CACHE_SUFFIX = "rent-house-";

    public static final String RENTINFO_CACHE_SUFFIX = "rent-rentinfo-";

    public static final String COMMENT_CACHE_SUFFIX = "rent-comment-";

    public static final String TOKEN_USER_SUFFIX = "rent-token-user-";

    public enum TimeUnitEnum {
        DAYUNIT("天"), WEEKUNIT("周"),
        MONTHUNIT("月"), YEARUNIT("年"),
        FOREVERUNIT("永久");
        String word;

        TimeUnitEnum(String word) {
            this.word = word;
        }
    }

    public enum HouseState {
        FREE("空闲"), RENTED("已租出"),BOUGHT("已售出");
        String word;

        HouseState(String word) {
            this.word = word;
        }
    }

    public enum RentState {
        RUNNING("进行中"), ABBOTED("异常"), COMPLETED("完成");
        String word;

        RentState(String word) {
            this.word = word;
        }
    }
    public enum TradingMode {
        RENTING("出租"), BUYING("出售");
        String word;

        TradingMode(String word) {
            this.word = word;
        }
    }

}
