package com.fragment.fragmenttest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.database.SQLException;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.util.Calendar;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;


public class HistoricalChartFragment extends Fragment {

	private String value = "";
	private LineChart climate_chart;
	private LineChart PM25_chart;
	private LineChart NH3_chart;
	private LineChart H2S_chart;
	private LineChart humidity_chart;
	private LineChart CO2_chart;
	private LineChart CH4_chart;


	//region 變數群
	private String[] record_id_arry;
	private String[] sensor_id_arry;
	private String[] sensor_category_arry;
	private String[] sensor_value_arry;
	private String[] read_time_arry;
	private int ID;
	//private Date now;
	private SimpleDateFormat sdf;
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

	private String[] climate_array;
	private String[] pm25_arry;
	private String[] nh3_arry;
	private String[] h2s_arry;
	private String[] humidity_arry;
	private String[] co2_arry;
	private String[] ch4_arry;

	private ArrayList<String> climate_arraylist = new ArrayList<String>();
	private ArrayList<String> pm25_arraylist = new ArrayList<String>();
	private ArrayList<String> nh3_arraylist = new ArrayList<String>();
	private ArrayList<String> h2s_arraylist = new ArrayList<String>();
	private ArrayList<String> humidity_arraylist = new ArrayList<String>();
	private ArrayList<String> co2_arraylist = new ArrayList<String>();
	private ArrayList<String> ch4_arraylist = new ArrayList<String>();

	private ArrayList<String> climate_time_arraylist = new ArrayList<String>();
	private ArrayList<String> pm25_time_arraylist = new ArrayList<String>();
	private ArrayList<String> nh3_time_arraylist = new ArrayList<String>();
	private ArrayList<String> h2s_time_arraylist = new ArrayList<String>();
	private ArrayList<String> humidity_time_arraylist = new ArrayList<String>();
	private ArrayList<String> co2_time_arraylist = new ArrayList<String>();
	private ArrayList<String> ch4_time_arraylist = new ArrayList<String>();

	//endregion

	private WebView mWebView;
	private ProgressDialog dialog;
	private IdentityHashMap<String, Object> sensor_data_hash_map = null;
	private Button startTimeButton;
	private Button endTimeButton;
	private TextView startTimeText;
	private TextView endTimeText;

	Calendar startcalendar;
	Calendar endcalendar;

	private int smYear;
	private int smMonth;
	private int smDay;
	private int smh;
	private int smm;

	private int emYear;
	private int emMonth;
	private int emDay;
	private int emh;
	private int emm;

	private String ST = "";
	private String ET = "";

	private LineDataSet set1;

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
		View v = inflater.inflate(R.layout.frg_historicalchart, container, false);
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
		tine = 3600000;
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		now = new Date();
		beforDate = new Date(now.getTime() - tine);
		//System.out.println(sdf.format(beforDate));
		String Time = sdf.format(beforDate);
		Log.e("現在時間減10分鐘:", Time);
		//Toast.makeText(MainActivity.this, "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
		TestClient.execute(Time);

		climate_chart = (LineChart) getView().findViewById(R.id.climate_chart);
		PM25_chart = (LineChart) getView().findViewById(R.id.pm25_chart);
		NH3_chart = (LineChart) getView().findViewById(R.id.NH3_chart);
		H2S_chart = (LineChart) getView().findViewById(R.id.H2S_chart);
		humidity_chart = (LineChart) getView().findViewById(R.id.humidity_chart);
		CO2_chart = (LineChart) getView().findViewById(R.id.CO2_chart);
		CH4_chart = (LineChart) getView().findViewById(R.id.CH4_chart);
		startTimeButton = (Button) getView().findViewById(R.id.startTimeButton);
		endTimeButton = (Button) getView().findViewById(R.id.endTimeButton);
		startTimeText = (TextView) getView().findViewById(R.id.startTimeText);
		endTimeText = (TextView) getView().findViewById(R.id.endTimeText);

		dialog = ProgressDialog.show(this.getContext(), "",
				"Loading. Please wait...", false, false);
		startcalendar = Calendar.getInstance();




		initialChart(climate_chart);
		addLineDataSet(climate_chart);


		startTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//addEntry(climate_chart);
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(AC, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						//startTimeText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
						final Calendar c = Calendar.getInstance();
						smYear = year;
						smMonth = monthOfYear;
						smDay = dayOfMonth;

						smh = c.get(Calendar.HOUR_OF_DAY);
						smm = c.get(Calendar.MINUTE);
						updateDisplay(1, smYear, smMonth, smDay, smh, smm);


					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
				//addEntry();
			}
		});
		endTimeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(AC, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						//endTimeText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
						final Calendar c = Calendar.getInstance();

						emYear = year;
						emMonth = monthOfYear;
						emDay = dayOfMonth;
						emh = c.get(Calendar.HOUR_OF_DAY);
						emm = c.get(Calendar.MINUTE);
						updateDisplay(2, emYear, emMonth, emDay, emh, emm);

						PostgresqlTask TestClient = new PostgresqlTask();
						TestClient.execute();
						dialog = ProgressDialog.show(AC, "",
								"Loading. Please wait...", false, false);
						startcalendar = Calendar.getInstance();
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

			}
		});



	}
