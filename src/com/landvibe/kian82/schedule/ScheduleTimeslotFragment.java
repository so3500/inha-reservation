package com.landvibe.kian82.schedule;

///*
// * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.inhareservation.R;
import com.landvibe.kian82.common.RangeSeekBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ScheduleTimeslotFragment extends Fragment {
	String[] str = { "Soccer", "BasketBall", "MainBuilding", "StudentBuilding", "Etc" };
	private static final String ARG_POSITION = "position";
	private static final String ARG_DATE = "date";
	LinearLayout temp_layout=null;
	LinearLayout temp_layout_holder=null;
	ToggleButton temp_toggle = null;

	public static ScheduleTimeslotFragment newInstance(int position, String dateString) {
		ScheduleTimeslotFragment fragment = new ScheduleTimeslotFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		b.putString(ARG_DATE, dateString);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		final ScrollView scroll_layout = (ScrollView) inflater.inflate(R.layout.category_common, container, false);
		final LinearLayout layout = (LinearLayout) scroll_layout.findViewById(R.id.category_layout);
		final Map<Integer, List<JSONObject>> scheduleMap = new HashMap<Integer, List<JSONObject>>();
		 final Boolean isExtend[];
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("rday", getArguments().getString(ARG_DATE));
		params.put("fcategory", getArguments().getInt(ARG_POSITION) + 1);
		client.get("http://oursoccer.co.kr/study/reserve/timeslot2.php", params, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				for (int i = 0; i < response.length(); i++) {
					try {
						if( response.getJSONObject(i).getInt("f_category") == getArguments().getInt(ARG_POSITION) + 1)
						{
						try {
							int facilityId = response.getJSONObject(i).getInt("f_id");
							if (scheduleMap.containsKey(facilityId)) {
								List<JSONObject> scheduleList = scheduleMap.get(facilityId);
								scheduleList.add(response.getJSONObject(i));
								scheduleMap.put(facilityId, scheduleList);
							} else {
								List<JSONObject> scheduleList = new ArrayList<JSONObject>();
								scheduleList.add(response.getJSONObject(i));
								scheduleMap.put(facilityId, scheduleList);
							}
						} catch (Exception e) {
						}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

				for (Integer facilityId : scheduleMap.keySet()) {
					
					final LinearLayout scheduleRow = (LinearLayout) inflater.inflate(R.layout.category_item, container, false);
					final RelativeLayout scheduleLayout = (RelativeLayout) scheduleRow.findViewById(R.id.category_item_schedule_layout);
					final LinearLayout scheduleLayout_holder = (LinearLayout) scheduleRow.findViewById(R.id.category_item_schedule_layout_holder);
					final ToggleButton nameText = (ToggleButton) scheduleRow.findViewById(R.id.category_item_f_name_text);
					nameText.setTextOn(null);
					nameText.setTextOff(null);
					nameText.setId(facilityId);
					nameText.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							if(nameText.isChecked())
							{
							 LinearLayout layout2 = new LinearLayout(getActivity());
							 LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						
						   	layout2.setLayoutParams(params);
							layout2.setOrientation(LinearLayout.VERTICAL);
							
							if(nameText != temp_toggle)
							{
								
							 if(temp_layout != null && temp_layout_holder != null) // 기존의 값이 뿌려져 있다면 
							 {
							 temp_layout_holder.removeView(temp_layout);  // 기존의 뷰를 지우고
							 
							 temp_toggle.setChecked(false);                     // 토글 버튼 false 지정
							 }
							}
							 
					     	PrintList.print(getActivity(), layout2, getArguments().getString(ARG_DATE),nameText.getId());      //리스트 뿌리고
					     	temp_layout = layout2;  // 그 레이아웃을 임
					     	temp_layout_holder = scheduleLayout_holder;
					     	temp_toggle = nameText;
							
							scheduleLayout_holder.addView(layout2);
							}
							else
							{
								 if(temp_layout != null && temp_layout_holder != null)
									 temp_layout_holder.removeView(temp_layout);
						          }
							
							
							
						}
					});
					

					List<JSONObject> scheduleList = scheduleMap.get(facilityId);
					for (JSONObject schedule : scheduleList) {
						try {
							nameText.setText(schedule.getString("f_name"));
							RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(getActivity(),false);
							rangeSeekBar.setRangeValues(0, 100);
							if(schedule.getInt("end_time_index")!=0)
							{
							rangeSeekBar.setSelectedMinValue(schedule.getInt("start_time_index")+8);
							rangeSeekBar.setSelectedMaxValue(schedule.getInt("end_time_index")+8); // 9에서 8로
							}
							else
							{
								rangeSeekBar.setSelectedMinValue(0);
								rangeSeekBar.setSelectedMaxValue(0);
							}
							scheduleLayout.addView(rangeSeekBar);
						} catch (Exception e) {
						}
					}
					layout.addView(scheduleRow);
				}
			}

		});
		return scroll_layout;
	}
}
