package kindle.pojo;

/**
 * @description 记住我
 * @author hely
 * @date 2017-09-01
 * @param
 */
public class Remember {

    private String uuid;
    private String token;
    private User user;

    public Remember() {
    }

    public Remember(String uuid,String token, User user) {
        this.uuid = uuid;
        this.token = token;
        this.user = user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