//*********************************************************************************************************************************************
//	// 新增進去一個座標點
//	private void addEntry() {
//		LineData data = climate_chart.getData();
//// 每一個LineDataSet代表一條線，每張統計圖表可以同時存在若干個統計折線，這些折線像陣列一樣從0開始下標。
//// 本例只有一個，那麼就是第0條折線
//		ILineDataSet set1 =  data.getDataSetByIndex(0);
//// 如果該統計折線圖還沒有資料集，則建立一條出來，如果有則跳過此處程式碼。
//		if (set1 == null) {
//			set1 = createLineDataSet();
//			data.addDataSet(set1);
//		}
//// 先新增一個x座標軸的值
//// 因為是從0開始，data.getXValCount()每次返回的總是全部x座標軸上總數量，所以不必多此一舉的加1
//		data.addXValue(String.valueOf((data.getXValCount())));
//// 生成隨機測試數
//		float f = (float) ((Math.random()) *100);
//// set.getEntryCount()獲得的是所有統計圖表上的資料點總量，
//// 如從0開始一樣的陣列下標，那麼不必多次一舉的加1
//		List<Entry> valsComp1 = new ArrayList<Entry>();
//		Entry c1e1 = new Entry(0f, (int) 100000f); // 0 == quarter 1
//		valsComp1.add(c1e1);
//// and so on ...
//		LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
//		setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
//		List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//		dataSets.add(setComp1);
//		//LineData data = new LineData(dataSets);
//		climate_chart.setData(data);
//		climate_chart.invalidate();
//// 往linedata裡面新增點。注意：addentry的第二個引數即代表折線的下標索引。
//// 因為本例只有一個統計折線，那麼就是第一個，其下標為0.
//// 如果同一張統計圖表中存在若干條統計折線，那麼必須分清是針對哪一條（依據下標索引）統計折線新增。
//		//data.addEntry(entry, 0);
//// 像ListView那樣的通知資料更新
//		climate_chart.notifyDataSetChanged();
//// 當前統計圖表中最多在x軸座標線上顯示的總量
//		climate_chart.setVisibleXRangeMaximum(5);
//// y座標軸線最大值
//// mChart.setVisibleYRange(30, AxisDependency.LEFT);
//// 將座標移動到最新
//// 此程式碼將重新整理圖表的繪圖
//		climate_chart.moveViewToX(data.getXValCount() - 5);
//// mChart.moveViewTo(data.getXValCount()-7, 55f,
//// AxisDependency.LEFT);
//	}
//	// 初始化資料集，新增一條統計折線，可以簡單的理解是初始化y座標軸線上點的表徵
//	private LineDataSet createLineDataSet() {
//		LineDataSet set = new LineDataSet(null, "動態新增的資料");
//		set.setAxisDependency(YAxis.AxisDependency.LEFT);
//// 折線的顏色
//		set.setColor(ColorTemplate.getHoloBlue());
//		set.setCircleColor(Color.WHITE);
//		set.setLineWidth(10f);
//		set.setCircleSize(5f);
//		set.setFillAlpha(128);
//		set.setFillColor(ColorTemplate.getHoloBlue());
//		set.setHighLightColor(Color.GREEN);
//		set.setValueTextColor(Color.WHITE);
//		set.setValueTextSize(10f);
//		set.setDrawValues(true);
//		return set;
//	}
//*************************************************************************************************************************************************
	private String formateTime(int time) {

		String timeStr = "";
		if (time < 10) {
			timeStr = "0" + String.valueOf(time);
		} else {
			timeStr = String.valueOf(time);
		}
		return timeStr;
	}
	private void updateDisplay(int N, int Y, int M, int D, int H, int m) {
		switch (N) {
			case 1:
				startTimeText.setText(
						new StringBuilder().append(Y).append("-")
								.append(formateTime(M + 1)).append("-")
								.append(formateTime(D)).append(" ")
								.append(formateTime(H)).append(":")
								.append(formateTime(m)));
				ST = (String) startTimeText.getText();
				Log.e("ST", ST);
				break;
			case 2:
				endTimeText.setText(
						new StringBuilder().append(Y).append("-")
								.append(formateTime(M + 1)).append("-")
								.append(formateTime(D)).append(" ")
								.append(formateTime(H)).append(":")
								.append(formateTime(m)));
				ET = (String) endTimeText.getText();
				Log.e("ET", ET);
				break;
		}
	}

	private ResultSet rs;

	//region AsyncTask連查資料庫與畫圖
	private class PostgresqlTask extends AsyncTask<String, String, String> {
		@Override
		public String doInBackground(String... params) {
			Connection con = null;
			PreparedStatement ps = null;
			rs = null;
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
				//sql = "select * from sensor_records where read_time>='" + ST + "' AND read_time<='" + ET + "' order by read_time desc";
				sql = "select * from sensor_records where read_time>='" + beforDate + "' order by read_time desc";
				//sql ="select  sensor_category,sensor_value,read_time from  sensor_records where (sensor_category='climate'or sensor_category='pm2.5'or sensor_category='nh3'or sensor_category='h2s'or sensor_category='humidity'or sensor_category='co2'or sensor_category='ch4' )and read_time>='2019-04-18 00:00:00' AND read_time<='2019-05-22 14:27:00' order by read_time desc";
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

//				 climate_array= new String[size];
//				 pm25_arry= new String[size];
//				 nh3_arry= new String[size];
//				 h2s_arry= new String[size];
//				 humidity_arry= new String[size];
//				 co2_arry= new String[size];
//				 ch4_arry= new String[size];

				rs.first();

				while (rs.next()) {
					//Retrieve by column name
					String record_id = rs.getString("record_id");
					record_id_arry[ID] = record_id;// "RID:" + record_id;
					String sensor_id = rs.getString("sensor_id");
					sensor_id_arry[ID] = sensor_id;
					;//"SID:" + sensor_id;
					String sensor_category = rs.getString("sensor_category");
					sensor_category_arry[ID] = sensor_category;//"SCAT:" + sensor_category;
					String sensor_value = rs.getString("sensor_value");
					sensor_value_arry[ID] = sensor_value;//"SV:" + sensor_value;
					String read_time = rs.getString("read_time");
					read_time_arry[ID] = read_time;//"RT:" + read_time;
//                //Display values
//                    Log.e("record_id!", record_id);
//                    Log.e("sensor_id!", sensor_id);
//                    Log.e("sensor_category!", sensor_category);
//                    Log.e("sensor_value!", sensor_value);
//                    Log.e("read_time!", read_time);
//                    String TTV = "";
//                    TTV = TTV + "record_id:" + record_id + "\n" + "sensor_id:" + sensor_id + "\n" + "sensor_category:" + sensor_category + "\n" + "sensor_value:" + sensor_value + "\n" + "read_time:" + read_time;
//                    stringBuilder.append(TTV).append("\n");
					ID += 1;
				}
				//HashMap<String, String> sensor_data_hash_map = new HashMap<String, String>();

				//sensor_data_hash_map =new IdentityHashMap<String,Object>();
				for (int i = 0; i < ID; i += 1000) {
					//Log.e("IdentityHashMap", sensor_category_arry[i] + ":" + sensor_value_arry[i]);//String.valueOf(i));
					//sensor_data_hash_map.put(sensor_category_arry[i], sensor_value_arry[i]);
					if (sensor_category_arry[i].equals("climate")) {
						//climate_array[i] = sensor_value_arry[i];
						climate_arraylist.add(sensor_value_arry[i]);
						climate_time_arraylist.add(read_time_arry[i]);
						//addEntry(climate_chart,sensor_value_arry[i]);
						Log.e("climate_arraylist+i:" + i, sensor_value_arry[i]);
					} else if (sensor_category_arry[i].equals("pm2.5")) {
						//pm25_arry[i] =sensor_value_arry[i];
						pm25_arraylist.add(sensor_value_arry[i]);
						pm25_time_arraylist.add(read_time_arry[i]);
						Log.e("pm25_arraylist+i:" + i, sensor_value_arry[i]);
					} else if (sensor_category_arry[i].equals("nh3")) {
						//nh3_arry[i] = sensor_value_arry[i];
						nh3_arraylist.add(sensor_value_arry[i]);
						nh3_time_arraylist.add(read_time_arry[i]);
						Log.e("nh3_arraylist+i:" + i, sensor_value_arry[i]);
					} else if (sensor_category_arry[i].equals("h2s")) {
						//h2s_arry[i] = sensor_value_arry[i];
						h2s_arraylist.add(sensor_value_arry[i]);
						h2s_time_arraylist.add(read_time_arry[i]);
						Log.e("h2s_arraylist+i:" + i, sensor_value_arry[i]);
					} else if (sensor_category_arry[i].equals("humidity")) {
						//humidity_arry[i] = sensor_value_arry[i];
						humidity_arraylist.add(sensor_value_arry[i]);
						humidity_time_arraylist.add(read_time_arry[i]);
						Log.e("humidity_arraylist+i" + i, sensor_value_arry[i]);
					} else if (sensor_category_arry[i].equals("co2")) {
						//co2_arry[i] = String.valueOf(sensor_value_arry[i]);
						co2_arraylist.add(sensor_value_arry[i]);
						co2_time_arraylist.add(read_time_arry[i]);
						Log.e("co2_arraylist+i" + i, sensor_value_arry[i]);
					} else if (sensor_category_arry[i].equals("ch4")) {
						//ch4_arry[i] =sensor_value_arry[i];
						ch4_arraylist.add(sensor_value_arry[i]);
						ch4_time_arraylist.add(read_time_arry[i]);
						Log.e("ch4_arraylist+i" + i, sensor_value_arry[i]);
					}
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
//region 溫度Chart
		// 这个应该是设置数据的函数了
		private void set_climate_Data(int count, float range) {
			// 在图表执行动作时，为定制回调设置一个动作监听器。
			climate_chart.setOnChartGestureListener((OnChartGestureListener) MyApplication.getContext());
			// 为图表设置一个既定的监听器
			climate_chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) MyApplication.getContext());
			// 把这个设为true来绘制网格背景，如果false则不绘制
			climate_chart.setDrawGridBackground(true);

			// 把这个设置为false，禁用所有手势和图表上的触摸，默认：true
			climate_chart.setTouchEnabled(true);

			// 设置图标拖动为允许
			climate_chart.setDragEnabled(true);
			// 设置图表缩放为允许
			climate_chart.setScaleEnabled(true);
			climate_chart.setScaleXEnabled(true);
			climate_chart.setScaleYEnabled(true);
			climate_chart.setDescription("");
			climate_chart.getXAxis().setLabelRotationAngle(0);

			// 挤压缩放设置为允许，即X轴和Y轴会成比例缩放，如果设置为false，则变成单独缩放
			climate_chart.setPinchZoom(true);

			// 返回代表所有x标签的对象，这个方法可以用来获得XAxis对象并修改它（例如改变标签的位置、样式等）
			XAxis xAxis = climate_chart.getXAxis();
			// 返回左y轴对象。在水平的柱状图中，这是最上面的轴。
			YAxis leftAxis = climate_chart.getAxisLeft();
			leftAxis.removeAllLimitLines();
			ClimateYAxisValueFormatter custom = new ClimateYAxisValueFormatter();
			climate_chart.getAxisLeft().setValueFormatter(custom);

			// 使网格线在虚线模式下绘制，例如像这个“------”。只有在硬件加速被关闭的情况下才会起作用。记住，硬件加速会提高性能。
			//123
			leftAxis.enableGridDashedLine(0f, 0f, 0f);
			// 将其设置为true，无论是否启用其他网格线，都要画出零线。默认值:假
			//123
			leftAxis.setDrawZeroLine(true);

			// limit lines are drawn behind data (and not on top)
			// 如果这被设置为true，那么界限就会被绘制在实际的数据后面，否则就在上面。默认值:假
			//123
			leftAxis.setDrawLimitLinesBehindData(true);
			//123
			climate_chart.getAxisRight().setEnabled(false);
			//123
			climate_chart.getAxisLeft().setEnabled(true);
			//123
			climate_chart.getXAxis().setEnabled(true);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

			// add data
			// 这是自己设定的添加数据的方法，count设置了数据的个数，range设置了波动范围
			//set_climate_Data(250, 100);

			//123
			// 调用动画对图表显示进行处理
			climate_chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


			// 这个应该就是x轴的数据了
			ArrayList<String> xVals = new ArrayList<String>();
			// 从 0 到 count设置x轴的数据
			for (int i = 0; i < count; i++) {
				xVals.add(climate_time_arraylist.get(i) + "");
			}
			// 这个是y轴的数据
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			// 设置y轴的数据，在这里，是用random函数来生成的
			for (int i = 0; i < count; i++) {
				//String mult = climate_arraylist.get(i);
				if (Float.parseFloat(climate_arraylist.get(i)) < 100) {
					float val = (float) Float.parseFloat(climate_arraylist.get(i));//(float) // (Math.random() * mult) + 3;// + (float)
					yVals.add(new Entry(val, i));
				}
			}
			// MPAC自定义的一种类
			// create a dataset and give it a type
			set1 = new LineDataSet(yVals, "溫度");
			set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);// 設定平滑曲線
			// set1.setFillAlpha(110);
			// set1.setFillColor(Color.RED);

			// set the line to be drawn like this "- - - - - -"
			// 下面是设置线的各种属性
			set1.setColor(Color.BLUE);
			set1.setCircleColor(Color.BLUE);
			set1.setLineWidth(1f);
			set1.setCircleRadius(0f);
			// 把这个设置为true，允许在每个数据圆上画一个洞。
			set1.setDrawCircleHole(false);
			set1.setValueTextSize(2f);
			//

			set1.setDrawFilled(false);


			if (Utils.getSDKInt() >= 18) {
				// fill drawable only supported on api level 18 and above
				//Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
				//set1.setFillDrawable(drawable);
				set1.setFillColor(Color.BLUE);
			} else {
				set1.setFillColor(Color.BLACK);
			}

			ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
			dataSets.add(set1); // add the datasets

			// create a data object with the datasets
			LineData data = new LineData(xVals, dataSets);
			// set data

			climate_chart.setData(data);

			//region 動態畫圖
			float f = (float) ((Math.random()) * 100);
			Entry entry = new Entry(f, set1.getEntryCount());
			data.addEntry(entry, 0);
			//endregion

			climate_chart.notifyDataSetChanged();
		}

		//endregion
