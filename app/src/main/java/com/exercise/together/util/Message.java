package com.exercise.together.util;

/**
 * Created by chulchoice on 2015-07-02.
 */
public class Message {

    public static final int TYPE_LOG = 0;
    public static final int TYPE_MESSAGE_ME = 1;
    public static final int TYPE_MESSAGE_OTHER = 2;
    public static final int TYPE_ACTION = 3;

    private int mType;
    private String mUserName;
    private String mMessage;

    private Message(){}

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUserName;
    };

    public static class Builder {
        private int mType;
        private String mUserName;
        private String mMessage;

        public Builder(int type){
            this.mType = type;
        }

        public Builder setUserName(String name){
            mUserName = name;
            return this;
        }

        public Builder setMessage(String message){
            mMessage = message;
            return this;
        }

        public Message Build(){
            Message m = new Message();
            m.mType = mType;
            m.mUserName = mUserName;
            m.mMessage = mMessage;
            return m;
        }
    }
}
