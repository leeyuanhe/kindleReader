package kindle.pojo;

import javax.persistence.*;

/**
 * @description 记住我
 * @author hely
 * @date 2017-09-01
 * @param
 */
public class Remember extends BaseEntity{


    private String invariableSeries;
    private String token;
    private Integer userId;

    @Transient
    private User user;

    public Remember() {
    }
    public Remember(String invariableSeries, String token, Integer userId) {
        this.invariableSeries = invariableSeries;
        this.token = token;
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInvariableSeries() {
        return invariableSeries;
    }

    public void setInvariableSeries(String invariableSeries) {
        this.invariableSeries = invariableSeries;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