//region PM25_Chart
// 这个应该是设置数据的函数了
		private void set_PM25_Data(int count, float range) {
			Log.e("set_PM25_Data", "進到方法");

			// 在图表执行动作时，为定制回调设置一个动作监听器。
			PM25_chart.setOnChartGestureListener((OnChartGestureListener) MyApplication.getContext());
			// 为图表设置一个既定的监听器
			PM25_chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) MyApplication.getContext());
			// 把这个设为true来绘制网格背景，如果false则不绘制
			PM25_chart.setDrawGridBackground(true);

			// 把这个设置为false，禁用所有手势和图表上的触摸，默认：true
			PM25_chart.setTouchEnabled(true);

			// 设置图标拖动为允许
			PM25_chart.setDragEnabled(true);
			// 设置图表缩放为允许
			PM25_chart.setScaleEnabled(true);
			PM25_chart.setScaleXEnabled(true);
			PM25_chart.setScaleYEnabled(true);
			PM25_chart.setDescription("");
			PM25_chart.getXAxis().setLabelRotationAngle(0);
			// 挤压缩放设置为允许，即X轴和Y轴会成比例缩放，如果设置为false，则变成单独缩放
			PM25_chart.setPinchZoom(true);

			// 返回代表所有x标签的对象，这个方法可以用来获得XAxis对象并修改它（例如改变标签的位置、样式等）
			XAxis xAxis = PM25_chart.getXAxis();
			// 返回左y轴对象。在水平的柱状图中，这是最上面的轴。
			YAxis leftAxis = PM25_chart.getAxisLeft();
			PM25YAxisValueFormatter custom = new PM25YAxisValueFormatter();
			PM25_chart.getAxisLeft().setValueFormatter(custom);
			leftAxis.removeAllLimitLines();

			// 使网格线在虚线模式下绘制，例如像这个“------”。只有在硬件加速被关闭的情况下才会起作用。记住，硬件加速会提高性能。
			//123
			leftAxis.enableGridDashedLine(0f, 0f, 0f);
			// 将其设置为true，无论是否启用其他网格线，都要画出零线。默认值:假
			//123
			leftAxis.setDrawZeroLine(true);

			// limit lines are drawn behind data (and not on top)
			// 如果这被设置为true，那么界限就会被绘制在实际的数据后面，否则就在上面。默认值:假
			//123
			leftAxis.setDrawLimitLinesBehindData(true);
			//123
			PM25_chart.getAxisRight().setEnabled(false);
			//123
			PM25_chart.getAxisLeft().setEnabled(true);
			//123
			PM25_chart.getXAxis().setEnabled(true);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

			// add data
			// 这是自己设定的添加数据的方法，count设置了数据的个数，range设置了波动范围
			//set_climate_Data(250, 100);

			//123
			// 调用动画对图表显示进行处理
			PM25_chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


			// 这个应该就是x轴的数据了
			ArrayList<String> xVals = new ArrayList<String>();
			// 从 0 到 count设置x轴的数据
			Log.e("set_PM25_Data", "開始要畫");
			for (int i = 0; i < count; i++) {
				xVals.add(pm25_time_arraylist.get(i) + "");
				Log.e("set_PM25_Data", "畫X軸:" + pm25_time_arraylist.get(i));
			}
			// 这个是y轴的数据
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			// 设置y轴的数据，在这里，是用random函数来生成的
			for (int i = 0; i < count; i++) {
				//String mult = climate_arraylist.get(i);
				if (Float.parseFloat(pm25_arraylist.get(i)) < 100) {
					float val = (float) Float.parseFloat(pm25_arraylist.get(i));//(float) // (Math.random() * mult) + 3;// + (float)
					//Log.e("set_PM25_Data","畫Y軸:"+pm25_arraylist.get(i));
					// ((mult *
					// 0.1) / 10);
					yVals.add(new Entry(val, i));
				}
			}
			// MPAC自定义的一种类
			// create a dataset and give it a type
			LineDataSet set1 = new LineDataSet(yVals, "PM 2.5");
			set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);// 設定平滑曲線
			// set1.setFillAlpha(110);
			// set1.setFillColor(Color.RED);

			// set the line to be drawn like this "- - - - - -"
			// 下面是设置线的各种属性
			set1.setColor(Color.BLUE);
			set1.setCircleColor(Color.BLUE);
			set1.setLineWidth(1f);
			set1.setCircleRadius(0f);
			// 把这个设置为true，允许在每个数据圆上画一个洞。
			set1.setDrawCircleHole(false);
			set1.setValueTextSize(2f);
			//

			set1.setDrawFilled(false);


			if (Utils.getSDKInt() >= 18) {
				// fill drawable only supported on api level 18 and above
				//Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
				//set1.setFillDrawable(drawable);
				set1.setFillColor(Color.BLUE);
			} else {
				set1.setFillColor(Color.BLACK);
			}

			ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
			dataSets.add(set1); // add the datasets

			// create a data object with the datasets
			LineData data = new LineData(xVals, dataSets);
			// set data
			PM25_chart.setData(data);
		}

		////endregion
