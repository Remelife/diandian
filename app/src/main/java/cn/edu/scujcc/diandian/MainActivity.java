package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends AppCompatActivity  implements ChannelRvAdapter.ChannelClickListener {
        private RecyclerView channelRv;
        private ChannelRvAdapter rvAdapter;
        private  ChannelLab lab =ChannelLab.getInstance();
        //线程通信1步 创建handler
        private Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == ChannelLab.MSG_CHANNELS){
                    rvAdapter.notifyDataSetChanged();
                }
            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.channelRv =findViewById(R.id.channel_rv);
        //
        rvAdapter =new ChannelRvAdapter(MainActivity.this,this);
        this.channelRv.setAdapter(rvAdapter);
        this.channelRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onChannelClick(int p) {
        Intent intent =new Intent(MainActivity.this,PlayerActivity.class);
        Channel c = lab.getChannel(p);
        intent.putExtra("channel",c);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //把主线程handler传给子线程
        lab.getData(handler);
    }
}
