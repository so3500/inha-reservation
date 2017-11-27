package com.landvibe.kian82.schedule;

import java.util.zip.Inflater;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PrintList {
	static String URL;
	static AsyncHttpClient client = new AsyncHttpClient();
	static RequestParams params = new RequestParams();
	static JSONObject obj;
	static String str = "", startStr, endStr;
	static int start, end;
	public static void print(final Context context, final LinearLayout layout, String r_day, int f_id)
	{
		
		ArrayAdapter<?> startTimeAdapter = ArrayAdapter.createFromResource(context, R.array.startTimeList, R.layout.spinner_item);
		ArrayAdapter<?> endTimeAdapter = ArrayAdapter.createFromResource(context, R.array.endTimeList, R.layout.spinner_item);
		startTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		endTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		final Spinner startSpinner = new Spinner(context);
		final Spinner endSpinner = new Spinner(context);
		final LinearLayout layout_total = new LinearLayout(context);
		 final LinearLayout layout_column_left = new LinearLayout(context);
		 final LinearLayout layout_column_right = new LinearLayout(context);
		 final View divide_column = new View(context);
		 divide_column.setLayoutParams(new LinearLayout.LayoutParams(8,LayoutParams.MATCH_PARENT ));
		 divide_column.setBackgroundColor(Color.argb(0xFF, 0xCE, 0xE3, 0xF6));
		 layout_total.setOrientation(LinearLayout.HORIZONTAL);
		 LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			layoutParams.setMargins(30, 20, 30, 20);
		 
		 layout_total.setLayoutParams(layoutParams);
		 layout_column_left.setOrientation(LinearLayout.VERTICAL);
		 layout_column_left.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1f));
		 layout_column_right.setOrientation(LinearLayout.VERTICAL);
		 layout_column_right.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1f));
		startSpinner.setAdapter(startTimeAdapter);
		endSpinner.setAdapter(endTimeAdapter);
		
		
		
		str = "";
		URL = "http://oursoccer.co.kr/study/reserve/getReservation.php";
		params.put("r_day", r_day);
		params.put("f_id", f_id);
		client.get(URL, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				try {
					if (response.length() == 0)
					{
						TextView empty_tv = new TextView(context);
						empty_tv.setText("예약이 없습니다");
						empty_tv.setTextColor(Color.WHITE);
						empty_tv.setGravity(Gravity.CENTER);
						layout_column_left.addView(empty_tv);
					}
					else
					{
						
				
					for (int i = 0; i < response.length(); i++)
					{
						
						 TextView cname_tv = new TextView(context);
						 TextView event_tv = new TextView(context);
						 TextView time_tv = new TextView(context);
						 
	
						 final LinearLayout layout_row = new LinearLayout(context);
						 final LinearLayout layout_name_event = new LinearLayout(context);
						 View divide_line = new View(context);
						 View divide_line2 = new View(context);
						 View divide_line3 = new View(context);
						
						
						 
						 layout_name_event.setOrientation(LinearLayout.VERTICAL);
						 layout_name_event.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 2f));
						obj = response.getJSONObject(i);
						event_tv.setText(obj.getString("event"));
						cname_tv.setText(obj.getString("c_name"));
						start = obj.getInt("start_time_index");
						end = (obj.getInt("end_time_index")-1);
						time_tv.setText(startSpinner.getItemAtPosition(++start).toString() + " ~ " + endSpinner.getItemAtPosition(++end).toString());
						event_tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
						cname_tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
						time_tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
						event_tv.setTextSize(10);
						cname_tv.setTextSize(10);
						time_tv.setTextSize(14);
						cname_tv.setGravity(Gravity.CENTER);
						event_tv.setGravity(Gravity.CENTER);
						time_tv.setGravity(Gravity.CENTER);
						layout_name_event.setGravity(Gravity.CENTER);
						layout_name_event.addView(cname_tv);
						divide_line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2));
						divide_line.setBackgroundColor(Color.argb(0xFF, 0x2c, 0x3e, 0x50));
						layout_name_event.addView(divide_line);
						layout_name_event.addView(event_tv);
						
						divide_line2.setLayoutParams(new LinearLayout.LayoutParams(2,LayoutParams.MATCH_PARENT ));
						divide_line2.setBackgroundColor(Color.argb(0xFF, 0x2c, 0x3e, 0x50));
						layout_row.addView(layout_name_event);
						layout_row.addView(divide_line2);
						layout_row.addView(time_tv);
						layout_row.setBackgroundColor(Color.WHITE);
						
						divide_line3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 7));
						divide_line3.setBackgroundColor(Color.argb(0xFF, 0x2c, 0x3e, 0x50));
						layout_column_left.addView(layout_row);
						layout_column_left.addView(divide_line3);
					}
					}
					layout_total.addView(layout_column_left);
					layout.addView(layout_total);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
//				Toast.makeText(this, responseString, Toast.LENGTH_LONG).show();
//				Log.i(tag, Integer.toString(statusCode));
			}
		});
	}
}