//region NH3_Chart
		private void set_NH3_Data(int count, float range) {
			// 在图表执行动作时，为定制回调设置一个动作监听器。
			NH3_chart.setOnChartGestureListener((OnChartGestureListener) MyApplication.getContext());
			// 为图表设置一个既定的监听器
			NH3_chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) MyApplication.getContext());
			// 把这个设为true来绘制网格背景，如果false则不绘制
			NH3_chart.setDrawGridBackground(true);

			// 把这个设置为false，禁用所有手势和图表上的触摸，默认：true
			NH3_chart.setTouchEnabled(true);

			// 设置图标拖动为允许
			NH3_chart.setDragEnabled(true);
			// 设置图表缩放为允许
			NH3_chart.setScaleEnabled(true);
			NH3_chart.setScaleXEnabled(true);
			NH3_chart.setScaleYEnabled(true);
			NH3_chart.setDescription("");
			NH3_chart.getXAxis().setLabelRotationAngle(0);
			// 挤压缩放设置为允许，即X轴和Y轴会成比例缩放，如果设置为false，则变成单独缩放
			NH3_chart.setPinchZoom(true);

			// 返回代表所有x标签的对象，这个方法可以用来获得XAxis对象并修改它（例如改变标签的位置、样式等）
			XAxis xAxis = NH3_chart.getXAxis();
			// 返回左y轴对象。在水平的柱状图中，这是最上面的轴。
			YAxis leftAxis = NH3_chart.getAxisLeft();
			leftAxis.removeAllLimitLines();
			PM25YAxisValueFormatter custom = new PM25YAxisValueFormatter();
			NH3_chart.getAxisLeft().setValueFormatter(custom);
			// 使网格线在虚线模式下绘制，例如像这个“------”。只有在硬件加速被关闭的情况下才会起作用。记住，硬件加速会提高性能。
			//123
			leftAxis.enableGridDashedLine(0f, 0f, 0f);
			// 将其设置为true，无论是否启用其他网格线，都要画出零线。默认值:假
			//123
			leftAxis.setDrawZeroLine(true);

			// limit lines are drawn behind data (and not on top)
			// 如果这被设置为true，那么界限就会被绘制在实际的数据后面，否则就在上面。默认值:假
			//123
			leftAxis.setDrawLimitLinesBehindData(true);
			//123
			NH3_chart.getAxisRight().setEnabled(false);
			//123
			NH3_chart.getAxisLeft().setEnabled(true);
			//123
			NH3_chart.getXAxis().setEnabled(true);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

			// add data
			// 这是自己设定的添加数据的方法，count设置了数据的个数，range设置了波动范围
			//set_climate_Data(250, 100);

			//123
			// 调用动画对图表显示进行处理
			NH3_chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
			// 这个应该就是x轴的数据了
			ArrayList<String> xVals = new ArrayList<String>();
			// 从 0 到 count设置x轴的数据
			for (int i = 0; i < count; i++) {
				xVals.add(nh3_time_arraylist.get(i) + "");
			}
			// 这个是y轴的数据
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			// 设置y轴的数据，在这里，是用random函数来生成的
			for (int i = 0; i < count; i++) {
				//String mult = climate_arraylist.get(i);
				if ((float) Float.parseFloat(nh3_arraylist.get(i)) < 10) {
					float val = (float) Float.parseFloat(nh3_arraylist.get(i));//(float) // (Math.random() * mult) + 3;// + (float)
					// ((mult *
					// 0.1) / 10);
					yVals.add(new Entry(val, i));
				}
			}
			// MPAC自定义的一种类
			// create a dataset and give it a type
			LineDataSet set1 = new LineDataSet(yVals, "NH3");
			set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);// 設定平滑曲線
			// set1.setFillAlpha(110);
			// set1.setFillColor(Color.RED);

			// set the line to be drawn like this "- - - - - -"
			// 下面是设置线的各种属性
			set1.setColor(Color.BLUE);
			set1.setCircleColor(Color.BLUE);
			set1.setLineWidth(1f);
			set1.setCircleRadius(0f);
			// 把这个设置为true，允许在每个数据圆上画一个洞。
			set1.setDrawCircleHole(false);
			set1.setValueTextSize(2f);
			//

			set1.setDrawFilled(false);


			if (Utils.getSDKInt() >= 18) {
				// fill drawable only supported on api level 18 and above
				//Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
				//set1.setFillDrawable(drawable);
				set1.setFillColor(Color.BLUE);
			} else {
				set1.setFillColor(Color.BLACK);
			}

			ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
			dataSets.add(set1); // add the datasets

			// create a data object with the datasets
			LineData data = new LineData(xVals, dataSets);
			// set data
			NH3_chart.setData(data);
		}

		//endregion
