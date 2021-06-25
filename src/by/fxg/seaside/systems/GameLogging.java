package by.fxg.seaside.systems;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.fxg.seaside.utils.Utils;

public class GameLogging {
	public static GameLogging instance;
	private List<String> logChat = new ArrayList<String>();
	private List<String> logJob = new ArrayList<String>();
	private List<String> logRadio = new ArrayList<String>();
	private List<String> logVip = new ArrayList<String>();
	private List<String> logReport = new ArrayList<String>();
	
	private List<String> logMoneyTransfer = new ArrayList<String>();
	private List<String> logItemTransfer = new ArrayList<String>();
	private List<String> logVehicleTransfer = new ArrayList<String>();
	private List<String> logKillsInfo = new ArrayList<String>();
	private List<String> logFractionInfo = new ArrayList<String>();
	private List<String> logCmd = new ArrayList<String>();
	
	public GameLogging() {
		instance = this;
	}
	
	public void logChat(String par1) {
		this.logChat.add(Utils.getTimeStamp() + par1);
	}
	
	public void logJob(String par1) {
		this.logJob.add(Utils.getTimeStamp() + par1);
	}

	public void logRadio(String par1) {
		this.logRadio.add(Utils.getTimeStamp() + par1);
	}

	public void logVip(String par1) {
		this.logVip.add(Utils.getTimeStamp() + par1);
	}

	public void logReport(String par1) {
		this.logReport.add(Utils.getTimeStamp() + par1);
	}

	public void logMoneyTransfer(String par1) {
		this.logMoneyTransfer.add(Utils.getTimeStamp() + par1);
	}

	public void logItemTransfer(String par1) {
		this.logItemTransfer.add(Utils.getTimeStamp() + par1);
	}

	public void logVehicleTransfer(String par1) {
		this.logVehicleTransfer.add(Utils.getTimeStamp() + par1);
	}

	public void logKillsInfo(String par1) {
		this.logKillsInfo.add(Utils.getTimeStamp() + par1);
	}

	public void logFractionInfo(String par1) {
		this.logFractionInfo.add(Utils.getTimeStamp() + par1);
	}

	public void logCmd(String par1) {
		this.logCmd.add(Utils.getTimeStamp() + par1);
	}
	
	public static enum LogReason {
		MONEY_ADD,
		MONEY_REMOVE,
		MONEY_SET,
		MONEY_BANK_ADD,
		MONEY_BANK_REMOVE,
		MONEY_BANK_SET,
		MONEY_DEPOSIT_ADD,
		MONEY_DEPOSIT_REMOVE,
		MONEY_DEPOSIT_SET,
		
		PASSPORT_GET,
		PASSPORT_LOST,
		
		BUSINESS_BUY,
		BUSINESS_SELL,
		BUSINESS_LOST,
		
		HOUSE_BUY,
		HOUSE_SELL,
		HOUSE_LOST,
		HOUSE_UPGRADE,
		
		VEHICLE_BUY,
		VEHICLE_SELL,
		VEHICLE_LOST,
		
		ITEM_BUY,
		ITEM_SELL,
		ITEM_USE,
		ITEM_DROP,
		ITEM_LOST,
		
		FAMILY_CREATE,
		FAMILY_EDIT,
		FAMILY_DELETE,
		FAMILY_SELL,
		FAMILY_INVITE,
		FAMILY_RANK_ADD,
		FAMILY_RANK_REMOVE,
		FAMILY_RANK_EDIT,
		FAMILY_BRAND_CREATE,
		FAMILY_BRAND_EDIT,
		FAMILY_BRAND_DELETE,
		
		MARKET_RENT,
		MARKET_LOST,
		MARKET_BUY,
		MARKET_SELL,
		
		
	}
}
