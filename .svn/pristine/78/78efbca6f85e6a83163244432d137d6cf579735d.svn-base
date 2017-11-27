/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.landvibe.kian82.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.landvibe.kian82.common.MenuActivity;
import com.landvibe.kian82.common.PagerSlidingTabStrip;

public class ScheduleActivity extends FragmentActivity {
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private SchedulePagerAdapter adapter;

	private TextView dateText;
	private Calendar calendar;
	private String r_day = null;
	private String r_day_set[];
	private ImageButton goToMenu_btn;
	private int currentColor = 0xFF666666;
    private ImageView date_left_arrow;
    private ImageView date_right_arrow;
    int tab_position;
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		Intent intent = getIntent();
		
		r_day = intent.getStringExtra("r_day");
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new SchedulePagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		
		goToMenu_btn = (ImageButton) findViewById(R.id.schedule_home_btn);
		goToMenu_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					v.startAnimation(AnimationUtils.loadAnimation(ScheduleActivity.this, R.anim.button_click));
					v.startAnimation(AnimationUtils.loadAnimation(ScheduleActivity.this, R.anim.button_click2));
					Intent myIntent = new Intent(ScheduleActivity.this, MenuActivity.class);
					startActivity(myIntent);
					finish();
					overridePendingTransition(R.anim.slide_in_from_buttom, R.anim.slide_out_to_top);
				}
		});
		
		date_left_arrow = (ImageView)findViewById(R.id.schedule_left_arrow);
		date_right_arrow = (ImageView)findViewById(R.id.schedule_right_arrow);
		
		date_left_arrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tab_position = pager.getCurrentItem();
				v.startAnimation(AnimationUtils.loadAnimation(ScheduleActivity.this, R.anim.button_click));
				v.startAnimation(AnimationUtils.loadAnimation(ScheduleActivity.this, R.anim.button_click2));
				 calendar.add(Calendar.DAY_OF_YEAR, -1);
				 updateDateText();
			}
		});
		
		date_right_arrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tab_position = pager.getCurrentItem();
				v.startAnimation(AnimationUtils.loadAnimation(ScheduleActivity.this, R.anim.button_click));
				v.startAnimation(AnimationUtils.loadAnimation(ScheduleActivity.this, R.anim.button_click2));
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				 updateDateText();
			
			}
		});
		
		dateText = (TextView) findViewById(R.id.myReservation_date_tv);
		dateText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click));
				v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click2));
				new DatePickerDialog(ScheduleActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		if(intent.getStringExtra("r_day") == null)
		{
			calendar = Calendar.getInstance();
			updateDateText();
		}
		else
		{
			
			r_day_set = r_day.split("-");
			
			calendar = Calendar.getInstance();
			calendar.set(Integer.parseInt(r_day_set[0],10), Integer.parseInt(r_day_set[1],10)-1, Integer.parseInt(r_day_set[2],10));
			updateDateText();
		}
//		r_day_set = r_day.split("-");
//		calendar.set(Integer.parseInt(r_day_set[0]),Integer.parseInt(r_day_set[1]),Integer.parseInt(r_day_set[2]));
//		Toast.makeText(ScheduleActivity.this,Integer.parseInt(r_day_set[0]) + Integer.parseInt(r_day_set[1]) + Integer.parseInt(r_day_set[2]), Toast.LENGTH_LONG).show();
	
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
			updateDateText();
		}
	};

		private void updateDateText() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		dateText.setText(dateFormat.format(calendar.getTime()));
		adapter = new SchedulePagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setCurrentItem(tab_position);
	}

	public class SchedulePagerAdapter extends FragmentPagerAdapter {
		private final String[] TITLES = { "축구장", "농구장", "학생회관", "본관", "기타" };

		public SchedulePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return ScheduleTimeslotFragment.newInstance(position, dateText.getText().toString());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);

			FragmentManager manager = ((Fragment) object).getFragmentManager();
			FragmentTransaction trans = manager.beginTransaction();
			trans.remove((Fragment) object);
			trans.commit();
		}

	}
	
	@Override

	public void onBackPressed() {

	    // 여기에 코드 입력
		
		finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

	}

}