//region H2S_Chart
		private void set_H2S_Data(int count, float range) {
			// 在图表执行动作时，为定制回调设置一个动作监听器。
			H2S_chart.setOnChartGestureListener((OnChartGestureListener) MyApplication.getContext());
			// 为图表设置一个既定的监听器
			H2S_chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) MyApplication.getContext());
			// 把这个设为true来绘制网格背景，如果false则不绘制
			H2S_chart.setDrawGridBackground(true);

			// 把这个设置为false，禁用所有手势和图表上的触摸，默认：true
			H2S_chart.setTouchEnabled(true);

			// 设置图标拖动为允许
			H2S_chart.setDragEnabled(true);
			// 设置图表缩放为允许
			H2S_chart.setScaleEnabled(true);
			H2S_chart.setScaleXEnabled(true);
			H2S_chart.setScaleYEnabled(true);
			H2S_chart.setDescription("");
			H2S_chart.getXAxis().setLabelRotationAngle(0);
			// 挤压缩放设置为允许，即X轴和Y轴会成比例缩放，如果设置为false，则变成单独缩放
			H2S_chart.setPinchZoom(true);

			// 返回代表所有x标签的对象，这个方法可以用来获得XAxis对象并修改它（例如改变标签的位置、样式等）
			XAxis xAxis = H2S_chart.getXAxis();
			// 返回左y轴对象。在水平的柱状图中，这是最上面的轴。
			YAxis leftAxis = H2S_chart.getAxisLeft();
			leftAxis.removeAllLimitLines();
			PM25YAxisValueFormatter custom = new PM25YAxisValueFormatter();
			H2S_chart.getAxisLeft().setValueFormatter(custom);
			// 使网格线在虚线模式下绘制，例如像这个“------”。只有在硬件加速被关闭的情况下才会起作用。记住，硬件加速会提高性能。
			//123
			leftAxis.enableGridDashedLine(0f, 0f, 0f);
			// 将其设置为true，无论是否启用其他网格线，都要画出零线。默认值:假
			//123
			leftAxis.setDrawZeroLine(true);

			// limit lines are drawn behind data (and not on top)
			// 如果这被设置为true，那么界限就会被绘制在实际的数据后面，否则就在上面。默认值:假
			//123
			leftAxis.setDrawLimitLinesBehindData(true);
			//123
			H2S_chart.getAxisRight().setEnabled(false);
			//123
			H2S_chart.getAxisLeft().setEnabled(true);
			//123
			H2S_chart.getXAxis().setEnabled(true);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

			// add data
			// 这是自己设定的添加数据的方法，count设置了数据的个数，range设置了波动范围
			//set_climate_Data(250, 100);

			//123
			// 调用动画对图表显示进行处理
			H2S_chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


			// 这个应该就是x轴的数据了
			ArrayList<String> xVals = new ArrayList<String>();
			// 从 0 到 count设置x轴的数据
			for (int i = 0; i < count; i++) {
				xVals.add(h2s_time_arraylist.get(i) + "");
			}
			// 这个是y轴的数据
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			// 设置y轴的数据，在这里，是用random函数来生成的
			for (int i = 0; i < count; i++) {
				//if(Float.parseFloat(h2s_arraylist.get(i))<6)
				{
					//String mult = climate_arraylist.get(i);
					float val = (float) Float.parseFloat(h2s_arraylist.get(i));//(float) // (Math.random() * mult) + 3;// + (float)
					// ((mult *
					// 0.1) / 10);
					yVals.add(new Entry(val, i));
				}
			}
			// MPAC自定义的一种类
			// create a dataset and give it a type
			LineDataSet set1 = new LineDataSet(yVals, "H2S");
			set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);// 設定平滑曲線
			// set1.setFillAlpha(110);
			// set1.setFillColor(Color.RED);

			// set the line to be drawn like this "- - - - - -"
			// 下面是设置线的各种属性
			set1.setColor(Color.BLUE);
			set1.setCircleColor(Color.BLUE);
			set1.setLineWidth(1f);
			set1.setCircleRadius(0f);
			// 把这个设置为true，允许在每个数据圆上画一个洞。
			set1.setDrawCircleHole(false);
			set1.setValueTextSize(2f);
			//

			set1.setDrawFilled(false);


			if (Utils.getSDKInt() >= 18) {
				// fill drawable only supported on api level 18 and above
				//Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
				//set1.setFillDrawable(drawable);
				set1.setFillColor(Color.BLUE);
			} else {
				set1.setFillColor(Color.BLACK);
			}

			ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
			dataSets.add(set1); // add the datasets

			// create a data object with the datasets
			LineData data = new LineData(xVals, dataSets);
			// set data
			H2S_chart.setData(data);
		}

		//endregion
