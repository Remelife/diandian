package cn.edu.scujcc.diandian;

public class Response {
    public final static int STATUS_OK = 1;
    public final static int STATUS_ERROR = 0;

    private int status;
    private String message;
    private User data;


    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public User getData() {
        return data;
    }
    public void setData(User data) {
        this.data = data;
    }
}
