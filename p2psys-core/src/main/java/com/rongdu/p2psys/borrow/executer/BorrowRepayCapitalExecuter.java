package com.rongdu.p2psys.borrow.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;

/**
 * 标还款扣除本金
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowRepayCapitalExecuter extends BaseExecuter {

	private String accountLogType = Constant.REPAID_CAPITAL;
	private String sumLogRemarkTemplate = "已还款合计：已还款本金${money}元，标ID：[${borrow.id}]。";

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		super.prepare();
	}

	@Override
	public void addAccountLog() {
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, super.toUser);
		log.setMoney(super.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setRemark(this.getLogRemark());
		log.setPaymentsType((byte)2);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
	    //标准版还款处理，不需要冻结资金 by zhangyz
		//accountDao.modify(-super.money, 0, -super.money, 0,-super.money,super.user.getUserId());
	    accountDao.modify(-super.money, -super.money, 0, 0, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		this.sumManage();
		accountSumDao.update(EnumAccountSumProperty.REPAY_CASH.getValue(), super.money, super.user.getUserId());

		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, super.toUser);
		sumLog.setMoney(super.money);
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		sumLog.setBeforeMoney(sum.getRepayCash());
		sumLog.setRemark(getAccountSumLogRemark());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getRepayCash(), super.money));
		accountSumLogDao.save(sumLog);

	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOperateLog() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInterface() {
		// TODO Auto-generated method stub

	}

	@Override
	public void extend() {
		// TODO Auto-generated method stub

	}
}