//region 濕度_Chart
		private void set_humidity_Data(int count, float range) {
			// 在图表执行动作时，为定制回调设置一个动作监听器。
			humidity_chart.setOnChartGestureListener((OnChartGestureListener) MyApplication.getContext());
			// 为图表设置一个既定的监听器
			humidity_chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) MyApplication.getContext());
			// 把这个设为true来绘制网格背景，如果false则不绘制
			humidity_chart.setDrawGridBackground(true);

			// 把这个设置为false，禁用所有手势和图表上的触摸，默认：true
			humidity_chart.setTouchEnabled(true);

			// 设置图标拖动为允许
			humidity_chart.setDragEnabled(true);
			// 设置图表缩放为允许
			humidity_chart.setScaleEnabled(true);
			humidity_chart.setScaleXEnabled(true);
			humidity_chart.setScaleYEnabled(true);
			humidity_chart.setDescription("");
			humidity_chart.getXAxis().setLabelRotationAngle(0);
			// 挤压缩放设置为允许，即X轴和Y轴会成比例缩放，如果设置为false，则变成单独缩放
			humidity_chart.setPinchZoom(true);

			// 返回代表所有x标签的对象，这个方法可以用来获得XAxis对象并修改它（例如改变标签的位置、样式等）
			XAxis xAxis = humidity_chart.getXAxis();
			// 返回左y轴对象。在水平的柱状图中，这是最上面的轴。
			YAxis leftAxis = humidity_chart.getAxisLeft();
			leftAxis.removeAllLimitLines();
			HumidityYAxisValueFormatter custom = new HumidityYAxisValueFormatter();
			humidity_chart.getAxisLeft().setValueFormatter(custom);

			// 使网格线在虚线模式下绘制，例如像这个“------”。只有在硬件加速被关闭的情况下才会起作用。记住，硬件加速会提高性能。
			//123
			leftAxis.enableGridDashedLine(0f, 0f, 0f);
			// 将其设置为true，无论是否启用其他网格线，都要画出零线。默认值:假
			//123
			leftAxis.setDrawZeroLine(true);

			// limit lines are drawn behind data (and not on top)
			// 如果这被设置为true，那么界限就会被绘制在实际的数据后面，否则就在上面。默认值:假
			//123
			leftAxis.setDrawLimitLinesBehindData(true);
			//123
			humidity_chart.getAxisRight().setEnabled(false);
			//123
			humidity_chart.getAxisLeft().setEnabled(true);
			//123
			humidity_chart.getXAxis().setEnabled(true);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

			// add data
			// 这是自己设定的添加数据的方法，count设置了数据的个数，range设置了波动范围
			//set_climate_Data(250, 100);

			//123
			// 调用动画对图表显示进行处理
			humidity_chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


			// 这个应该就是x轴的数据了
			ArrayList<String> xVals = new ArrayList<String>();
			// 从 0 到 count设置x轴的数据
			for (int i = 0; i < count; i++) {
				xVals.add(humidity_time_arraylist.get(i) + "");
			}
			// 这个是y轴的数据
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			// 设置y轴的数据，在这里，是用random函数来生成的
			for (int i = 0; i < count; i++) {
				//String mult = climate_arraylist.get(i);
				if (Float.parseFloat(humidity_arraylist.get(i)) < 100) {
					float val = (float) Float.parseFloat(humidity_arraylist.get(i));//(float) // (Math.random() * mult) + 3;// + (float)
					// ((mult *
					// 0.1) / 10);
					yVals.add(new Entry(val, i));
				}
			}
			// MPAC自定义的一种类
			// create a dataset and give it a type
			LineDataSet set1 = new LineDataSet(yVals, "濕度");
			set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);// 設定平滑曲線
			// set1.setFillAlpha(110);
			// set1.setFillColor(Color.RED);

			// set the line to be drawn like this "- - - - - -"
			// 下面是设置线的各种属性
			set1.setColor(Color.BLUE);
			set1.setCircleColor(Color.BLUE);
			set1.setLineWidth(1f);
			set1.setCircleRadius(0f);
			// 把这个设置为true，允许在每个数据圆上画一个洞。
			set1.setDrawCircleHole(false);
			set1.setValueTextSize(2f);
			//

			set1.setDrawFilled(false);


			if (Utils.getSDKInt() >= 18) {
				// fill drawable only supported on api level 18 and above
				//Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
				//set1.setFillDrawable(drawable);
				set1.setFillColor(Color.BLUE);
			} else {
				set1.setFillColor(Color.BLACK);
			}

			ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
			dataSets.add(set1); // add the datasets

			// create a data object with the datasets
			LineData data = new LineData(xVals, dataSets);
			// set data
			humidity_chart.setData(data);
		}

		//endregion
//region CO2_Chart
		private void set_CO2_Data(int count, float range) {
			// 在图表执行动作时，为定制回调设置一个动作监听器。
			CO2_chart.setOnChartGestureListener((OnChartGestureListener) MyApplication.getContext());
			// 为图表设置一个既定的监听器
			CO2_chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) MyApplication.getContext());
			// 把这个设为true来绘制网格背景，如果false则不绘制
			CO2_chart.setDrawGridBackground(true);

			// 把这个设置为false，禁用所有手势和图表上的触摸，默认：true
			CO2_chart.setTouchEnabled(true);

			// 设置图标拖动为允许
			CO2_chart.setDragEnabled(true);
			// 设置图表缩放为允许
			CO2_chart.setScaleEnabled(true);
			CO2_chart.setScaleXEnabled(true);
			CO2_chart.setScaleYEnabled(true);
			CO2_chart.setDescription("");
			CO2_chart.getXAxis().setLabelRotationAngle(0);

			// 挤压缩放设置为允许，即X轴和Y轴会成比例缩放，如果设置为false，则变成单独缩放
			CO2_chart.setPinchZoom(true);

			// 返回代表所有x标签的对象，这个方法可以用来获得XAxis对象并修改它（例如改变标签的位置、样式等）
			XAxis xAxis = CO2_chart.getXAxis();
			// 返回左y轴对象。在水平的柱状图中，这是最上面的轴。
			YAxis leftAxis = CO2_chart.getAxisLeft();
			leftAxis.removeAllLimitLines();
			CO2YAxisValueFormatter custom = new CO2YAxisValueFormatter();
			CO2_chart.getAxisLeft().setValueFormatter(custom);

			// 使网格线在虚线模式下绘制，例如像这个“------”。只有在硬件加速被关闭的情况下才会起作用。记住，硬件加速会提高性能。
			//123
			leftAxis.enableGridDashedLine(0f, 0f, 0f);
			// 将其设置为true，无论是否启用其他网格线，都要画出零线。默认值:假
			//123
			leftAxis.setDrawZeroLine(true);

			// limit lines are drawn behind data (and not on top)
			// 如果这被设置为true，那么界限就会被绘制在实际的数据后面，否则就在上面。默认值:假
			//123
			leftAxis.setDrawLimitLinesBehindData(true);
			//123
			CO2_chart.getAxisRight().setEnabled(false);
			//123
			CO2_chart.getAxisLeft().setEnabled(true);
			//123
			CO2_chart.getXAxis().setEnabled(true);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

			// add data
			// 这是自己设定的添加数据的方法，count设置了数据的个数，range设置了波动范围
			//set_climate_Data(250, 100);

			//123
			// 调用动画对图表显示进行处理
			CO2_chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


			// 这个应该就是x轴的数据了
			ArrayList<String> xVals = new ArrayList<String>();
			// 从 0 到 count设置x轴的数据
			for (int i = 0; i < count; i++) {
				xVals.add(co2_time_arraylist.get(i) + "");
			}
			// 这个是y轴的数据
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			// 设置y轴的数据，在这里，是用random函数来生成的
			for (int i = 0; i < count; i++) {
				//String mult = climate_arraylist.get(i);
				float val = (float) Float.parseFloat(co2_arraylist.get(i));//(float) // (Math.random() * mult) + 3;// + (float)
				// ((mult *
				// 0.1) / 10);
				yVals.add(new Entry(val, i));
			}
			// MPAC自定义的一种类
			// create a dataset and give it a type
			LineDataSet set1 = new LineDataSet(yVals, "CO2");
			set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);// 設定平滑曲線
			// set1.setFillAlpha(110);
			// set1.setFillColor(Color.RED);

			// set the line to be drawn like this "- - - - - -"
			// 下面是设置线的各种属性
			set1.setColor(Color.BLUE);
			set1.setCircleColor(Color.BLUE);
			set1.setLineWidth(1f);
			set1.setCircleRadius(0f);
			// 把这个设置为true，允许在每个数据圆上画一个洞。
			set1.setDrawCircleHole(false);
			set1.setValueTextSize(2f);
			//

			set1.setDrawFilled(false);


			if (Utils.getSDKInt() >= 18) {
				// fill drawable only supported on api level 18 and above
				//Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
				//set1.setFillDrawable(drawable);
				set1.setFillColor(Color.BLUE);
			} else {
				set1.setFillColor(Color.BLACK);
			}

			ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
			dataSets.add(set1); // add the datasets

			// create a data object with the datasets
			LineData data = new LineData(xVals, dataSets);
			// set data
			CO2_chart.setData(data);
		}

		//endregion
