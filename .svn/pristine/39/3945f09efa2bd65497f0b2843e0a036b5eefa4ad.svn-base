package com.landvibe.kian82.login;

import com.landvibe.kian82.setting.SetupActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginUtils {
	public static boolean isLoggedIn(Context context) {
		if (getSeq(context) > 0) {
			return true;
		}
		return false;
	}

	public static void storeSeq(Context context, int seq, String name) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("s_seq", seq);
		editor.putString("s_name", name);
		editor.commit();
	}

	public static void setautocheck(Context context, boolean b) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("bool", b);
	}

	public static boolean getautocheck(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getBoolean("bool", SetupActivity.autoc);
	}

	public static int getSeq(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getInt("s_seq", -1);
	}

	public static String getName(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("s_name", "");
	}
	public static void removeautocheck(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove("bool");
		editor.commit();
	}
	public static void removeSeqName(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove("s_seq");
		editor.remove("s_name");
		editor.commit();
	}
}
