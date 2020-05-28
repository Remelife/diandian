package cn.edu.scujcc.diandian;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class ChannelLab {
    private static final String TAG = "DianDian";//LOG
    public final static int MSG_CHANNELS = 1;
    public final static int MSG_HOT_COMMENTS = 2;
    public final static int MSG_ADD_COMMENTS = 3;
    public final static int MSG_FAILURE = 4;

    //单例第一步
    private static ChannelLab INSTANCE = null;

    private List<Channel> data;

    //单例第二步
    private ChannelLab() {
        //data来源于网络
        data = new ArrayList<>();
//        getData();
    }

    //单例第三步
    public static ChannelLab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChannelLab();
        }
        return INSTANCE;
    }

    /*
     * 生成测试数据
     * */
    public void test() {
        data = new ArrayList<>();
        Channel c = new Channel();

    }

    /*
     * 返回数据总数量
     *
     * */
    public int getsize() {
        return data.size();
    }

    /*
     * 返回指定位置的频道信息
     * */
    public Channel getChannel(int position) {
        return this.data.get(position);
    }

    public void getData(Handler handler) {
        //调用单例
        Retrofit retrofit = RetrofitClient.getInstance();

        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<Result<List<Channel>>> call = api.getAllChannels();
        //enqueue自动生成子线程，去执行代码
        call.enqueue(new Callback<Result<List<Channel>>>() {
            @Override
            public void onResponse(Call<Result<List<Channel>>> call,
                                   Response<Result<List<Channel>>> response) {
                if (response.code() == 403) {
                    Log.w(TAG, "访问被禁止");
                    Message msg = new Message();
                    msg.what = MSG_FAILURE;
                    handler.sendMessage(msg);
                } else if (null != response && null != response.body()) {
                    Log.d(TAG, "从阿里云得到的数据是:");
                    Log.d(TAG, response.body().toString());
                    Result<List<Channel>> result = response.body();
                    data = result.getData();
                    //发出通知
                    Message msg = new Message();
                    msg.what = MSG_CHANNELS;//自己定义1代表从阿里云获取数据完毕
                    handler.sendMessage(msg);
                } else {
                    Log.w(TAG, "response没有数据!");
                }
            }

            @Override
            public void onFailure(Call<Result<List<Channel>>> call, Throwable t) {
                Log.e(TAG, "访问网络失败", t);
            }
        });
    }

    /**
     * 从服务器获取热门评论
     *
     * @return
     */
    public void getHotComments(String channelId, Handler handler) {
        //调用单例
        Retrofit retrofit = RetrofitClient.getInstance();
        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<Result<List<Comment>>> call = api.getHotComments(channelId);
        call.enqueue(new Callback<Result<List<Comment>>>() {
            @Override
            public void onResponse(Call<Result<List<Comment>>> call, Response<Result<List<Comment>>> response) {
                if (response.code() == 403){
                    Log.w(TAG, "访问被禁止");
                    Message msg = new Message();
                    msg.what = MSG_FAILURE;
                    handler.sendMessage(msg);
                }else if (null != response && null != response.body()) {
                        Log.d(TAG, "从阿里云得到的数据是:");
                        Log.d(TAG, response.body().toString());
                        Result<List<Comment>> result = response.body();
                        List<Comment> comments = result.getData();
                        //发出通知
                        Message msg = new Message();
                        msg.what = MSG_HOT_COMMENTS;//自己定义2代表热门评论
                        msg.obj = comments;
                        handler.sendMessage(msg);
                    } else {
                        Log.w(TAG, "response没有数据!");
                    }
            }

            @Override
            public void onFailure(Call<Result<List<Comment>>> call, Throwable t) {
                Log.e(TAG, "访问网络失败！", t);
            }
        });
    }


    /**
     * 添加评论
     * @param channelId
     * @param comment
     * @param handler
     */
    public void addComment(String channelId, Comment comment, Handler handler) {
        Retrofit retrofit = RetrofitClient.getInstance();
        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<Channel> call = api.addComment(channelId, comment);
        call.enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                Log.d(TAG, "新增评论后服务器返回的数据是：");
                Log.d(TAG, response.body().toString());
                Message msg = new Message();
                msg.what = MSG_ADD_COMMENTS;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.e(TAG, "访问网络失败！", t);
                Message msg = new Message();
                msg.what = MSG_FAILURE;
                handler.sendMessage(msg);
            }
        });
    }

}
