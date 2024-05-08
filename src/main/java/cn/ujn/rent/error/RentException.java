package cn.ujn.rent.error;

public class RentException extends RuntimeException {

    private String errMessage;

    public RentException() {
    }

    public RentException(String message) {
        super(message);
        this.errMessage = message;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public static void cast(String message) {
        throw new RentException(message);
    }
}
