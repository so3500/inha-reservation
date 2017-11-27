package com.landvibe.kian82.schedule;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.inhareservation.R;
import com.landvibe.kian82.common.RangeSeekBar;

public class ScheduleListAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	List<JSONObject> scheduleList;
	int layout;

	public ScheduleListAdapter(Context _context, int _layout, List<JSONObject> object) {
		context = _context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = _layout;
		scheduleList = object;
	}

	@Override
	public int getCount() {
		return scheduleList.size();
	}

	@Override
	public Object getItem(int position) {
		return scheduleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// TODO Auto-generated method stub
		ViewHolder view_holder;

		if (convertView == null) {
			convertView = inflater.inflate(layout, parent, false);
			view_holder = new ViewHolder();
			view_holder.name = (TextView) convertView.findViewById(R.id.schedule_item_name_text);
			view_holder.rLayout = (RelativeLayout) convertView.findViewById(R.id.schedule_item_schedule_layout);
			convertView.setTag(view_holder);
		} else {
			view_holder = (ViewHolder) convertView.getTag();
		}

		JSONObject schedule = scheduleList.get(position);
		try {
			view_holder.name.setText(schedule.getString("name"));

			RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(context);
			rangeSeekBar.setRangeValues(15, 90);
			rangeSeekBar.setSelectedMinValue(30);
			rangeSeekBar.setSelectedMaxValue(50);
			view_holder.rLayout.addView(rangeSeekBar);
		} catch (Exception e) {
		}
		return convertView;
	}

	private static class ViewHolder {

		TextView name;
		RelativeLayout rLayout;
	}
}
