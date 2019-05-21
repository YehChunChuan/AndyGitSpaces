package com.fragment.fragmenttest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static android.content.Context.*;
import static android.support.v4.content.ContextCompat.getSystemService;
import static com.fragment.fragmenttest.R.*;


@RequiresApi(api = Build.VERSION_CODES.N)
public class RealTimeFragment extends Fragment {
	
	private String value = "";
	//先宣告
	Activity activity;
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


	View  v;
	TextView textHumidity_Data;
	TextView textCO2_Data;
	TextView textViewCH4_Data;
	TextView textViewTemp_Data;
	TextView textViewPM25_Data;
	TextView textViewNH3_Data;
	TextView textViewH2S_Data;

	TextView textHumidity_Time;
	TextView textCO2_Time;
	TextView textViewCH4_Time;
	TextView textViewTemp_Time;
	TextView textViewPM25_Time;
	TextView textViewNH3_Time;
	TextView textViewH2S_Time;

	ImageView imagTemp;
	ImageView imagPM25;
	ImageView imagNH3;
	ImageView imageH2S;
	ImageView imagHumidity;
	ImageView imagCO2;
	ImageView imagCH4;


	Double climateD;
	Double pm25D;
	Double nh3D;
	Double h2sD;
	Double humidityD;
	Double co2D;
	Double ch4D;

	String climateT;
	String pm25T;
	String nh3T;
	String h2sT;
	String humidityT;
	String co2T;
	String ch4T;
	Boolean ThreadFalse=true;

	//private Handler handler;

	//找到UI工人的經紀人，這樣才能派遣工作  (找到顯示畫面的UI Thread上的Handler)
	private Handler mUI_Handler = new Handler();
	//宣告特約工人的經紀人
	private Handler mThreadHandler;
	//宣告特約工人
	private HandlerThread mThread;
	MainActivity AC;

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		Log.d("=====>", "RealTimeFragment onAttach");
		MainActivity mainActivity = (MainActivity)activity;
		value = mainActivity.getRealTimeData();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AC = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("=====>", "RealTimeFragment onCreateView");
		tine=5000;
		now = new Date();
		beforDate = new Date(now.getTime() - tine);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(beforDate));
		Log.e("現在時間減10分鐘:", sdf.format(beforDate));
		//Toast.makeText(getActivity().getApplicationContext(), "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();

