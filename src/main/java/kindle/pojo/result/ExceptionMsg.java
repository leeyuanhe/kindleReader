
package kindle.pojo.result;

public enum ExceptionMsg {
	SUCCESS("004", "操作成功"),
	FAILED("005","操作失败"),
    ParamError("006", "参数错误！"),
    LoginNameOrPassWordError("003", "用户名或者密码错误！"),
    EmailUsed("001","该邮箱已被注册"),
    UserNameUsed("002","该登录名称已存在"),

    ;
   private ExceptionMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;
    
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}

    
}

