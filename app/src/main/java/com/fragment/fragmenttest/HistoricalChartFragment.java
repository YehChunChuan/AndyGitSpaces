package com.fragment.fragmenttest;

import android.app.Activity;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;




import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import static android.content.ContentValues.TAG;


public class HistoricalChartFragment extends Fragment {

	private String value = "";
	private LineChart chart1;
	private LineChart chart2;


	//regaion 變數群
	private String[] record_id_arry;
	private String[] sensor_id_arry;
	private String[] sensor_category_arry;
	private String[] sensor_value_arry;
	private String[] read_time_arry;
	private int ID;
	//private Date now;
	private android.icu.text.SimpleDateFormat sdf;
	private int tine;
	private Date now;
	Date beforDate;
	String climate;
	String pm25;
	String nh3;
	String h2s;
	String humidity;
	String co2;
	String ch4;

	String climate_time;
	String pm25_time;
	String nh3_time;
	String h2s_time;
	String humidity_time;
	String co2_time;
	String ch4_time;

	String climate2;
	String pm252;
	String nh32;
	String h2s2;
	String humidity2;
	String co22;
	String ch42;
	//endregion


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d("=====>", "HistoricalChartFragment onAttach");
		MainActivity mainActivity = (MainActivity) activity;
		value = mainActivity.getHistoricalChart();
	}

	MainActivity AC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AC = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d("=====>", "HistoricalChartFragment onCreateView");
		//mLineChart = (LineChart) (R.id.chart1);

		//記得要定義Layout View
		return inflater.inflate(R.layout.frg_historicalchart, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("=====>", "HistoricalChartFragment onActivityCreated");

		FragmentActivity fragmentActivity = getActivity();
		//if (fragmentActivity instanceof MainActivity)
		{
			MainActivity activity = (MainActivity) fragmentActivity;
		}

		chart1 = (LineChart) getView().findViewById(R.id.chart1);
		chart2 = (LineChart) getView().findViewById(R.id.chart2);
		//TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
		//txtResult.setText(value);
	}

	private class PigsAppsTask extends AsyncTask<String, Integer, String> {

		//@Override
		protected void onPreExecute() {
//TODOAuto-generated method stub
			Log.d(TAG, "onPreExecute");
			//super.onPreExecute();
		}

		//@Override
		protected String doInBackground(String... params) {
		//TODOAuto-generated method stub
			Log.d(TAG, "doInBackground params[0]=" + params[0]);
			//publishProgress(10);
			//return100L;
			return (String) "";
		}

		//@Override
		protected void onProgressUpdate(Integer... values) {
//TODOAuto-generated method stub
			Log.d(TAG, "onProgressUpdate values[0]=" + values[0]);
			//super.onProgressUpdate(values);
		}
		//@Override
		protected void onPostExecute(String result) {
		//TODOAuto-generated method stub
			Log.d(TAG, "onPostExecute result=" + result);
			//super.onPostExecute(result);
		}
	}

	//regaion 取整點的方式
	private String getInitialTime(String time) {
		String hour = "00";//小时
		String minutes = "00";//分钟
		String outTime = "00:00";
		StringTokenizer st = new StringTokenizer(time, ":");
		List<String> inTime = new ArrayList<String>();
		while (st.hasMoreElements()) {
			inTime.add(st.nextToken());
		}
		hour = inTime.get(0).toString();
		minutes = inTime.get(1).toString();
		if (Integer.parseInt(minutes) > 30) {
			hour = (Integer.parseInt(hour) + 1) + "";
		}
		outTime = hour + ":00";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		try {
			outTime = sdf.format(sdf.parse(outTime));
		} catch (ParseException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return outTime;
	}
	//endregaion
}
