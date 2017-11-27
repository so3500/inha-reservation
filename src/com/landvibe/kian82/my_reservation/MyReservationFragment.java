
package com.landvibe.kian82.my_reservation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.inhareservation.R;
import com.landvibe.kian82.group_reservation.GroupReservationFragment.GetJsonArrayThread;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class MyReservationFragment extends Fragment {
	private int position;
	private int student_seq;
	private int currentDate;
	private int end_time_index;
	
	private static JSONArray jsonArray = new JSONArray();
	RequestParams param = new RequestParams();
	AsyncHttpClient client = new AsyncHttpClient();
	private String URL_select = "http://oursoccer.co.kr/study/reserve/My_SelectReserve.php";
	public static MyReservationAdapter adapter_myreservation;
	public ListView listview;
	private int dateYear;
	
	TextView noreserve_tv;
	private SyncHttpClient client_select = new SyncHttpClient();
	private RequestParams params_select = new RequestParams();
	private JSONObject jsonObject = null;
	ReserveSelectTask reserveSelectTask = new ReserveSelectTask();
	Integer[] taskParams = new Integer[4];

	LinearLayout l_Layout;
	Resources resource;

	// �����ڿ� ���� �Լ�.
	public static MyReservationFragment create(int position, int student_seq, int currentDate, int end_time_index) {
		MyReservationFragment frg_month = new MyReservationFragment();
		Bundle b = new Bundle();
		b.putInt("position", position);
		b.putInt("student_seq", student_seq);
		b.putInt("currentDate", currentDate);
		b.putInt("end_time_index", end_time_index);
		frg_month.setArguments(b);
		return frg_month;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resource = getResources();
		// setContentView(R.layout.month_1); �̰� �Ƚ���
		// �����׸�Ʈ�� onCreate����, create �Լ����� �޾Ƶξ��� ������ �����ͼ�, �������� ������ ���� ������ ����
		this.position = this.getArguments().getInt("position");
		this.student_seq = this.getArguments().getInt("student_seq");
		this.currentDate = this.getArguments().getInt("currentDate");
		this.end_time_index  = this.getArguments().getInt("end_time_index");

		taskParams[0] = position;
		taskParams[1] = currentDate;
		taskParams[2] = student_seq;
		taskParams[3] = end_time_index;
	}

	// onCreateView�� �������̵��ϴµ�, ������������ ������ �並 �����ϴ� ��
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		l_Layout = (LinearLayout) inflater.inflate(R.layout.fragment_month_group, container, false); // (resource,root)
		noreserve_tv = (TextView) l_Layout.findViewById(R.id.groupReservation_noreserve_tv);
		
		reserveSelectTask.execute(taskParams);
		
		return l_Layout;
	}
	

	//�����͸� �޾ƿ��� ���� ��׶��� �۾��� ������ Ŭ���� AsyncTask<params, progress, result>
	private class ReserveSelectTask extends AsyncTask<Integer, Integer, Boolean>{
	   @Override
	   protected Boolean doInBackground(Integer... params) {
		   // taskParams = {position, r_day, student_seq, end_time_index_p}	      
	      params_select.put("upDown", taskParams[0]);
	      params_select.put("r_day", taskParams[1]);
	      params_select.put("s_seq", taskParams[2]);
	      params_select.put("end_time_index", taskParams[3]);
	      client_select.post(URL_select, params_select, new JsonHttpResponseHandler() {
	         @Override
	         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
	            super.onFailure(statusCode, headers, responseString, throwable);
	            //Log.d("error_", "statusCode : " + statusCode);
	         }
	         @Override
	         public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
	            jsonArray = response;
	            
	            //Log.d("error_", "statusCode : " + statusCode);
	            //Log.d("error_", "response.length() : " + response.length());
	            }
	         });
	      
	      return null;
	   } // doInBackground ��
	   
	   @Override
	    protected void onPostExecute(Boolean result) {           
	        super.onPostExecute(result);
	        adapter_myreservation = new MyReservationAdapter((MyReservationActivity) getActivity(),jsonArray, resource ); // ��͸� �����ϰ� ������ ����	
			listview = (ListView) l_Layout.findViewById(R.id.GroupReservation_listview); // ����Ʈ�信 ��� ����
			listview.setAdapter(adapter_myreservation);   
	        
	        if(jsonArray == null || jsonArray.length() == 0){ 
				noreserve_tv.setHeight(300);
				noreserve_tv.setText("���� ��Ȳ�� �����ϴ�.");
				
				} else {
				noreserve_tv.setHeight(0);
				noreserve_tv.setText("");
				}

			if(position == 1){
				Log.d("error_", "jsonArray position 1 : " + jsonArray.length());
			} else if(position == 0){
				Log.d("error_", "jsonArray position 0 : " + jsonArray.length());
			}
	    }
	} // =============== AsyncTask �� ================

	
	

}