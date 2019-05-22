package com.fragment.fragmenttest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.SQLException;
import android.graphics.Color;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.StringTokenizer;


public class HistoricalChartFragment extends Fragment {

	private String value = "";
	private LineChart climate_chart;
	private LineChart chart2;


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

	private  WebView mWebView;
	private ProgressDialog dialog;
	private IdentityHashMap<String,Object> sensor_data_hash_map=null;


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
            tine=100000000;
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			now=new Date();
            beforDate = new Date(now.getTime() - tine);
            //System.out.println(sdf.format(beforDate));
			String Time = sdf.format(beforDate);
            Log.e("現在時間減10分鐘:", Time);
            //Toast.makeText(MainActivity.this, "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
            TestClient.execute(Time);

		climate_chart = (LineChart) getView().findViewById(R.id.chart1);
		chart2 = (LineChart) getView().findViewById(R.id.chart2);
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
                    record_id_arry[ID] =record_id;// "RID:" + record_id;
                    String sensor_id = rs.getString("sensor_id");
                    sensor_id_arry[ID] = sensor_id;;//"SID:" + sensor_id;
                    String sensor_category = rs.getString("sensor_category");
                    sensor_category_arry[ID] =  sensor_category;//"SCAT:" + sensor_category;
                    String sensor_value = rs.getString("sensor_value");
                    sensor_value_arry[ID] = sensor_value;//"SV:" + sensor_value;
                    String read_time = rs.getString("read_time");
                    read_time_arry[ID] =  read_time;//"RT:" + read_time;
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
                    for (int i = 0; i < ID; i+=1000) {
						//Log.e("IdentityHashMap", sensor_category_arry[i] + ":" + sensor_value_arry[i]);//String.valueOf(i));
						//sensor_data_hash_map.put(sensor_category_arry[i], sensor_value_arry[i]);
						if (sensor_category_arry[i].equals("climate")) {
							//climate_array[i] = sensor_value_arry[i];
							climate_arraylist.add(sensor_value_arry[i]);
							climate_time_arraylist.add(read_time_arry[i]);
							Log.e("climate_arraylist+i:"+i,sensor_value_arry[i]);
						} else if (sensor_category_arry[i].equals("pm2.5")) {
							//pm25_arry[i] =sensor_value_arry[i];
							pm25_arraylist.add(sensor_value_arry[i]);
							pm25_time_arraylist.add(read_time_arry[i]);
							Log.e("pm25_arraylist+i:"+i,sensor_value_arry[i]);
						} else if (sensor_category_arry[i].equals("nh3")) {
							//nh3_arry[i] = sensor_value_arry[i];
							nh3_arraylist.add(sensor_value_arry[i]);
							nh3_time_arraylist.add(read_time_arry[i]);
							Log.e("nh3_arraylist+i:"+i,sensor_value_arry[i]);
						} else if (sensor_category_arry[i].equals("h2s")) {
							//h2s_arry[i] = sensor_value_arry[i];
							h2s_arraylist.add(sensor_value_arry[i]);
							h2s_time_arraylist.add(read_time_arry[i]);
							Log.e("h2s_arraylist+i:"+i,sensor_value_arry[i]);
						} else if (sensor_category_arry[i].equals("humidity")) {
							//humidity_arry[i] = sensor_value_arry[i];
							humidity_arraylist.add(sensor_value_arry[i]);
							humidity_time_arraylist.add(read_time_arry[i]);
							Log.e("humidity_arraylist+i"+i,sensor_value_arry[i]);
						} else if (sensor_category_arry[i].equals("co2")) {
							//co2_arry[i] = String.valueOf(sensor_value_arry[i]);
							co2_arraylist.add(sensor_value_arry[i]);
							co2_time_arraylist.add(read_time_arry[i]);
							Log.e("co2_arraylist+i"+i,sensor_value_arry[i]);
						} else if (sensor_category_arry[i].equals("ch4")) {
							//ch4_arry[i] =sensor_value_arry[i];
							ch4_arraylist.add(sensor_value_arry[i]);
							ch4_time_arraylist.add(read_time_arry[i]);
							Log.e("ch4_arraylist+i"+i,sensor_value_arry[i]);
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

			// 挤压缩放设置为允许，即X轴和Y轴会成比例缩放，如果设置为false，则变成单独缩放
			climate_chart.setPinchZoom(true);

			// 返回代表所有x标签的对象，这个方法可以用来获得XAxis对象并修改它（例如改变标签的位置、样式等）
			XAxis xAxis = climate_chart.getXAxis();
			// 返回左y轴对象。在水平的柱状图中，这是最上面的轴。
			YAxis leftAxis = climate_chart.getAxisLeft();
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
				float val = (float) Float.parseFloat(climate_arraylist.get(i));//(float) // (Math.random() * mult) + 3;// + (float)
				// ((mult *
				// 0.1) / 10);
				yVals.add(new Entry(val, i));
			}
			// MPAC自定义的一种类
			// create a dataset and give it a type
			LineDataSet set1 = new LineDataSet(yVals, "溫度");
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
			set1.setValueTextSize(5f);
			//

			set1.setDrawFilled(false);


			if(Utils.getSDKInt() >= 18) {
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
			LineData data = new LineData(xVals,dataSets);
			// set data
			climate_chart.setData(data);
		}

        //After
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            System.out.println("result: " + result);
            try {
				//顯示
				set_climate_Data(climate_arraylist.size(), 0);
//-------------------------------------------------------------------------------------------------------------------------
//
//                //定義 ListView 每個 Item 的資料
//                List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
//                ID = 0;
//
//                for (int i = 0; i < record_id_arry.length; i++) {
//                    Map<String, Object> item = new HashMap<String, Object>();
//                    item.put("record_id", record_id_arry[i]);
//                    item.put("sensor_id", sensor_id_arry[i]);
//                    item.put("sensor_category", sensor_category_arry[i]);
//                    item.put("sensor_value", sensor_value_arry[i]);
//                    item.put("read_time", read_time_arry[i]);
//                    itemList.add(item);
//                }
//
//                dialog.dismiss();
				//溫度
				//set_climate_Data(250, 100);
//				try {
//					XAxis xAxis = chart1.getXAxis();
//					xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//					xAxis.setGranularity(1f);
//					xAxis.setLabelCount(12, true);
//					xAxis.setAxisMinimum(0f);
//					xAxis.setAxisMaximum(20f);
//					//顯示邊界
//					chart1.setDrawBorders(true);
//					List<Entry> entries = new ArrayList<>();
//					for (int i = 0; i < climate_arraylist.size(); i++) {
//						//ClimateChart=Float.parseFloat(climate_arraylist.get(i));
//						entries.add(new Entry(i, Float.parseFloat(climate_arraylist.get(i))));
//					}
////			for (int i = 0; i < 10; i++) {
////				entries.add(new Entry(i, (float) (Math.random()) * 80));
////			}
//					//一個LineDataSet就是一條線
//					LineDataSet lineDataSet = new LineDataSet(entries, "溫度");
//					LineData data = new LineData(lineDataSet);
//					chart1.setData(data);
				} catch (Exception e) {
					Log.e("climateChart", e.getMessage());
				}

//				xAxis.setValueFormatter(new IAxisValueFormatter() {
//					@Override
//					public String getFormattedValue(float value, AxisBase axis) {
//						return rs.get((int) value); //mList為存有月份的集合
//						return rs.get((int) value); //mList為存有月份的集合
//					}
//				});

////---------------------------------------------------------------------------------------------------------------------------------------------
//			} catch (Exception e) {
//                e.printStackTrace();
//                //Toast.makeText(MyApplication.getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
//            }
                                                                                                                                                                                                     }

//        @Override
//        protected void onProgressUpdate(String...            values) {
//            super.onProgressUpdate(values);
//            try {
//                //顯示
//                //text1.append(values.toString());
////                if (!values.equals("1")) {
////                    Toast.makeText(getApplicationContext(), "Hello admin!", Toast.LENGTH_SHORT).show();
////                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(getApplicationContext(), "Seems like you 're not admin!", Toast.LENGTH_SHORT).show();
////                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(MyApplication.getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
//            }

        }

//        @Override
//        protected void onCancelled(String s) {
//            super.onCancelled(s);
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }

//    }
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
