package com.fragment.fragmenttest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public Context onCreateDialog;
    private String[] record_id_arry;
    private String[] sensor_id_arry;
    private String[] sensor_category_arry;
    private String[] sensor_value_arry;
    private String[] read_time_arry;
    private int ID;
    private Date now;
    private SimpleDateFormat sdf;
    private int tine;
    //private static Date beforDate;
    private ProgressDialog dialog;

    //找到UI工人的經紀人，這樣才能派遣工作  (找到顯示畫面的UI Thread上的Handler)
    private Handler mUI_Handler = new Handler();
    //宣告特約工人的經紀人
    private Handler mThreadHandler;
    //宣告特約工人
    private HandlerThread mThread;
    Date beforDate;


    String climate;
    String pm25;
    String nh3;
    String h2s;
    String humidity;
    String co2;
    String ch4;

    TextView textHumidity_Data;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //1
        tabHost.addTab(tabHost.newTabSpec("RealTimeData")
                        .setIndicator("RealTimeData"),
                RealTimeFragment.class,
                null);

        //2
        tabHost.addTab(tabHost.newTabSpec("HistoricalChart")
                        .setIndicator("HistoricalChart"),
                HistoricalChartFragment.class,
                null);
//        //3
        tabHost.addTab(tabHost.newTabSpec("ReservedFragment")
                        .setIndicator("ReservedFragment"),
                ReservedFragment.class,
                null);
//        //4
//        tabHost.addTab(tabHost.newTabSpec("Twitter")
//                        .setIndicator("Twitter"),
//                TwitterFragment.class,
//                null);


        Calendar c = Calendar.getInstance();
        c.set(2012, 11, 12);        //設定日期為2012年12月12日，
        //在Calendar類別中月份的編號是由0~11

        c.set(Calendar.YEAR, 2013);                        //將年改成2013年
        c.set(Calendar.MONTH, Calendar.JANUARY);   //將月份改成1月
        c.set(Calendar.DAY_OF_MONTH, 31);            // 將日改成31日

        c.set(Calendar.HOUR_OF_DAY, 18);               //將hour改成下午六點

        c.set(Calendar.AM_PM, Calendar.PM);             //將hour改成下午六點
        c.set(Calendar.HOUR, 6);

        //Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);                      //取出年
        int month = c.get(Calendar.MONTH) + 1;           //取出月，月份的編號是由0~11 故+1
        int day = c.get(Calendar.DAY_OF_MONTH);        //取出日

        int weekday = c.get(Calendar.DAY_OF_WEEK);   //取出星期幾，編號由Sunday(1)~Saturday(7)


        //TestClient.execute("2019-04-29 10:00:00");300000   600000   10000
        tine=60000;


        now = new Date();
        beforDate = new Date(now.getTime() - tine);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(beforDate));
        Log.e("現在時間減10分鐘:", sdf.format(beforDate));
        //Toast.makeText(MainActivity.this, "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
//        RealTimeMethod();
//region 非同步程式的開始點
//        if (checkNetWork()) {
//            //Toast.makeText(getApplicationContext(), "MainActivity", Toast.LENGTH_LONG).show();
//            PostgresqlTask TestClient = new PostgresqlTask();
//            //TestClient.execute("2019-04-29 10:00:00");300000   600000   10000
//            tine=10000;
//
//            beforDate = new Date(now.getTime() - tine);
//            System.out.println(sdf.format(beforDate));
//            Log.e("現在時間減10分鐘:", sdf.format(beforDate));
//            Toast.makeText(MainActivity.this, "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
//            TestClient.execute(sdf.format(beforDate));
//
////----------------------------------------------------------------------------------------
//            dialog = ProgressDialog.show(MainActivity.this, "",
//                    "Loading. Please wait...", false, false);
//
//            //ProgressDialog pd3 = ProgressDialog.show(this, "提示", "正在登陆中", false, true);
//        }
        //endregion



        //聘請一個特約工人，有其經紀人派遣其工人做事 (另起一個有Handler的Thread)
        mThread = new HandlerThread("name");
        //讓Worker待命，等待其工作 (開啟Thread)
        mThread.start();
        //找到特約工人的經紀人，這樣才能派遣工作 (找到Thread上的Handler)
        mThreadHandler=new Handler(mThread.getLooper());
        //請經紀人指派工作名稱 r，給工人做
        //mThreadHandler.post(r1);

        textHumidity_Data=(TextView) findViewById(R.id.textTemp_Date);
    }
