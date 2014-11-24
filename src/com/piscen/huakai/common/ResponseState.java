/**
 * 
 */
package com.piscen.huakai.common;

/**
 * 
 * @author wu_zhang
 * @2014-11-24下午5:11:58
 * @TODO 返回code
 */
public class ResponseState {
	/** 请求成功 */
	public static final int SUCCESSED=000000;
	/** 数据为空 */
    public static final int DATANULL=200001;
    /**
     * 系统错误
     */
    public static final int SYSERROR=300002;
    /**
     * 参数错误
     */
    public static final int PARAMETERERROR=400003;
    /**
     * 用户名或密码不正确
     */
    public static final int USERINFOERROR=000001;
    /**
     * 用户不是检测员
     */
    public static final int USERINFONOTJIANCE=000002;
    /**
     * 用户被锁定
     */
    public static final int USERINFONOT=000003;
    /**
     * 无效用户
     */
    public static final int USERINFONOTUSE=000004;
    /**
     * 重新登录
     */
    public static final int LOGIAGAIN = 200000;
    /**
     * 检测不通过
     */
    public static final int MARK = 000006;
    
    public final static int DEPARTURE = 200002;//车辆离场

}
