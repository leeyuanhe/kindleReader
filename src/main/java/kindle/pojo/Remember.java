package kindle.pojo;

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


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