//region Thread執行資料庫
//    //工作名稱 r1 的工作內容
//    private  Runnable r1=new Runnable () {
//
//        public void run() {
//
//            // TODO Auto-generated method stub
//            //做了很多事
//            tine=10000;
//            //老闆指定每隔幾秒要做一次工作1 (單位毫秒:1000等於1秒)
//            mThreadHandler.postDelayed(this, tine);
//
//            beforDate = new Date(now.getTime() - tine);
//            System.out.println(sdf.format(beforDate));
//            Log.e("現在時間減10分鐘:", sdf.format(beforDate));
//            Toast.makeText(MainActivity.this, "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
//
//
//                Connection con = null;
//                PreparedStatement ps = null;
//                ResultSet rs = null;
//                String count = "0";
//                Statement st = null;
//                StringBuilder stringBuilder = new StringBuilder();
//
//                try {
//                    //STEP 3: Open a connection
//                    System.out.println("Connecting to database...");
//                    Class.forName("org.postgresql.Driver");
//                    Log.e("我要連線!!!", "我要連線!!!");
//                    //DriverManager.setLoginTimeout(5);
//                    //jdbc:postgresql://140.116.247.120:36432/happig幹happig_read幹HappigRead幹
//                    con = DriverManager.getConnection(
//                            "jdbc:postgresql://140.116.247.120:36432/happig",
//                            "happig_read",
//                            "HappigRead");
//
//                    if (con != null && !con.isClosed()) {
//                        System.out.println("資料庫連線測試成功！");
//                        //con.close();
//                    }
////-----------------------------------------------------------------------------------------------------------------------------------------------
//
//
////            //STEP 4: Execute a query
//                    //2019-04-26 00:00:00
//                    System.out.println("Creating statement...");
//                    //st = con.createStatement();
//
//                    String sql;
//                    sql = "select * from sensor_records where read_time>='" + beforDate + "' order by read_time desc";
//
//                    //ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
////            //STEP 5: Extract data from result set
//                    Log.e("executeQuery!", sql);
//                    //rs = ps.executeQuery(sql);
//
//
//                    //改成可以雙向滾動，但不及時更新，就是如果數據庫裡的數據修改過，
//                    //並不修改ResultSet記錄 必須要改成 TYPE_SCROLL_INSENSITIVE
//                    ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//                            ResultSet.CONCUR_READ_ONLY);
//                    //执行查询
//                    rs = ps.executeQuery();
//
//
//                    int size = 0;
//                    if (rs != null) {
//                        rs.last();    // moves cursor to the last row
//                        size = rs.getRow(); // get row id
//                    }
//
//                    ID = 0;
//                    record_id_arry = new String[size];
//                    sensor_id_arry = new String[size];
//                    sensor_category_arry = new String[size];
//                    sensor_value_arry = new String[size];
//                    read_time_arry = new String[size];
//                    rs.first();
//
//                    while (rs.next()) {
//                        //Retrieve by column name
//                        String record_id = rs.getString("record_id");
//                        record_id_arry[ID] = /*"RID:" +*/ record_id;
//
//                        String sensor_id = rs.getString("sensor_id");
//                        sensor_id_arry[ID] = /*"SID:" +*/ sensor_id;
//
//                        String sensor_category = rs.getString("sensor_category");
//                        sensor_category_arry[ID] = /*"SCAT:" +*/ sensor_category;
//
//                        String sensor_value = rs.getString("sensor_value");
//                        sensor_value_arry[ID] = /*"SV:" +*/ sensor_value;
//
//                        String read_time = rs.getString("read_time");
//                        read_time_arry[ID] = /*"RT:" + */read_time;
////                //Display values
//                        Log.e("record_id!", record_id);
//                        Log.e("sensor_id!", sensor_id);
//                        Log.e("sensor_category!", sensor_category);
//                        Log.e("sensor_value!", sensor_value);
//                        Log.e("read_time!", read_time);
//                        String TTV = "";
//                        //TTV = TTV + "record_id:" + record_id + "\n" + "sensor_id:" + sensor_id + "\n" + "sensor_category:" + sensor_category + "\n" + "sensor_value:" + sensor_value + "\n" + "read_time:" + read_time;
////                        stringBuilder.append(TTV).append("\n");
//                        ID += 1;
//                    }
//
//                    HashMap<String, String> sensor_data_hash_map = new HashMap<String, String>();
//                    for (int i = 0; i < 9; i++) {
//
//                        sensor_data_hash_map.put(sensor_category_arry[i], sensor_value_arry[i]);
//                    }
//                     climate = sensor_data_hash_map.get("climate");
//                     pm25 = sensor_data_hash_map.get("pm2.5");
//                     nh3 = sensor_data_hash_map.get("nh3");
//                     h2s = sensor_data_hash_map.get("h2s");
//                     humidity = sensor_data_hash_map.get("humidity");
//                     co2 = sensor_data_hash_map.get("co2");
//                     ch4 = sensor_data_hash_map.get("ch4");
////------------------------------------------------------------------------------------------------------------------------------------------------
//
//                    //System.out.println(ps);
//                    // Log.e("ps!", ps.toString());
//                    //return   ;
//                } catch (SQLException e) {
//                    Log.e("SQLException有例外!", e.getMessage());
//                    //Toast.makeText(MyApplication.getContext(), "No SQLException有例外!"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Log.e("Exception有例外!", e.getMessage());
//                }
//
//        }
//
//    };
//endregion


    public String getRealTimeData() {
        return "RealTimeData";
    }

    public String getHistoricalChart() {
        return "HistoricalChart";
    }

    public String Reserved() {
        return "Reserved";
    }


    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    //處理少量資訊或UI
                    break;
            }
        }
    };
    public void Reflase(String V){

        textHumidity_Data.setText(V);
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //region AsyncTask連查資料庫
//    private class PostgresqlTask extends AsyncTask<String, String, String> {
//        @Override
//        public String doInBackground(String... params) {
//            Connection con = null;
//            PreparedStatement ps = null;
//            ResultSet rs = null;
//            String count = "0";
//            Statement st = null;
//            StringBuilder stringBuilder = new StringBuilder();
//
//            try {
//                //STEP 3: Open a connection
//                System.out.println("Connecting to database...");
//                Class.forName("org.postgresql.Driver");
//                Log.e("我要連線!!!", "我要連線!!!");
//                //DriverManager.setLoginTimeout(5);
//                //jdbc:postgresql://140.116.247.120:36432/happig幹happig_read幹HappigRead幹
//                con = DriverManager.getConnection(
//                        "jdbc:postgresql://140.116.247.120:36432/happig",
//                        "happig_read",
//                        "HappigRead");
//
//                if (con != null && !con.isClosed()) {
//                    System.out.println("資料庫連線測試成功！");
//                    //con.close();
//                }
////-----------------------------------------------------------------------------------------------------------------------------------------------
//
//
////            //STEP 4: Execute a query
//                //2019-04-26 00:00:00
//                System.out.println("Creating statement...");
//                //st = con.createStatement();
//
//                String sql;
//                sql = "select * from sensor_records where read_time>='" + params[0] + "' order by read_time desc";
//
//                //ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
////            //STEP 5: Extract data from result set
//                Log.e("executeQuery!", sql);
//                //rs = ps.executeQuery(sql);
//
//
//                //改成可以雙向滾動，但不及時更新，就是如果數據庫裡的數據修改過，
//                //並不修改ResultSet記錄 必須要改成 TYPE_SCROLL_INSENSITIVE
//                ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//                        ResultSet.CONCUR_READ_ONLY);
//                //执行查询
//                rs = ps.executeQuery();
//
//
//                int size = 0;
//                if (rs != null) {
//                    rs.last();    // moves cursor to the last row
//                    size = rs.getRow(); // get row id
//                }
//
//                ID = 0;
//                record_id_arry = new String[size];
//                sensor_id_arry = new String[size];
//                sensor_category_arry = new String[size];
//                sensor_value_arry = new String[size];
//                read_time_arry = new String[size];
//                rs.first();
//
//                while (rs.next()) {
//                    //Retrieve by column name
//                    String record_id = rs.getString("record_id");
//                    record_id_arry[ID] = "RID:" + record_id;
//
//                    String sensor_id = rs.getString("sensor_id");
//                    sensor_id_arry[ID] = "SID:" + sensor_id;
//
//                    String sensor_category = rs.getString("sensor_category");
//                    sensor_category_arry[ID] = "SCAT:" + sensor_category;
//
//                    String sensor_value = rs.getString("sensor_value");
//                    sensor_value_arry[ID] = "SV:" + sensor_value;
//
//                    String read_time = rs.getString("read_time");
//                    read_time_arry[ID] = "RT:" + read_time;
////                //Display values
//                    Log.e("record_id!", record_id);
//                    Log.e("sensor_id!", sensor_id);
//                    Log.e("sensor_category!", sensor_category);
//                    Log.e("sensor_value!", sensor_value);
//                    Log.e("read_time!", read_time);
//                    String TTV = "";
//                    TTV = TTV + "record_id:" + record_id + "\n" + "sensor_id:" + sensor_id + "\n" + "sensor_category:" + sensor_category + "\n" + "sensor_value:" + sensor_value + "\n" + "read_time:" + read_time;
//                    stringBuilder.append(TTV).append("\n");
//                    ID += 1;
//                }
////------------------------------------------------------------------------------------------------------------------------------------------------
//
//                //System.out.println(ps);
//                // Log.e("ps!", ps.toString());
//                //return   ;
//            } catch (SQLException e) {
//                Log.e("SQLException有例外!", e.getMessage());
//                //Toast.makeText(MyApplication.getContext(), "No SQLException有例外!"+e.getMessage(), Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                Log.e("Exception有例外!", e.getMessage());
//            }
//            return stringBuilder.toString();
//        }
//
//        //After
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            System.out.println("result: " + result);
//            try {
//                //顯示
//
//
//////                final ListView listView1 = (ListView) findViewById(R.id.lsv_main);
////                List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
////                for (int i = 0; i < record_id_arry.length; i++) {
////                    Map<String, Object> listItem = new HashMap<String, Object>();
////                    listItem.put("record_id", record_id_arry[i]);
////                    listItem.put("sensor_id", sensor_id_arry[i]);
////                    listItems.add(listItem);
////                }
//
//
////                SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), listItems, R.layout.list_view_item, new String[]{"record_id", "sensor_id"}, new int[]{R.id.txtViewList_record_id, R.id.txtViewList_record_id});
////                listView1.setAdapter(simpleAdapter);
//
//
////-------------------------------------------------------------------------------------------------------------------------
////
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
//
//                    itemList.add(item);
//                }
//
//                dialog.dismiss();
//
////                // ListView 中所需之資料參數可透過修改 Adapter 的建構子傳入
////                mListAdapter = new ListAdapter(getApplicationContext(), itemList);
////
////                //設定 ListView 的 Adapter
////                lsv_main.setAdapter(mListAdapter);
////
//////---------------------------------------------------------------------------------------------------------------------------------------------
//                //dialog.dismiss();
//
////                if (!result.equals("1")) {
////
////                    //Toast.makeText(getApplicationContext(), "Hello admin!", Toast.LENGTH_SHORT).show();
////                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//////                    label.setVisibility(View.VISIBLE);
//////                    label.setText(result);
////
////                } else {
////                    //Toast.makeText(MyApplication.getContext(), "Seems like you 're not admin!", Toast.LENGTH_SHORT).show();
////                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                //Toast.makeText(MyApplication.getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
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
//
//        }
//
//        @Override
//        protected void onCancelled(String s) {
//            super.onCancelled(s);
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//
//    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//endregion

    //region 檢查網路功能
    private boolean checkNetWork() {
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null) {
//            //網路是否已連線
//            mNetworkInfo.isConnected();
//            txtViewNetWork.append("" +mNetworkInfo.isConnected()+"\n");
//            //網路連線方式名稱
            mNetworkInfo.getTypeName();
            //txtViewNetWork.setText("" + mNetworkInfo.getTypeName()+"\n");
//            //網路連線狀態
            mNetworkInfo.getState();
//            txtViewNetWork.append("" + mNetworkInfo.getState()+"\n");
//            //網路是否可使用
//            mNetworkInfo.isAvailable();
//            txtViewNetWork.append("" + mNetworkInfo.isAvailable()+"\n");
//            //網路是否已連接or連線中
//            mNetworkInfo.isConnectedOrConnecting();
//            txtViewNetWork.append("" + mNetworkInfo.isConnectedOrConnecting()+"\n");
//            //網路是否故障有問題
//            mNetworkInfo.isFailover();
//            txtViewNetWork.append("" + mNetworkInfo.isFailover()+"\n");
//            //網路是否在漫遊模式
//            mNetworkInfo.isRoaming();
//            txtViewNetWork.append("" + mNetworkInfo.isRoaming()+"\n");
//            //網路詳細狀態
            mNetworkInfo.getDetailedState();
//            txtViewNetWork.append("" + mNetworkInfo.getDetailedState()+"\n");
//            //網路狀態詳細資訊
//            mNetworkInfo.getExtraInfo();
//            txtViewNetWork.append("" + mNetworkInfo.getExtraInfo()+"\n");
//            //網路出錯時的原因回報:
//            mNetworkInfo.getReason();
//            txtViewNetWork.append("" + mNetworkInfo.getReason()+"\n");
            //mWebView.loadUrl("http://nikeru8.blogspot.tw/");
        } else {
            new AlertDialog.Builder(this).setMessage("阿是不會開網路逆!?")
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent callNetSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                            Toast.makeText(getApplicationContext(), "請前往開啟網路", Toast.LENGTH_LONG).show();
                            //startActivity(callNetSettingIntent);
                        }
                    })
                    .show();
            return false;
        }
        return true;
    }
    //endregion
