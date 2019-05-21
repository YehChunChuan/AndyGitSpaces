package com.fragment.fragmenttest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.SQLException;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class HistoricalChartFragment extends Fragment {

	private String value = "";
	private LineChart chart1;
	private LineChart chart2;


	//region 變數群
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
	private Date beforDate;
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

	private  WebView mWebView;
	private ProgressDialog dialog;


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
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d("=====>", "HistoricalChartFragment onCreateView");
		//記得要定義Layout View
		//region 加入webview的使用
		View v=inflater.inflate(R.layout.frg_historicalchart, container, false);
//		mWebView = (WebView) v.findViewById(R.id.webview);
//		mWebView.loadUrl("http://54.189.167.79/chart");
//		// Enable Javascript
//		WebSettings webSettings = mWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		// Force links and redirects to open in the WebView instead of in a browser
//		mWebView.setWebViewClient(new WebViewClient());
		//endregion

		return v;
	}


	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("=====>", "HistoricalChartFragment onActivityCreated");

		FragmentActivity fragmentActivity = getActivity();
		//if (fragmentActivity instanceof MainActivity)
		{
			MainActivity activity = (MainActivity) fragmentActivity;
		}
		PostgresqlTask TestClient = new PostgresqlTask();
            //TestClient.execute("2019-04-29 10:00:00");300000   600000   10000
            tine=10000;
			sdf = new android.icu.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			now=new Date();
            beforDate = new Date(now.getTime() - tine);
            //System.out.println(sdf.format(beforDate));
            Log.e("現在時間減10分鐘:", sdf.format(beforDate));
            //Toast.makeText(MainActivity.this, "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
            TestClient.execute(sdf.format(beforDate));





		chart1 = (LineChart) getView().findViewById(R.id.chart1);
		chart2 = (LineChart) getView().findViewById(R.id.chart2);




		//TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
		//txtResult.setText(value);
		//顯示邊界
		chart1.setDrawBorders(true);
		//設定資料
		List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			entries.add(new Entry(i, (float) (Math.random()) * 80));
		}
		//一個LineDataSet就是一條線
		LineDataSet lineDataSet = new LineDataSet(entries, "溫度");
		LineData data = new LineData(lineDataSet);
		chart1.setData(data);


	}
	private ResultSet rs;
	//region AsyncTask連查資料庫
    private class PostgresqlTask extends AsyncTask<String, String, String> {
        @Override
        public String doInBackground(String... params) {
            Connection con = null;
            PreparedStatement ps = null;
			rs= null;
            String count = "0";
            Statement st = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                //STEP 3: Open a connection
                //System.out.println("Connecting to database...");
                Class.forName("org.postgresql.Driver");
                Log.e("我要連線!!!", "我要連線!!!");
                //DriverManager.setLoginTimeout(5);
                //jdbc:postgresql://140.116.247.120:36432/happig幹happig_read幹HappigRead幹
                con = DriverManager.getConnection(
                        "jdbc:postgresql://140.116.247.120:36432/happig",
                        "happig_read",
                        "HappigRead");
                if (con != null && !con.isClosed()) {
                    //System.out.println("資料庫連線測試成功！");
                    //con.close();
                }
//-----------------------------------------------------------------------------------------------------------------------------------------------
//            //STEP 4: Execute a query
                //2019-04-26 00:00:00
                //System.out.println("Creating statement...");
                //st = con.createStatement();
                String sql;
                sql = "select * from sensor_records where read_time>='" + params[0] + "' order by read_time desc";
                //ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            //STEP 5: Extract data from result set
                Log.e("executeQuery!", sql);
                //rs = ps.executeQuery(sql);
                //改成可以雙向滾動，但不及時更新，就是如果數據庫裡的數據修改過，
                //並不修改ResultSet記錄 必須要改成 TYPE_SCROLL_INSENSITIVE
                ps = ((java.sql.Connection) con).prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                //执行查询
                rs = ps.executeQuery();
                int size = 0;
                if (rs != null) {
                    rs.last();    // moves cursor to the last row
                    size = rs.getRow(); // get row id
                }

                ID = 0;
                record_id_arry = new String[size];
                sensor_id_arry = new String[size];
                sensor_category_arry = new String[size];
                sensor_value_arry = new String[size];
                read_time_arry = new String[size];
                rs.first();

                while (rs.next()) {
                    //Retrieve by column name
                    String record_id = rs.getString("record_id");
                    record_id_arry[ID] = "RID:" + record_id;
                    String sensor_id = rs.getString("sensor_id");
                    sensor_id_arry[ID] = "SID:" + sensor_id;
                    String sensor_category = rs.getString("sensor_category");
                    sensor_category_arry[ID] = "SCAT:" + sensor_category;
                    String sensor_value = rs.getString("sensor_value");
                    sensor_value_arry[ID] = "SV:" + sensor_value;
                    String read_time = rs.getString("read_time");
                    read_time_arry[ID] = "RT:" + read_time;
//                //Display values
                    Log.e("record_id!", record_id);
                    Log.e("sensor_id!", sensor_id);
                    Log.e("sensor_category!", sensor_category);
                    Log.e("sensor_value!", sensor_value);
                    Log.e("read_time!", read_time);
//                    String TTV = "";
//                    TTV = TTV + "record_id:" + record_id + "\n" + "sensor_id:" + sensor_id + "\n" + "sensor_category:" + sensor_category + "\n" + "sensor_value:" + sensor_value + "\n" + "read_time:" + read_time;
//                    stringBuilder.append(TTV).append("\n");
                    ID += 1;
                }
				//HashMap<String, String> sensor_data_hash_map = new HashMap<String, String>();

				IdentityHashMap<String,Object> sensor_data_hash_map =new IdentityHashMap<String,Object>();
                    for (int i = 0; i < ID; i++) {
                        sensor_data_hash_map.put(sensor_category_arry[i], sensor_value_arry[i]);
                    }
//------------------------------------------------------------------------------------------------------------------------------------------------

                //System.out.println(ps);
                // Log.e("ps!", ps.toString());
                //return   ;
            } catch (SQLException e) {
                Log.e("SQLException有例外!", e.getMessage());
                //Toast.makeText(MyApplication.getContext(), "No SQLException有例外!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("Exception有例外!", e.getMessage());
            }
            return stringBuilder.toString();
        }

        //After
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            System.out.println("result: " + result);
            try {
                //顯示
//-------------------------------------------------------------------------------------------------------------------------
//
                //定義 ListView 每個 Item 的資料
                List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
                ID = 0;

                for (int i = 0; i < record_id_arry.length; i++) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("record_id", record_id_arry[i]);
                    item.put("sensor_id", sensor_id_arry[i]);
                    item.put("sensor_category", sensor_category_arry[i]);
                    item.put("sensor_value", sensor_value_arry[i]);
                    item.put("read_time", read_time_arry[i]);
                    itemList.add(item);
                }

                dialog.dismiss();


				XAxis xAxis = chart1.getXAxis();
				xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
				xAxis.setGranularity(1f);
				xAxis.setLabelCount(12, true);
				xAxis.setAxisMinimum(0f);
				xAxis.setAxisMaximum(20f);
//				xAxis.setValueFormatter(new IAxisValueFormatter() {
//					@Override
//					public String getFormattedValue(float value, AxisBase axis) {
//						return rs.get((int) value); //mList為存有月份的集合
//						return rs.get((int) value); //mList為存有月份的集合
//					}
//				});

////---------------------------------------------------------------------------------------------------------------------------------------------
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(MyApplication.getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            try {
                //顯示
                //text1.append(values.toString());
//                if (!values.equals("1")) {
//                    Toast.makeText(getApplicationContext(), "Hello admin!", Toast.LENGTH_SHORT).show();
//                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Seems like you 're not admin!", Toast.LENGTH_SHORT).show();
//                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MyApplication.getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//endregion



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
