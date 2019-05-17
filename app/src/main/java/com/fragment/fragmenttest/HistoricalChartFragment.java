package com.fragment.fragmenttest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;





public class HistoricalChartFragment extends Fragment {
	
	private String value = "";


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d("=====>", "HistoricalChartFragment onAttach");
		MainActivity mainActivity = (MainActivity)activity;
		value = mainActivity.getHistoricalChart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("=====>", "HistoricalChartFragment onCreateView");
		return inflater.inflate(R.layout.frg_historicalchart, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("=====>", "HistoricalChartFragment onActivityCreated");
		//TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
		//txtResult.setText(value);
	}

	
}