//***********************************************************************************************************************************************************************************



//    private void RealTimeMethod() {
//        try {
//            // 进行网络请求
////                runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                    // 更新UI
//            //Toast.makeText(getActivity().getApplicationContext(),"any mesage",Toast.LENGTH_SHORT).show();
//
////------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//            Connection con = null;
//            PreparedStatement ps = null;
//            ResultSet rs = null;
//            String count = "0";
//            Statement st = null;
//            StringBuilder stringBuilder = new StringBuilder();
//
//            try {
//
//                tine = 60000;
//                now = new Date();
//                beforDate = new Date(now.getTime() - tine);
//                System.out.println(sdf.format(beforDate));
//                Log.e("現在時間減10分鐘:", sdf.format(beforDate));
//                //Toast.makeText(getContext().getApplicationContext(), "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_LONG).show();
//                //STEP 3: Open a connection
//                System.out.println("Connecting to database...");
//                Class.forName("org.postgresql.Driver");
//                Log.e("我要連線!!!", "我要連線!!!");
//                //DriverManager.setLoginTimeout(5);
//                //jdbc:postgresql://140.116.247.120:36432/happig幹happig_read幹HappigRead幹
//                con = DriverManager.getConnection(
//                        "jdbc:postgresql://140.116.247.120:36432/happig",
//                        "happig_read",
//                        "HappigRead");
//
//                if (con != null && !con.isClosed()) {
//                    System.out.println("資料庫連線測試成功！");
//                    //con.close();
//                }
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }finally {
//            System.out.println("資料庫連線測試結束！");
//        }
//    }



    @Override
    protected void onStart() {
//        dialog.dismiss();
        super.onStart();
        Log.e("onStop", "onStop!!!!!!!!!!!!!!!!!!!!");
//        if (checkNetWork()) {
            //Toast.makeText(getApplicationContext(), "MainActivity", Toast.LENGTH_LONG).show();

//            //TestClient.execute("2019-04-29 10:00:00");300000   600000   10000
//            //tine=300000;
//            now = new Date();
//            beforDate = new Date(now.getTime() - tine);
//            System.out.println(sdf.format(beforDate));
//            Log.e("現在時間減10分鐘:", sdf.format(beforDate));
//            Toast.makeText(MainActivity.this, "現在時間減"+tine+"秒鐘:" + sdf.format(beforDate), Toast.LENGTH_SHORT).show();
//            TestClient.execute(sdf.format(beforDate));

//----------------------------------------------------------------------------------------
//            dialog = ProgressDialog.show(MainActivity.this, "",
//                    "Loading. Please wait...", false, false);
//
//            //ProgressDialog pd3 = ProgressDialog.show(this, "提示", "正在登陆中", false, true);
//        }
    }


}
