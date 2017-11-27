package com.landvibe.kian82.group_reservation;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.inhareservation.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class GroupReservationFragment extends Fragment {
	private int position;
	private String dateYear;
	private String groupName;
	private SyncHttpClient client_select = new SyncHttpClient();
	private String URL_select = "http://oursoccer.co.kr/study/reserve/Group_SelectGroupReserve.php";
	private RequestParams params_select = new RequestParams();
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private String student_name;
	private String[] dateArray = { "01", "02", "03", "04", "05", "06", "07","08", "09", "10", "11", "12" };
	public static GroupReservationAdapter adapter_groupreservation;
	public ListView listview;
	TextView noreserve_tv;
	GetJsonArrayThread getJsonArrayThread;
	Resources resources;
	

	// frgmant_Group �� �ǵ����ִ� �Լ� like ������
	public static GroupReservationFragment create( int position,String dateYear, String groupName,String student_name) {
		GroupReservationFragment frg_month = new GroupReservationFragment();
		Bundle b = new Bundle();
		b.putInt("position", position);
		b.putString("dateYear", dateYear);
		b.putString("groupName", groupName);
		b.putString("student_name", student_name);
		frg_month.setArguments(b);
		return frg_month;
	}

	/*
	 * class viewHolder { LinearLayout list_Layout; TextView reserve_Date;
	 * TextView facility_Name; }
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.month_1); �̰� �Ƚ���
		// �����׸�Ʈ�� onCreate����, create �Լ����� �޾Ƶξ��� ������ �����ͼ�, �������� ������ ���� ������ ����
		this.position = this.getArguments().getInt("position"); // ���� index ��
		this.dateYear = this.getArguments().getString("dateYear");
		this.groupName = this.getArguments().getString("groupName");
		this.student_name = this.getArguments().getString("student_name");

		resources = getResources();
		
		String tempDateStr = dateYear + dateArray[position]; // ex) 201507
		int YearMonth = Integer.parseInt(tempDateStr);
		if(groupName == null || groupName == ""){
			groupName = "There is no groupName parameter";
		}
		params_select.put("g_name", groupName);
		params_select.put("r_day", YearMonth);		
	}

	// onCreateView�� �������̵��ϴµ�, ������������ ������ �並 �����ϴ� ��
	@Override
	public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		final LinearLayout l_Layout = (LinearLayout) inflater.inflate(R.layout.fragment_month_group, container, false); // (resource,root)
		noreserve_tv = (TextView) l_Layout.findViewById(R.id.groupReservation_noreserve_tv);
		
		// /////////////////////////////////////////// �����׸�Ʈ����// findviewbyid �ϴ� ��, listview��� ��// ////////////////////////////////////////////
		adapter_groupreservation = new GroupReservationAdapter((GroupReservationActivity) getActivity(),jsonArray, resources ); // ��͸� �����ϰ� ������ ����	
		listview = (ListView) l_Layout.findViewById(R.id.GroupReservation_listview); // ����Ʈ�信 ��� ����
		listview.setAdapter(adapter_groupreservation);
		
		getJsonArrayThread = new GetJsonArrayThread();
		getJsonArrayThread.start();
		try {
			getJsonArrayThread.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(jsonArray == null || jsonArray.length() == 0){ 
			noreserve_tv.setHeight(300);
			noreserve_tv.setText("���� ��Ȳ�� �����ϴ�.");
			
			} else {
			noreserve_tv.setHeight(0);
			noreserve_tv.setText("");
			}
		
		adapter_groupreservation.setItem(jsonArray);
		adapter_groupreservation.notifyDataSetChanged();
		
		//TextView noreserve_tv = new TextView((GroupReservationActivity) getActivity());
		// ���� ����� ���� �� "���� ��Ȳ�� �����ϴ�" �� ����
		//Log.d("error_", "l_Layout.getChildCount() : " + l_Layout.getChildCount());
		
		return l_Layout;

	}

	
	public class GetJsonArrayThread extends Thread{
		public void run(){
			client_select.post(URL_select, params_select,new JsonHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers,String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString,throwable);
				}
				@Override
				public void onSuccess(int statusCode, Header[] headers,JSONArray response) {					
					jsonArray = response;
					//Log.d("time_", "In client");
					//Log.d("error_","GroupReservation jsonArray.length() : "+ jsonArray.length());
				}
			});
		}
	}
	
	

}
