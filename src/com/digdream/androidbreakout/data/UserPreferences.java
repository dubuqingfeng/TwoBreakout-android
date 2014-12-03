package com.digdream.androidbreakout.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

/**
 * ���Ǳ����û��Ĺؿ������ؼ�¼���û�������Ϣ
 * �û�ͨ���Ĺؿ����ܷ֣����֣����˾���ģʽ�ٶ�
 * ʹ��SharePreferences
 * 
 * @author user
 *
 */
public class UserPreferences {
	//
	// �Զ���¼���û������ǳƣ����룬userid������ǩ��,ͷ��url
	private final String AUTO_LOGIN = "auto_login";
	private SharedPreferences userPreferences;
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private final String USERID = "";
	private final String NICKNAME = "";
	private final String SIGN = "";
	private final String ICON_HEAD = "";
	
	private final String USERLEVEL = "";
	private final String USERSCORE = "userscore";
	private final String USERVELOCITY = "uservelocity";

	/**
	 * ��ʼ��SharePreferences
	 */
	public void init(Context ctx) {
		if (null == userPreferences) {
			userPreferences = ctx.getSharedPreferences("breakoutuser",
					Context.MODE_PRIVATE);

		}
	}
	/**
	 * ͨ���Ĺؿ�
	 * @return
	 */
	public int getLevel(){
		int flevel = userPreferences.getInt(USERLEVEL , 0);
		return flevel;
	}
	
	public void saveLevel(int level){
		Editor editor = userPreferences.edit();
		//int flevel = level;
		editor.putInt(USERLEVEL, level);
		editor.commit();
	}
	/**
	 * �ܷ�
	 * @return
	 */
	public int getScore(){
		int fscore = userPreferences.getInt(USERSCORE, 0);
		return fscore;
	}

	public void saveScore(int score){
		Editor editor = userPreferences.edit();
		editor.putInt(USERSCORE , score);
		editor.commit();
	}
	/**
	 * ����
	 * @param name
	 */
	public void saveName(String name) {
		Editor editor = userPreferences.edit();
		String fName = new String(
				Base64.encode(name.getBytes(), Base64.DEFAULT));
		editor.putString(USERNAME, fName);
		editor.commit();
	}

	public String getName() {
		String fName = userPreferences.getString(USERNAME, "");
		return new String(Base64.decode(fName, Base64.DEFAULT));
	}
	/**
	 * ����ģʽ�ٶ�
	 * @return
	 */
	public int getOneVelocity(){
		int velocity = userPreferences.getInt(USERVELOCITY, 1);
		return velocity;
	}
	public void saveOneVelocity(int velocity){
		Editor editor = userPreferences.edit();
		editor.putInt(USERVELOCITY, velocity);
		editor.commit();
	}

	public void savePWD(String password) {
		Editor editor = userPreferences.edit();
		String fpwd = new String(Base64.encode(password.getBytes(),
				Base64.DEFAULT));
		editor.putString(PASSWORD, fpwd);
		editor.commit();
	}

	public String getPWD() {
		String fpwd = userPreferences.getString(PASSWORD, "");
		return new String(Base64.decode(fpwd, Base64.DEFAULT));
	}

	public void saveUID(String uid) {
		Editor editor = userPreferences.edit();
		editor.putString(USERID, uid);
		editor.commit();
	}

	public String getUID() {
		String uid = userPreferences.getString(USERID, "no");
		return uid;
	}

	public void setAutoLogin(boolean autoLogin) {
		Editor editor = userPreferences.edit();
		editor.putBoolean(AUTO_LOGIN, autoLogin);
		editor.commit();
	}

	public boolean getAutoLogin() {
		return userPreferences.getBoolean(AUTO_LOGIN, false);

	}

	public void saveNICKNAME(String nickname) {
		Editor editor = userPreferences.edit();
		editor.putString(NICKNAME, nickname);
		editor.commit();
	}

	public String getNICKNAME() {
		String nickname = userPreferences.getString(NICKNAME, "����һ���ǳ�");
		return nickname;
	}

	public void saveSIGN(String sign) {
		Editor editor = userPreferences.edit();
		editor.putString(SIGN, sign);
		editor.commit();
	}

	public String getSIGN() {
		String sign = userPreferences.getString(SIGN, "����һ��ǩ��");
		return sign;
	}

	public void saveICON_HEAD(String icon_head) {
		Editor editor = userPreferences.edit();
		editor.putString(ICON_HEAD, icon_head);
		editor.commit();
	}

	public String getICON_HEAD() {
		String icon_head = userPreferences.getString(ICON_HEAD, "");
		return icon_head;
	}

}