//		v = inflater.inflate(R.layout.frg_realtime, container, false);
//		textHumidity_Data = (TextView)v.findViewById(R.id.textHumidity_Data);
//		textHumidity_Data.setText("幹");
//		return v;


		return inflater.inflate(layout.frg_realtime, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("=====>", "RealTimeFragment onActivityCreated");
//		TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
//		txtResult.setText(value);

		FragmentActivity fragmentActivity = getActivity();
		//if (fragmentActivity instanceof MainActivity)
		{
			MainActivity activity = (MainActivity) fragmentActivity;
		}

		//聘請一個特約工人，有其經紀人派遣其工人做事 (另起一個有Handler的Thread)
		mThread = new HandlerThread("name");
		//讓Worker待命，等待其工作 (開啟Thread)
		mThread.start();
		//找到特約工人的經紀人，這樣才能派遣工作 (找到Thread上的Handler)
		mThreadHandler=new Handler(mThread.getLooper());
		//請經紀人指派工作名稱 r，給工人做
		mThreadHandler.post(r1);

//region 使用UIthread跑資料庫
//		getActivity().runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				Toast.makeText(getActivity().getApplicationContext(),"any mesage",Toast.LENGTH_LONG).show();
//
//				try {
//					// 进行网络请求
//					getActivity().runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							// 更新UI
//							//Toast.makeText(getActivity().getApplicationContext(),"any mesage",Toast.LENGTH_SHORT).show();
//
////------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//							Connection con = null;
//							PreparedStatement ps = null;
//							ResultSet rs = null;
//							String count = "0";
//							Statement st = null;
//							StringBuilder stringBuilder = new StringBuilder();
//
//							try {
//
//								tine=60000;
//
//								beforDate = new Date(now.getTime() - tine);
//								System.out.println(sdf.format(beforDate));
//								Log.e("現在時間減10分鐘:", sdf.format(beforDate));
//								//Toast.makeText(getContext().getApplicationContext(), "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
//								//STEP 3: Open a connection
//								System.out.println("Connecting to database...");
//								Class.forName("org.postgresql.Driver");
//								Log.e("我要連線!!!", "我要連線!!!");
//								//DriverManager.setLoginTimeout(5);
//								//jdbc:postgresql://140.116.247.120:36432/happig幹happig_read幹HappigRead幹
//								con = DriverManager.getConnection(
//										"jdbc:postgresql://140.116.247.120:36432/happig",
//										"happig_read",
//										"HappigRead");
//
//								if (con != null && !con.isClosed()) {
//									System.out.println("資料庫連線測試成功！");
//									//con.close();
//								}
//
////-----------------------------------------------------------------------------------------------------------------------------------------------
//
//
////            //STEP 4: Execute a query
//								//2019-04-26 00:00:00
//								System.out.println("Creating statement...");
//								//st = con.createStatement();
//
//								String sql;
//								sql = "select * from sensor_records where read_time>='" + beforDate + "' order by read_time desc";
//
//								//ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
////            //STEP 5: Extract data from result set
//								Log.e("executeQuery!", sql);
//								//rs = ps.executeQuery(sql);
//
//
//								//改成可以雙向滾動，但不及時更新，就是如果數據庫裡的數據修改過，
//								//並不修改ResultSet記錄 必須要改成 TYPE_SCROLL_INSENSITIVE
//								ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//										ResultSet.CONCUR_READ_ONLY);
//								//执行查询
//								rs = ps.executeQuery();
//
//
//								int size = 0;
//								if (rs != null) {
//									rs.last();    // moves cursor to the last row
//									size = rs.getRow(); // get row id
//								}
//
//								ID = 0;
//								record_id_arry = new String[size];
//								sensor_id_arry = new String[size];
//								sensor_category_arry = new String[size];
//								sensor_value_arry = new String[size];
//								read_time_arry = new String[size];
//								rs.first();
//
//								while (rs.next()) {
//									//Retrieve by column name
//									String record_id = rs.getString("record_id");
//									record_id_arry[ID] = "RID:" + record_id;
//
//									String sensor_id = rs.getString("sensor_id");
//									sensor_id_arry[ID] = "SID:" + sensor_id;
//
//									String sensor_category = rs.getString("sensor_category");
//									sensor_category_arry[ID] = "SCAT:" + sensor_category;
//
//									String sensor_value = rs.getString("sensor_value");
//									sensor_value_arry[ID] = "SV:" + sensor_value;
//
//									String read_time = rs.getString("read_time");
//									read_time_arry[ID] = "RT:" + read_time;
////                //Display values
//									Log.e("record_id!", record_id);
//									Log.e("sensor_id!", sensor_id);
//									Log.e("sensor_category!", sensor_category);
//									Log.e("sensor_value!", sensor_value);
//									Log.e("read_time!", read_time);
//									String TTV = "";
//									TTV = TTV + "record_id:" + record_id + "\n" + "sensor_id:" + sensor_id + "\n" + "sensor_category:" + sensor_category + "\n" + "sensor_value:" + sensor_value + "\n" + "read_time:" + read_time;
//									stringBuilder.append(TTV).append("\n");
//									ID += 1;
//								}
////------------------------------------------------------------------------------------------------------------------------------------------------
//
//								//System.out.println(ps);
//								// Log.e("ps!", ps.toString());
//								//return   ;
//							} catch (SQLException e) {
//								Log.e("SQLException有例外!", e.getMessage());
//								//Toast.makeText(getActivity().getApplicationContext(), "No SQLException有例外!"+e.getMessage(), Toast.LENGTH_SHORT).show();
//							} catch (Exception e) {
//								Log.e("Exception有例外!", e.getMessage());
//								//Toast.makeText(getActivity().getApplicationContext(), "No SQLException有例外!"+e.getMessage(), Toast.LENGTH_SHORT).show();
//							}
//							//return stringBuilder.toString();
////-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//						}
//					});
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//			}
//		});
//endregion
////region Thread跑資料庫
//
//		Thread thread;
//		thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (!Thread.interrupted()) { // 非阻塞过程中通过判断中断标志来退出
//					try {
//						// 进行网络请求
//						getActivity().runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								// 更新UI
//								Toast.makeText(getActivity().getApplicationContext(),"any mesage",Toast.LENGTH_SHORT).show();
//
////------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//								Connection con = null;
//								PreparedStatement ps = null;
//								ResultSet rs = null;
//								String count = "0";
//								Statement st = null;
//								StringBuilder stringBuilder = new StringBuilder();
//
//								try {
//
//									tine = 60000;
//
//									beforDate = new Date(now.getTime() - tine);
//									System.out.println(sdf.format(beforDate));
//									Log.e("現在時間減10分鐘:", sdf.format(beforDate));
//									//Toast.makeText(getContext().getApplicationContext(), "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
//									//STEP 3: Open a connection
//									System.out.println("Connecting to database...");
//									Class.forName("org.postgresql.Driver");
//									Log.e("我要連線!!!", "我要連線!!!");
//									//DriverManager.setLoginTimeout(5);
//									//jdbc:postgresql://140.116.247.120:36432/happig幹happig_read幹HappigRead幹
//									con = DriverManager.getConnection(
//											"jdbc:postgresql://140.116.247.120:36432/happig",
//											"happig_read",
//											"HappigRead");
//
//									if (con != null && !con.isClosed()) {
//										System.out.println("資料庫連線測試成功！");
//										//con.close();
//									}
////-----------------------------------------------------------------------------------------------------------------------------------------------
//
//
////            //STEP 4: Execute a query
//									//2019-04-26 00:00:00
//									System.out.println("Creating statement...");
//									//st = con.createStatement();
//
//									String sql;
//									sql = "select * from sensor_records where read_time>='" + beforDate + "' order by read_time desc";
//
//									//ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
////            //STEP 5: Extract data from result set
//									Log.e("executeQuery!", sql);
//									//rs = ps.executeQuery(sql);
//
//
//									//改成可以雙向滾動，但不及時更新，就是如果數據庫裡的數據修改過，
//									//並不修改ResultSet記錄 必須要改成 TYPE_SCROLL_INSENSITIVE
//									ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//											ResultSet.CONCUR_READ_ONLY);
//									//执行查询
//									rs = ps.executeQuery();
//
//
//									int size = 0;
//									if (rs != null) {
//										rs.last();    // moves cursor to the last row
//										size = rs.getRow(); // get row id
//									}
//
//									ID = 0;
//									record_id_arry = new String[size];
//									sensor_id_arry = new String[size];
//									sensor_category_arry = new String[size];
//									sensor_value_arry = new String[size];
//									read_time_arry = new String[size];
//									rs.first();
//
//									while (rs.next()) {
//										//Retrieve by column name
//										String record_id = rs.getString("record_id");
//										record_id_arry[ID] = "RID:" + record_id;
//
//										String sensor_id = rs.getString("sensor_id");
//										sensor_id_arry[ID] = "SID:" + sensor_id;
//
//										String sensor_category = rs.getString("sensor_category");
//										sensor_category_arry[ID] = "SCAT:" + sensor_category;
//
//										String sensor_value = rs.getString("sensor_value");
//										sensor_value_arry[ID] = "SV:" + sensor_value;
//
//										String read_time = rs.getString("read_time");
//										read_time_arry[ID] = "RT:" + read_time;
////                //Display values
//										Log.e("record_id!", record_id);
//										Log.e("sensor_id!", sensor_id);
//										Log.e("sensor_category!", sensor_category);
//										Log.e("sensor_value!", sensor_value);
//										Log.e("read_time!", read_time);
//										String TTV = "";
//										TTV = TTV + "record_id:" + record_id + "\n" + "sensor_id:" + sensor_id + "\n" + "sensor_category:" + sensor_category + "\n" + "sensor_value:" + sensor_value + "\n" + "read_time:" + read_time;
//										stringBuilder.append(TTV).append("\n");
//										ID += 1;
//									}
////------------------------------------------------------------------------------------------------------------------------------------------------
//
//									//System.out.println(ps);
//									// Log.e("ps!", ps.toString());
//									//return   ;
//								} catch (SQLException e) {
//									Log.e("SQLException有例外!", e.getMessage());
//									//Toast.makeText(getActivity().getApplicationContext(), "No SQLException有例外!"+e.getMessage(), Toast.LENGTH_SHORT).show();
//								} catch (Exception e) {
//									Log.e("Exception有例外!", e.getMessage());
//									//Toast.makeText(getActivity().getApplicationContext(), "No SQLException有例外!"+e.getMessage(), Toast.LENGTH_SHORT).show();
//								}
//								//return stringBuilder.toString();
////-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//							}
//						});
//						Thread.sleep(10000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//						break; // 阻塞过程捕获中断异常来退出，执行break跳出循环
//					}
//				}
//			}
//		});
//		thread.start();
//endregion
		textHumidity_Data=(TextView) getView().findViewById(id.textHumidity_Data);
		textCO2_Data=(TextView) getView().findViewById(id.textCO2_Data);
		textViewCH4_Data=(TextView) getView().findViewById(id.textViewCH4_Data);
		textViewTemp_Data=(TextView) getView().findViewById(id.textViewTemp_Data);
		textViewPM25_Data=(TextView) getView().findViewById(id.textViewPM25_Data);
		textViewNH3_Data=(TextView) getView().findViewById(id.textViewNH3_Data);
		textViewH2S_Data=(TextView) getView().findViewById(id.textViewH2S_Data);

		textHumidity_Time=(TextView) getView().findViewById(id.textHumidity_Date);
		textCO2_Time=(TextView) getView().findViewById(id.textCO2_Date);
		textViewCH4_Time=(TextView) getView().findViewById(id.textCH4_Date);
		textViewTemp_Time=(TextView) getView().findViewById(id.textTemp_Date);
		textViewPM25_Time=(TextView) getView().findViewById(id.textPM25_Date);
		textViewNH3_Time=(TextView) getView().findViewById(id.textNH3_Date);
		textViewH2S_Time=(TextView) getView().findViewById(id.textH2S_Date);

		imagTemp = (ImageView) getView().findViewById(id.imagTemp);
		imagPM25 = (ImageView) getView().findViewById(id.imagPM25);
		imagNH3 = (ImageView) getView().findViewById(id.imagNH3);
		imageH2S = (ImageView) getView().findViewById(id.imageH2S);
		imagHumidity = (ImageView) getView().findViewById(id.imagHumidity);
		imagCO2 = (ImageView) getView().findViewById(id.imagCO2);
		imagCH4 = (ImageView) getView().findViewById(id.imagCH4);
	}

	//NumberFormat ddf1=NumberFormat.getNumberInstance() ;
	DecimalFormat df   = new DecimalFormat("####.000");
	@SuppressLint("HandlerLeak")
	public Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//ddf1.setMaximumFractionDigits(4);
			if(msg!=null){
				switch (msg.what) {
					// 根据what字段辨识出消息
					case 1:
						// 取出数据包，解包得数据
						Bundle bundle = msg.getData();
						climate2=bundle.getString("climate");
						pm252=bundle.getString("pm25");
						nh32=bundle.getString("nh3");
						h2s2=bundle.getString("h2s");
						humidity2=bundle.getString("humidity");
						co22=bundle.getString("co2");
						ch42=bundle.getString("ch4");

						climateT=bundle.getString("climateT");
						pm25T=bundle.getString("pm25T");
						nh3T=bundle.getString("nh3T");
						h2sT=bundle.getString("h2sT");
						humidityT=bundle.getString("humidityT");
						co2T=bundle.getString("co2T");
						ch4T=bundle.getString("ch4T");
						try {
							 climateD= Double.parseDouble(df.format(Double.parseDouble(climate2)));
							 pm25D= Double.parseDouble(df.format(Double.parseDouble(pm252)));
							 nh3D= Double.parseDouble(df.format(Double.parseDouble(nh32)));
							 h2sD= Double.parseDouble(df.format(Double.parseDouble(h2s2)));
							 humidityD= Double.parseDouble(df.format(Double.parseDouble(humidity2)));
							 co2D= Double.parseDouble(df.format(Double.parseDouble(co22)));
							 ch4D= Double.parseDouble(df.format(Double.parseDouble(ch42)));
						}catch (Exception ex){
							//.makeText(getActivity().getApplicationContext(),"轉換Message有錯誤",Toast.LENGTH_SHORT).show();
							Log.e("轉換Message有錯誤",ex.getMessage());
						}
						try {
							if (climateD>25){imagTemp.setImageResource(R.drawable.red);}
							if (pm25D>=30){imagPM25.setImageResource(R.drawable.red);}
							if (nh3D>=5){imagNH3.setImageResource(R.drawable.red);}
							if (h2sD>=0.2){imageH2S.setImageResource(R.drawable.red);}
							if (humidityD>75){imagHumidity.setImageResource(R.drawable.red);}
							if (co2D>=900){imagCO2.setImageResource(R.drawable.red);}
							if (ch4D>=2){imagCH4.setImageResource(R.drawable.red);}
						}catch (Exception ex){
							//Toast.makeText(getActivity().getApplicationContext(),"判別Message有錯誤",Toast.LENGTH_SHORT).show();
							Log.e("判別Message有錯誤",ex.getMessage());
						}
						try {
							textHumidity_Data.setText(String.format("%.1f", humidityD));
							textCO2_Data.setText(String.format("%.0f", co2D));
							textViewCH4_Data.setText(String.format("%.3f", ch4D));
							textViewTemp_Data.setText(String.format("%.1f", climateD));
							textViewPM25_Data.setText(String.format("%.1f", pm25D));
							textViewNH3_Data.setText(String.format("%.3f", nh3D));
							textViewH2S_Data.setText(String.format("%.1f", h2sD));
							textHumidity_Time.setText(humidityT);
							textCO2_Time.setText(co2T);
							textViewCH4_Time.setText(ch4T);
							textViewTemp_Time.setText(climateT);
							textViewPM25_Time.setText(pm25T);
							textViewNH3_Time.setText(nh3T);
							textViewH2S_Time.setText(h2s);
						}catch (Exception ex){
							//Toast.makeText(getActivity().getApplicationContext(),"顯示Message有錯誤",Toast.LENGTH_SHORT).show();
							Log.e("顯示Message有錯誤",ex.getMessage());
						}
						break;

					default:
						break;
				}
			}
		}
	};

	//工作名稱 r1 的工作內容
	private  Runnable r1=new Runnable () {

		public void run() {

			// TODO Auto-generated method stub
			//做了很多事
			tine = 10000;
			//老闆指定每隔幾秒要做一次工作1 (單位毫秒:1000等於1秒)
			mThreadHandler.postDelayed(this, tine);
			now = new Date();
			beforDate = new Date(now.getTime() - tine);
			System.out.println(sdf.format(beforDate));
			Log.e("現在時間減" + tine + "毫秒鐘:", sdf.format(beforDate));
			//Toast.makeText(getContext(), "現在時間減"+tine+"毫秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String count = "0";
			Statement st = null;
			StringBuilder stringBuilder = new StringBuilder();
			try {
				//STEP 3: Open a connection
				System.out.println("Connecting to database...");
				Class.forName("org.postgresql.Driver");
				Log.e("我要連線!!!", "我要連線!!!");
				//DriverManager.setLoginTimeout(5);
				//jdbc:postgresql://140.116.247.120:36432/happig幹happig_read幹HappigRead幹
				con = DriverManager.getConnection(
						"jdbc:postgresql://140.116.247.120:36432/happig",
						"happig_read",
						"HappigRead");

				if (con != null && !con.isClosed()) {
					System.out.println("資料庫連線測試成功！");
					//con.close();
				}
//-----------------------------------------------------------------------------------------------------------------------------------------------
//            //STEP 4: Execute a query
				//2019-04-26 00:00:00
				System.out.println("Creating statement...");
				//st = con.createStatement();

				String sql;
				sql = "select * from sensor_records where read_time>='" + beforDate + "' order by read_time desc";

				//ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

//            //STEP 5: Extract data from result set
				Log.e("executeQuery!", sql);
				//rs = ps.executeQuery(sql);


				//改成可以雙向滾動，但不及時更新，就是如果數據庫裡的數據修改過，
				//並不修改ResultSet記錄 必須要改成 TYPE_SCROLL_INSENSITIVE
				ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
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
					if (record_id == null) {
						//Toast.makeText(getActivity().getApplicationContext(), record_id + "調資料庫有錯誤", Toast.LENGTH_SHORT).show();
						Log.e("record_id是空白的!!",record_id);
						break;
					}
					record_id_arry[ID] = /*"RID:" +*/ record_id;
					String sensor_id = rs.getString("sensor_id");
					if (sensor_id == null) {
						//Toast.makeText(getActivity().getApplicationContext(), sensor_id + "調資料庫有錯誤", Toast.LENGTH_SHORT).show();
						Log.e("sensor_id是空白的!!!!",sensor_id);
						break;
					}
					sensor_id_arry[ID] = /*"SID:" +*/ sensor_id;
					String sensor_category = rs.getString("sensor_category");
					if (sensor_category == null) {
						//Toast.makeText(getActivity().getApplicationContext(), sensor_category + "調資料庫有錯誤", Toast.LENGTH_SHORT).show();
						Log.e("sensor_category是空白的!!",sensor_category);
						break;
					}
					sensor_category_arry[ID] = /*"SCAT:" +*/ sensor_category;
					String sensor_value = rs.getString("sensor_value");
					if (sensor_value == null) {
						//Toast.makeText(getActivity().getApplicationContext(), sensor_value + "調資料庫有錯誤", Toast.LENGTH_SHORT).show();
						Log.e("sensor_value是空白的!!",sensor_value);
						break;
					}
					sensor_value_arry[ID] = /*"SV:" +*/ sensor_value;
					String read_time = rs.getString("read_time");
					if (read_time == null) {
						//Toast.makeText(getActivity().getApplicationContext(), read_time + "調資料庫有錯誤", Toast.LENGTH_SHORT).show();
						Log.e("read_time是空白的!!",read_time);
						break;
					}
					read_time_arry[ID] = /*"RT:" + */read_time;
//                //Display values
					Log.e("record_id!", record_id);
					Log.e("sensor_id!", sensor_id);
					Log.e("sensor_category!", sensor_category);
					Log.e("sensor_value!", sensor_value);
					Log.e("read_time!", read_time);
					String TTV = "";
					//TTV = TTV + "record_id:" + record_id + "\n" + "sensor_id:" + sensor_id + "\n" + "sensor_category:" + sensor_category + "\n" + "sensor_value:" + sensor_value + "\n" + "read_time:" + read_time;
//                        stringBuilder.append(TTV).append("\n");
					ID += 1;
				}

				HashMap<String, String> sensor_data_hash_map = new HashMap<String, String>();
				HashMap<String, String> sensor_time_hash_map = new HashMap<String, String>();
				for (int i = 0; i < 9; i++) {
					if ((sensor_category_arry[i] != null) && (sensor_value_arry[i] != null)) {
						sensor_data_hash_map.put(sensor_category_arry[i], sensor_value_arry[i]);
						sensor_time_hash_map.put(sensor_category_arry[i], read_time_arry[i]);
					}
				}
				try {
					climate = sensor_data_hash_map.get("climate");
					pm25 = sensor_data_hash_map.get("pm2.5");
					nh3 = sensor_data_hash_map.get("nh3");
					h2s = sensor_data_hash_map.get("h2s");
					humidity = sensor_data_hash_map.get("humidity");
					co2 = sensor_data_hash_map.get("co2");
					ch4 = sensor_data_hash_map.get("ch4");

					climate_time = sensor_time_hash_map.get("climate");
					pm25_time = sensor_time_hash_map.get("pm2.5");
					nh3_time = sensor_time_hash_map.get("nh3");
					h2s_time = sensor_time_hash_map.get("h2s");
					humidity_time = sensor_time_hash_map.get("humidity");
					co2_time = sensor_time_hash_map.get("co2");
					ch4_time = sensor_time_hash_map.get("ch4");


				} catch (Exception ex) {
					//Toast.makeText(getActivity().getApplicationContext(), "取得Message有錯誤", Toast.LENGTH_SHORT).show();
					Log.e("取得Message有錯誤", ex.getMessage());
				}

				//activity.findViewById(R.id.textHumidity_Data);
				//activity.Reflase
				//textHumidity_Data.setText(humidity);
				//textHumidity_Data.setText(humidity.toString());
				//textCO2_Data.setText(co2.toString());
				//textViewCH4_Data.setText(ch4.toString());
				//textViewTemp_Data.setText(climate.toString());
				//textViewPM25_Data.setText(pm25.toString());
				//textViewNH3_Data.setText(nh3.toString());
				//textViewH2S_Data.setText(h2s.toString());

				try {
					// 创建Message及Bundle对象
					Message msg = new Message();
					Bundle bundle = new Bundle();
					// bundle填充数据
					bundle.putString("climate", climate);
					bundle.putString("pm25", pm25);
					bundle.putString("nh3", nh3);
					bundle.putString("h2s", h2s);
					bundle.putString("humidity", humidity);
					bundle.putString("co2", co2);
					bundle.putString("ch4", ch4);

					bundle.putString("climateT", climate_time);
					bundle.putString("pm25T", pm25_time);
					bundle.putString("nh3T", nh3_time);
					bundle.putString("h2sT", h2s_time);
					bundle.putString("humidityT", humidity_time);
					bundle.putString("co2T", co2_time);
					bundle.putString("ch4T", ch4_time);

					// 设置msg的what字段及数据源
					msg.what = 1;
					msg.setData(bundle);
					// 发送Message
					handler.sendMessage(msg);
				} catch (Exception ex) {
					//Toast.makeText(getActivity().getApplicationContext(), "放置Message有錯誤", Toast.LENGTH_SHORT).show();
					Log.e("放置Message有錯誤", ex.getMessage());
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

		}

	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 1:
					//處理少量資訊或UI
					break;
			}
		}
	};



	//----------------------------------------------------------------------------------------
        //if (checkNetWork()) {
		//Toast.makeText(getApplicationContext(), "MainActivity", Toast.LENGTH_LONG).show();
		//PostgresqlTask TestClient = new PostgresqlTask();
		//TestClient.execute("2019-04-29 10:00:00");300000   600000   10000