//region CH4_Chart
		private void set_CH4_Data(int count, float range) {
			// 在图表执行动作时，为定制回调设置一个动作监听器。
			CH4_chart.setOnChartGestureListener((OnChartGestureListener) MyApplication.getContext());
			// 为图表设置一个既定的监听器
			CH4_chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) MyApplication.getContext());
			// 把这个设为true来绘制网格背景，如果false则不绘制
			CH4_chart.setDrawGridBackground(true);

			// 把这个设置为false，禁用所有手势和图表上的触摸，默认：true
			CH4_chart.setTouchEnabled(true);

			// 设置图标拖动为允许
			CH4_chart.setDragEnabled(true);
			// 设置图表缩放为允许
			CH4_chart.setScaleEnabled(true);
			CH4_chart.setScaleXEnabled(true);
			CH4_chart.setScaleYEnabled(true);
			CH4_chart.setDescription("");
			CH4_chart.getXAxis().setLabelRotationAngle(0);

			// 挤压缩放设置为允许，即X轴和Y轴会成比例缩放，如果设置为false，则变成单独缩放
			CH4_chart.setPinchZoom(true);

			// 返回代表所有x标签的对象，这个方法可以用来获得XAxis对象并修改它（例如改变标签的位置、样式等）
			XAxis xAxis = CH4_chart.getXAxis();
			// 返回左y轴对象。在水平的柱状图中，这是最上面的轴。
			YAxis leftAxis = CH4_chart.getAxisLeft();
			leftAxis.removeAllLimitLines();
			PM25YAxisValueFormatter custom = new PM25YAxisValueFormatter();
			CH4_chart.getAxisLeft().setValueFormatter(custom);
			// 使网格线在虚线模式下绘制，例如像这个“------”。只有在硬件加速被关闭的情况下才会起作用。记住，硬件加速会提高性能。
			//123
			leftAxis.enableGridDashedLine(0f, 0f, 0f);
			// 将其设置为true，无论是否启用其他网格线，都要画出零线。默认值:假
			//123
			leftAxis.setDrawZeroLine(true);

			// limit lines are drawn behind data (and not on top)
			// 如果这被设置为true，那么界限就会被绘制在实际的数据后面，否则就在上面。默认值:假
			//123
			leftAxis.setDrawLimitLinesBehindData(true);
			//123
			CH4_chart.getAxisRight().setEnabled(false);
			//123
			CH4_chart.getAxisLeft().setEnabled(true);
			//123
			CH4_chart.getXAxis().setEnabled(true);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

			// add data
			// 这是自己设定的添加数据的方法，count设置了数据的个数，range设置了波动范围
			//set_climate_Data(250, 100);

			//123
			// 调用动画对图表显示进行处理
			CH4_chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


			// 这个应该就是x轴的数据了
			ArrayList<String> xVals = new ArrayList<String>();
			// 从 0 到 count设置x轴的数据
			for (int i = 0; i < count; i++) {
				xVals.add(ch4_time_arraylist.get(i) + "");
			}
			// 这个是y轴的数据
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			// 设置y轴的数据，在这里，是用random函数来生成的
			for (int i = 0; i < count; i++) {
				//String mult = climate_arraylist.get(i);
				if (Float.parseFloat(ch4_arraylist.get(i)) < 2) {
					float val = (float) Float.parseFloat(ch4_arraylist.get(i));//(float) // (Math.random() * mult) + 3;// + (float)
					// ((mult *
					// 0.1) / 10);
					yVals.add(new Entry(val, i));
				}
			}
			// MPAC自定义的一种类
			// create a dataset and give it a type
			LineDataSet set1 = new LineDataSet(yVals, "CH4");
			set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);// 設定平滑曲線
			// set1.setFillAlpha(110);
			// set1.setFillColor(Color.RED);

			// set the line to be drawn like this "- - - - - -"
			// 下面是设置线的各种属性
			set1.setColor(Color.BLUE);
			set1.setCircleColor(Color.BLUE);
			set1.setLineWidth(1f);
			set1.setCircleRadius(0f);
			// 把这个设置为true，允许在每个数据圆上画一个洞。
			set1.setDrawCircleHole(false);
			set1.setValueTextSize(2f);
			//

			set1.setDrawFilled(false);


			if (Utils.getSDKInt() >= 18) {
				// fill drawable only supported on api level 18 and above
				//Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
				//set1.setFillDrawable(drawable);
				set1.setFillColor(Color.BLUE);
			} else {
				set1.setFillColor(Color.BLACK);
			}

			ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
			dataSets.add(set1); // add the datasets

			// create a data object with the datasets
			LineData data = new LineData(xVals, dataSets);
			// set data
			CH4_chart.setData(data);
		}

		//endregion
		//After
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//            System.out.println("result: " + result);
			try {
				//顯示
				for (int i=0;i<climate_arraylist.size();i++){
					addEntry(climate_chart, climate_arraylist.get(i));
				}

				set_climate_Data(climate_arraylist.size(), 0);
				dialog.dismiss();
//				set_PM25_Data(pm25_arraylist.size(), 0);
//				set_NH3_Data(nh3_arraylist.size(), 0);
//				set_H2S_Data(h2s_arraylist.size(), 0);
//				set_humidity_Data(humidity_arraylist .size(), 0);
//				set_CO2_Data(co2_arraylist .size(), 0);
//				set_CH4_Data(ch4_arraylist .size(), 0);
				//Log.e("set_PM25_Data","開始呼叫");
//-------------------------------------------------------------------------------------------------------------------------

			} catch (Exception e) {
				Log.e("climateChart", e.getMessage());
			}
		}
	}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//endregion


	public class ClimateYAxisValueFormatter implements YAxisValueFormatter {

		private DecimalFormat mFormat;

		@SuppressLint("NewApi")
		public ClimateYAxisValueFormatter() {
			mFormat = new DecimalFormat("###,###,###,##0");
		}

		@SuppressLint("NewApi")
		@Override
		public String getFormattedValue(float value, YAxis yAxis) {
			return mFormat.format(value) + "℃ ";
		}
	}

	public class PM25YAxisValueFormatter implements YAxisValueFormatter {

		private DecimalFormat mFormat;

		@SuppressLint("NewApi")
		public PM25YAxisValueFormatter() {
			mFormat = new DecimalFormat("###,###,###,##0");
		}

		@SuppressLint("NewApi")
		@Override
		public String getFormattedValue(float value, YAxis yAxis) {
			return mFormat.format(value) + " μg / m3";
		}
	}

	public class HumidityYAxisValueFormatter implements YAxisValueFormatter {

		private DecimalFormat mFormat;

		@SuppressLint("NewApi")
		public HumidityYAxisValueFormatter() {
			mFormat = new DecimalFormat("###,###,###,##0");
		}

		@SuppressLint("NewApi")
		@Override
		public String getFormattedValue(float value, YAxis yAxis) {
			return mFormat.format(value) + "%";
		}
	}

	public class CO2YAxisValueFormatter implements YAxisValueFormatter {

		private DecimalFormat mFormat;

		@SuppressLint("NewApi")
		public CO2YAxisValueFormatter() {
			mFormat = new DecimalFormat("###,###,###,##0");
		}

		@SuppressLint("NewApi")
		@Override
		public String getFormattedValue(float value, YAxis yAxis) {
			return mFormat.format(value) + "PPM";
		}
	}

