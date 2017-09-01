package kindle.pojo;

/**
 * @description 记住我
 * @author hely
 * @date 2017-09-01
 * @param
 */
public class Remember {

    private String uuid;
    private User user;

    public Remember() {
    }

    public Remember(String uuid, User user) {
        this.uuid = uuid;
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
}
