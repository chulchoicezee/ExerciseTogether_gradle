package com.exercise.together.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exercise.together.R;
import com.exercise.together.util.Callback;
import com.exercise.together.util.Constants.MENU;

public class SportsMainFragment extends Fragment {
	
	Context mContext;
	
	Callback mCallback;
	
	public final static String TAG = "BadmintonFragment";
	
	ProgressDialog mProDiag = null;
	
	public SportsMainFragment(){}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
		mCallback = (Callback)activity;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_sports_main, container, false);
         
        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Log.v(TAG, "onActivityCreated");
		
		Button btn = (Button)getActivity().findViewById(R.id.info_btn2);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCallback.onBtnClick(SportsMainFragment.this);	
			}
		});
		
		Bundle bd = getArguments();
		int number = bd.getInt("number");
		
		TextView tvTitle = (TextView)getActivity().findViewById(R.id.info_title_tv);
		TextView tv1 = (TextView)getActivity().findViewById(R.id.info_body_tv1);
		TextView tv2 = (TextView)getActivity().findViewById(R.id.info_body_tv2);
		TextView tv3 = (TextView)getActivity().findViewById(R.id.info_body_tv2_1);
		Button btn2 = (Button)getActivity().findViewById(R.id.info_btn2);
		btn2.setText("ģ������ ����");
		
		String tv1Str = null;
		String tv2Str = null;
		String tv3Str = null;
		String title = null;
		int region1 = 0;
		int region2 = 0;
		int region3 = 0;
		int region4 = 0;
		
		switch(number){
		case MENU.BADMINTON :
			tv1Str = "�� �ο�: 64��";
			tv2Str = "��Һ� �ο� : 64��";
			tv3Str = "����  10��\n ���� 27��\n ���� 32�� \n ���� 5��";
			region1 = 10;
			region2 = 27;
			region3 = 32;
			region4 = 5;
			title = "������";
		break;
		case MENU.TENNIS :
			tv1Str = "�� �ο�: 40��";
			tv2Str = "��Һ� �ο� : 40��";
			tv3Str = "����  30��\n ���� 3��\n ���� 5�� \n ���� 2��";
			region1 = 30;
			region2 = 3;
			region3 = 5;
			region4 = 2;
			title = "�״Ͻ�";
		break;
		case MENU.INLINE :
			tv1Str = "�� �ο�: 5��";
			tv2Str = "��Һ� �ο� : 5��";
			tv3Str = "����  3��\n ���� 2��\n ���� 0�� \n ���� 0��";
			region1 = 3;
			region2 = 2;
			region3 = 0;
			region4 = 0;
			title = "�ζ���";
		break;
		case MENU.TABLE_TENNIS :
			tv1Str = "�� �ο�: 62��";
			tv2Str = "��Һ� �ο� : 62��";
			tv3Str = "����  38��\n ���� 10��\n ���� 2�� \n ���� 12��";
			region1 = 38;
			region2 = 10;
			region3 = 2;
			region4 = 12;
			title = "Ź��";
		break;
		default:
			break;
		}
		tv1.setText(tv1Str);
		tv2.setText(tv2Str);
		tv3.setText(tv3Str);
		tvTitle.setText(title);
		
		//�׷���
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] {number, region1, region2, region3, region4});
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		
		renderer.setChartTitle("�ο���Ȳ");
		renderer.setChartTitleTextSize(30);
		
		String[] titles = new String[]{"����"};
		int[] colors = new int[]{Color.RED};
		
		//renderer.setLegendTextSize(22);
		int length = colors.length;
		for(int i=0; i<length; i++){
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		
		//axis Ÿ��Ʋ == label
		renderer.setXTitle("����");
		renderer.setYTitle("�ο���");
		renderer.setAxisTitleTextSize(22);
		renderer.setLabelsColor(Color.BLACK);
		renderer.setLabelsTextSize(20);
		//axis ����
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(5.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(30);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.addXTextLabel(1, "����");
		renderer.addXTextLabel(2, "����");
		renderer.addXTextLabel(3, "����");
		renderer.addXTextLabel(4, "����");
		renderer.addXTextLabel(5, "����");
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(0).setChartValuesTextAlign(Align.RIGHT);
		renderer.getSeriesRendererAt(0).setChartValuesTextSize(30);
		//renderer.getSeriesRendererAt(0).setGradientEnabled(true);	
		//renderer.getSeriesRendererAt(0).setGradientStart(0, Color.parseColor("#e71d73"));	
		//renderer.getSeriesRendererAt(0).setGradientStop(10, Color.parseColor("#ffffff"));
		
		renderer.setAxesColor(Color.DKGRAY);
		renderer.setXLabels(5);
		renderer.setYLabels(5);
		
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		
		renderer.setMarginsColor(Color.parseColor("#FFFFFF"));	
		renderer.setShowLegend(false);	
		
		renderer.setZoomRate(1.0f);
		renderer.setBarSpacing(0.5f);
		renderer.setMargins(new int[] {50, 50, 50, 50});
		renderer.setXLabels(0);
		renderer.setXLabelsColor(Color.BLUE);
		renderer.setYLabelsColor(0, Color.DKGRAY);
		renderer.setXLabelsPadding(10);
		
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for(int i=0; i<titles.length; i++){
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for(int k=0; k<seriesLength; k++){
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		
		GraphicalView gv = ChartFactory.getBarChartView(mContext, dataset, renderer, Type.STACKED);
		//gv.setBackgroundColor(Color.BLACK);
		LinearLayout llChart = (LinearLayout)getActivity().findViewById(R.id.info_chart);
		llChart.addView(gv);
	}


	
	
}
