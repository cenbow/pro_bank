package com.rongdu.p2psys.cf.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountLogService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 微信资金日志
 * @author Jinx
 *
 */
public class WechatAccountLogAction extends BaseAction<AccountLogModel> implements ModelDriven<AccountLogModel>{

	private Map<String,Object> data;
	
	@Resource
	private AccountLogService theAccountLogService;
	
	/**
	 * 我的设置 --页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/accountLog",results = { @Result(name = "account-log", type = "ftl", location = "/nb/cf/wechat/user/account-log.html")})
	public String setting(){
		return "account-log";
	}
	
	/**
	 * 微信资金日志 -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/accountLog")
	public void userAccountLog() throws IOException{
		User user = getNBSessionUser();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,400);
		data.put(ConstantUtil.MSG,"微信资金日志相关");
		if(null!=user){
			List<AccountLog> list = theAccountLogService.getByUserId(user.getUserId());
			data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
			data.put(ConstantUtil.DATA,list);
		}else{
			data.put(ConstantUtil.DATA,"用户登陆失效!");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
