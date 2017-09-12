package kindle.pojo;

/**
 * @description 记住我
 * @author hely
 * @date 2017-09-01
 * @param
 */
public class Remember {

    private String id;
    private String invariableSeries;
    private String token;
    private String userName;
    private User user;

    public Remember() {
    }

    public Remember(String invariableSeries, String token, String userName, User user) {
        this.invariableSeries = invariableSeries;
        this.token = token;
        this.userName = userName;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInvariableSeries() {
        return invariableSeries;
    }

    public void setInvariableSeries(String invariableSeries) {
        this.invariableSeries = invariableSeries;
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