//		tine=60000;
//
//		beforDate = new Date(now.getTime() - tine);
//		System.out.println(sdf.format(beforDate));
//		Log.e("現在時間減10分鐘:", sdf.format(beforDate));
//		Toast.makeText(MainActivity.this, "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
		//TestClient.execute(sdf.format(beforDate));

//----------------------------------------------------------------------------------------
//		dialog = ProgressDialog.show(MainActivity.this, "",
//				"Loading. Please wait...", false, false);

		//ProgressDialog pd3 = ProgressDialog.show(this, "提示", "正在登陆中", false, true);
//	}










//	//自訂showToastByRunnable方法
//	private void showToastByRunnable(final Context context, final CharSequence text, final int duration)     {
//		Handler handler = new Handler(Looper.getMainLooper());
//		handler.post(new Runnable() {
//			@Override
//			public void run() {
//				Toast.makeText(context, text, duration).show();
//			}
//		});
//	}










//	private boolean checkNetWork() {
//		ConnectivityManager mConnectivityManager =
//				(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//
//		if (mNetworkInfo != null) {
////            //網路是否已連線
////            mNetworkInfo.isConnected();
////            txtViewNetWork.append("" +mNetworkInfo.isConnected()+"\n");
////            //網路連線方式名稱
//			mNetworkInfo.getTypeName();
//			Toast.makeText(BaseApplication.getContext(), mNetworkInfo.getTypeName(), Toast.LENGTH_LONG).show();
//			//******************************************************************
//			////xtViewNetWork.setText("" + mNetworkInfo.getTypeName()+"\n");
//			//******************************************************************
////            //網路連線狀態
//			mNetworkInfo.getState();
////            txtViewNetWork.append("" + mNetworkInfo.getState()+"\n");
////            //網路是否可使用
////            mNetworkInfo.isAvailable();
////            txtViewNetWork.append("" + mNetworkInfo.isAvailable()+"\n");
////            //網路是否已連接or連線中
////            mNetworkInfo.isConnectedOrConnecting();
////            txtViewNetWork.append("" + mNetworkInfo.isConnectedOrConnecting()+"\n");
////            //網路是否故障有問題
////            mNetworkInfo.isFailover();
////            txtViewNetWork.append("" + mNetworkInfo.isFailover()+"\n");
////            //網路是否在漫遊模式
////            mNetworkInfo.isRoaming();
////            txtViewNetWork.append("" + mNetworkInfo.isRoaming()+"\n");
////            //網路詳細狀態
//			mNetworkInfo.getDetailedState();
////            txtViewNetWork.append("" + mNetworkInfo.getDetailedState()+"\n");
////            //網路狀態詳細資訊
////            mNetworkInfo.getExtraInfo();
////            txtViewNetWork.append("" + mNetworkInfo.getExtraInfo()+"\n");
////            //網路出錯時的原因回報:
////            mNetworkInfo.getReason();
////            txtViewNetWork.append("" + mNetworkInfo.getReason()+"\n");
//			//mWebView.loadUrl("http://nikeru8.blogspot.tw/");
//		} else {
//			new AlertDialog.Builder(BaseApplication.getContext()).setMessage("阿是不會開網路逆!?")
//					.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialogInterface, int i) {
//							Intent callNetSettingIntent = new Intent(
//									android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
//							Toast.makeText(BaseApplication.getContext(), "請前往開啟網路", Toast.LENGTH_LONG).show();
//							//startActivity(callNetSettingIntent);
//						}
//					})
//					.show();
//			return false;
//		}
//		return true;
//	}
//public void Reflase(String V){
//
//	textHumidity_Data.setText(V);
//}


	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
//		Toast.makeText(getActivity().getApplicationContext(), "onResume!!!!" , Toast.LENGTH_LONG).show();
		if(mThread.isInterrupted()) {
			mThread.start();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//Toast.makeText(getActivity().getApplicationContext(), "onPause!!!!" , Toast.LENGTH_LONG).show();
		mThread.quit();
		//mHandler.removeCallbacks(mThread);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//Toast.makeText(getActivity().getApplicationContext(), "onDestroy!!!!" , Toast.LENGTH_LONG).show();
		mHandler.removeCallbacks(mThread);
	}
}
