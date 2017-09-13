
package kindle.pojo.result;

public enum ExceptionMsg {


    EmailUsed("001","该邮箱已被注册"),
    UserNameUsed("002","该登录名称已存在"),
    LoginNameOrPassWordError("003", "用户名或者密码错误！"),
    SUCCESS("004", "操作成功"),
    FAILED("005","操作失败"),
    LoginNameNotExist("006","该用户名不存在"),
    ParamError("007", "参数错误！"),
    UserLocked("008","用户已锁定"),
    IncorrectAuthOverBoundary("009","错误次数过多")

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

