package com.exercise.together.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exercise.together.R;

import java.util.List;

/**
 * Created by chulchoice on 2015-07-02.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private List<Message> mMessages;

    public MessageAdapter(Context context, List<Message> messages){
        mMessages = messages;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;

        switch(viewType){
            case Message.TYPE_ACTION:
                layout = R.layout.item_action;
                break;
            case Message.TYPE_LOG:
                layout = R.layout.item_log;
                break;
            case Message.TYPE_MESSAGE_ME:
                layout = R.layout.item_message_me;
                break;
            case Message.TYPE_MESSAGE_OTHER:
                layout = R.layout.item_message;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.setUsername(message.getUsername());
        holder.setMessage(message.getMessage());
    }

    @Override
    public int getItemViewType(int position) {

        return mMessages.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mUsernameView;
        private TextView mMessageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mUsernameView = (TextView)itemView.findViewById(R.id.username);
            mMessageView = (TextView)itemView.findViewById(R.id.message);

        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }
    }

}
