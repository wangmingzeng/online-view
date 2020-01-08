package com.zach.biz.view.mode;

/**
 * 返回码/状态码定义
 * @author
 * 
<code>
	0			表示成功
	-1			未知系统异常(Exception)
	
	1~1000		保留编码, 保留给公共服务使用(SSO )
	1~100		保留编码
	100 ~ 199	登录和SSO使用
	200 ~ 1000	保留编码
	401			token缺失或失效
</code>
 *
 * 
 *
 */
public class ReturnCode {
	
	/**未知系统异常*/
	public static final long UNKNOWN_SYSTEM_ERROR = -1;
	
	/**成功*/
	public static final long SUCCESS = 200;

	/**参数错误*/
	public static final long PARAMS_ERROR = 301;
	
	/**token缺失或异常*/
	public static final long TOKEN_EXCEPTION = 501;
	
	/**登录逻辑错误**/
	public static final long LOGIN_LOGIC_ERROR = 1000;
	
	/**数据已存在，重复**/
	public static final long DATA_REPEAT = 1001;
	
	/**数据异常*/
	public static final long DATA_EXCEPTION = 1002;
	
	/**业务逻辑错误*/
	public static final long BUSINESS_LOGIC_ERROR = 1003;

}
