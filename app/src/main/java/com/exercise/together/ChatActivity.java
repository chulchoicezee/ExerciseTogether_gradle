package com.exercise.together;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exercise.together.util.Constants;
import com.exercise.together.util.Message;
import com.exercise.together.util.MessageAdapter;
import com.exercise.together.util.ProfileInfo;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {
    //
    private RecyclerView mMessagesView;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private Socket mSocket;
    private EditText mInputMessageView;
    private String mUsername="me";
    {
        try {
            mSocket = IO.socket(Constants.URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        ProfileInfo pi = i.getParcelableExtra(Constants.KEY.PROFILE_INFO);
        Toast.makeText(this, pi.name, Toast.LENGTH_SHORT).show();

        mMessagesView = (RecyclerView)findViewById(R.id.rlist);
        mMessagesView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(this, mMessages);
        mMessagesView.setAdapter(mAdapter);
        mInputMessageView = (EditText)findViewById(R.id.message_input);
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button btn = (Button)findViewById(R.id.send_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        //mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.connect();

        addLog(getResources().getString(R.string.message_welcome));
        addParticipantsLog(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        /**mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);*/
    }

    private void send(){
        String message = mInputMessageView.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            mInputMessageView.requestFocus();
        }
        mInputMessageView.setText("");
        addMyMessage(mUsername, message);
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("user", mUsername);
            obj.putOpt("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("new message", obj);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatActivity.this, "failed to connect", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    String userName = null;
                    String message = null;
                    try {
                        userName = data.getString("user");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ChatActivity.this, userName+": "+message, Toast.LENGTH_SHORT).show();
                    addMessage(userName, message);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addLog((String)args[0]);
                    //addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    //removeTyping(username);
                }
            });
        }
    };

    private void addMessage(String userName, String message){
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE_OTHER).setUserName(userName).setMessage(message).Build());
        mAdapter.notifyItemInserted(mMessages.size()-1);
        mMessagesView.scrollToPosition(mAdapter.getItemCount()-1);
    }

    private void addMyMessage(String userName, String message){
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE_ME).setUserName(userName).setMessage(message).Build());
        mAdapter.notifyItemInserted(mMessages.size()-1);
        mMessagesView.scrollToPosition(mAdapter.getItemCount()-1);
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG).setMessage(message).Build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

}
