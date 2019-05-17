package com.fragment.fragmenttest.db;

import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fragment.fragmenttest.MyApplication;

public class TestRunAsyncTask extends AsyncTask<Void, Void, String> {

    PostgresHelper client = null;

    @Override
    protected String doInBackground(Void... Void) {
        try {
            Log.e("doInBackground","幹幹幹幹幹幹幹");
            Toast.makeText(MyApplication.getContext(), "doInBackground", Toast.LENGTH_LONG).show();
            if (client.connect()) {
                System.out.println("DB connected");

            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //onProgressUpdate Run befor
    @Override
    protected void onProgressUpdate(Void... progresses) {
        Log.i("onProgressUpdate", "onProgressUpdate(Progress... progresses) called");
        client = new PostgresHelper(
                DbContract.HOST,
                DbContract.DB_NAME,
                DbContract.USERNAME,
                DbContract.PASSWORD);

    }

    //onPostExecute Run after
    @Override
    protected void onPostExecute(String result) {
        Log.i("onPostExecute", "onPostExecute(Result result) called");
        Log.i("幹我跑完了!", "result");
    }

    //onCancelled Run cancelled
    @Override
    protected void onCancelled() {
        Log.i("onCancelled", "onCancelled() called");
    }
}