//	//regaion 取整點的方式
//	private String getInitialTime(String time) {
//		String hour = "00";//小时
//		String minutes = "00";//分钟
//		String outTime = "00:00";
//		StringTokenizer st = new StringTokenizer(time, ":");
//		List<String> inTime = new ArrayList<String>();
//		while (st.hasMoreElements()) {
//			inTime.add(st.nextToken());
//		}
//		hour = inTime.get(0).toString();
//		minutes = inTime.get(1).toString();
//		if (Integer.parseInt(minutes) > 30) {
//			hour = (Integer.parseInt(hour) + 1) + "";
//		}
//		outTime = hour + ":00";
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//
//		try {
//			outTime = sdf.format(sdf.parse(outTime));
//		} catch (ParseException e) {
//// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (java.text.ParseException e) {
//			e.printStackTrace();
//		}
//		return outTime;
//	}
	//endregaion

	// 高温线下标
	private final int HIGH = 0;
	// 低温线下标
	//private final int LOW = 1;

	// 初始化图表
	private void initialChart(LineChart mChart) {
		mChart.setDescription("Andy Yeh Github@ https://github.com/YehChunChuan/AndyGitSpaces");
		mChart.setNoDataTextDescription("暫時無數據");
		//觸碰有效
		mChart.setTouchEnabled(true);
		// 可拖曳
		mChart.setDragEnabled(true);
		// 可缩放
		mChart.setScaleEnabled(true);
		mChart.setDrawGridBackground(false);
		mChart.setPinchZoom(true);
		// 设置图表的背景颜色
		mChart.setBackgroundColor(0xfff5f5f5);
		// 图表的注解(只有当数据集存在时候才生效)
		Legend l = mChart.getLegend();
		// 可以修改图表注解部分的位置
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		// 线性，也可是圆
		l.setForm(Legend.LegendForm.LINE);
		// 颜色
		l.setTextColor(Color.CYAN);
		// x坐标轴
		XAxis xl = mChart.getXAxis();
		xl.setTextColor(0xff00897b);
		xl.setDrawGridLines(false);
		xl.setAvoidFirstLastClipping(true);
		// 几个x坐标轴之间才绘制？
		xl.setSpaceBetweenLabels(1);
		// 如果false，那么x坐标轴将不可见
		xl.setEnabled(true);
		// 将X坐标轴放置在底部，默认是在顶部。
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		// 图表左边的y坐标轴线
		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setTextColor(0xff37474f);
		// 最大值
		leftAxis.setAxisMaxValue(50f);
		// 最小值
		leftAxis.setAxisMinValue(-10f);
		// 不一定要从0开始
		//leftAxis.setStartAtZero(false);
		leftAxis.setDrawGridLines(true);
		YAxis rightAxis = mChart.getAxisRight();
		// 不显示图表的右边y坐标轴线
		rightAxis.setEnabled(false);
	}
	// 为LineChart增加LineDataSet
	private void addLineDataSet(LineChart mChart) {
		LineData data = new LineData();

		//高溫
		data.addDataSet(createHighLineDataSet());
		//低溫
//		data.addDataSet(createLowLineDataSet());
		// 数据显示的颜色
		// data.setValueTextColor(Color.WHITE);
		// 先增加一个空的数据，随后往里面动态添加
		mChart.setData(data);
	}
	// 同时为高温线和低温线添加进去一个坐标点
	private void addEntry(LineChart mChart,String humidity) {

		LineData data = mChart.getData();

		data.addXValue((data.getXValCount()) + "");

		// 增加高温
		LineDataSet highLineDataSet = (LineDataSet) data.getDataSetByIndex(HIGH);
		float fh = (float) (Float.parseFloat(humidity));
		Entry entryh = new Entry(fh, highLineDataSet.getEntryCount());
		data.addEntry(entryh, HIGH);

		// 增加低温
//		LineDataSet lowLineDataSet = (LineDataSet) data.getDataSetByIndex(LOW);
//		float fl = (float) ((Math.random()) * 10);
//		Entry entryl = new Entry(fl, lowLineDataSet.getEntryCount());
//		data.addEntry(entryl, LOW);
		mChart.notifyDataSetChanged();
		// 当前统计图表中最多在x轴坐标线上显示的总量
		mChart.setVisibleXRangeMaximum(100);
		mChart.moveViewToX(data.getXValCount() - 100);
	}
	// 初始化数据集，添加一条高温统计折线
	private LineDataSet createHighLineDataSet() {

		LineDataSet set = new LineDataSet(null, "溫度");
		set.setAxisDependency(YAxis.AxisDependency.LEFT);

		// 折线的颜色
		set.setColor(Color.RED);
		set.setCircleColor(Color.YELLOW);
		set.setLineWidth(5f);
		set.setCircleSize(10f);
		// set.setFillAlpha(128);
		set.setCircleColorHole(Color.BLUE);
		set.setHighLightColor(Color.GREEN);
		set.setValueTextColor(Color.RED);
		set.setValueTextSize(10f);
		set.setDrawValues(true);

		set.setValueFormatter(new ValueFormatter() {
			@RequiresApi(api = Build.VERSION_CODES.N)
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
											ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat(".0℃");
				String s = "溫度" + decimalFormat.format(value);
				return s;
			}
		});

		return set;
	}


//	// 初始化数据集，添加一条低温统计折线
//	private LineDataSet createLowLineDataSet() {
//
//		LineDataSet set = new LineDataSet(null, "低温");
//		set.setAxisDependency(YAxis.AxisDependency.LEFT);
//
//		// 折线的颜色
//		set.setColor(ColorTemplate.getHoloBlue());
//		set.setCircleColor(Color.BLUE);
//		set.setLineWidth(1f);
//		set.setCircleSize(10f);
//		// set.setFillAlpha(128);
//		// set.setFillColor(ColorTemplate.getHoloBlue());
//		set.setHighLightColor(Color.DKGRAY);
//		set.setValueTextColor(Color.BLACK);
//		set.setCircleColorHole(Color.RED);
//		set.setValueTextSize(15f);
//		set.setDrawValues(true);
//
//		set.setValueFormatter(new ValueFormatter() {
//			@RequiresApi(api = Build.VERSION_CODES.N)
//			@Override
//			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
//											ViewPortHandler viewPortHandler) {
//				DecimalFormat decimalFormat = new DecimalFormat(".0℃");
//				String s = "低温" + decimalFormat.format(value);
//				return s;
//			}
//		});
//
//		return set;
//	}
}
