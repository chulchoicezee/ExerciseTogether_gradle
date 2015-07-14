package com.exercise.together;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.together.adapter.AListAdapter;
import com.exercise.together.util.Constants;
import com.exercise.together.util.ProfileInfo;

import java.util.ArrayList;

public class FriendListActivity extends Activity {

	private static final String TAG = "FriendListActivity";
	private AListAdapter mListAdapter;
	private ArrayList<ProfileInfo> mFriends;
	private int curPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_friend_list);
		
		setActionBar(R.string.app_name);
		
		Log.v(TAG, "onCreate");
		
		Intent i = getIntent();
		//Bundle bd = i.getBundleExtra(Constants.KEY.PROFILE_INFO_ARRAY);
		mFriends = i.getParcelableArrayListExtra(Constants.KEY.PROFILE_INFO_ARRAY);
		
		ListView lv = (ListView)findViewById(R.id.friendlist_lv);
		mListAdapter = new AListAdapter<ProfileInfo>(this, R.layout.list_layout, mFriends, null);
		TextView emptyView = (TextView)this.findViewById(R.id.friendList_lv_empty);
		emptyView.setText("no friends");
		lv.setEmptyView(emptyView);
		
		lv.setAdapter(mListAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				curPos = position;
				AlertDialog.Builder ab = new AlertDialog.Builder(FriendListActivity.this);
				ab.setMessage("당신의 닉네임으로 대화를 시작합니다.");
				ab.setCancelable(true);
				ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ProfileInfo pi = (ProfileInfo)mFriends.get(curPos);
						Toast.makeText(FriendListActivity.this, pi.name+" 과 대화합니다.", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(FriendListActivity.this, ChatActivity.class);
						i.putExtra(Constants.KEY.PROFILE_INFO, pi);
						startActivity(i);
					}
				});
				ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(FriendListActivity.this, "no", Toast.LENGTH_SHORT).show();
					}
				});
				ab.create().show();;
			}
		});
		//FragmentManager fm = getFragmentManager();
		//FragmentTransaction ft = fm.beginTransaction();
		//FriendListFragment mFragment = new FriendListFragment(friends);
		//ft.replace(R.id.frag1, mFragment, null);
		//ft.commit();
	}

    void setActionBar(int resTitle){
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE,
                    ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setTitle(getText(resTitle));
        }

    }
}
