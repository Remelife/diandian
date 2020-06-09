package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ChannelRvAdapter.ChannelClickListener {
        private RecyclerView channelRv;
        private ChannelRvAdapter rvAdapter;
        private  ChannelLab lab =ChannelLab.getInstance();
        //线程通信1步 创建handler
        private Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case ChannelLab.MSG_CHANNELS:
                    rvAdapter.notifyDataSetChanged();
                    break;
                    case ChannelLab.MSG_FAILURE:
                        failed();
                        break;
                }
            }
        };

        private void failed(){
            Toast.makeText(MainActivity.this, "Token无效，禁止访问", Toast.LENGTH_LONG)
                    .show();
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.channelRv =findViewById(R.id.channel_rv);
        //lambda简化
        //使用handler，把适配器改为实例变量
        rvAdapter = new ChannelRvAdapter(MainActivity.this, p -> {
            //跳转到新界面，使用意图Intent
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            //通过位置p得到当前频道channel，传递用户选中的频道到下一个界面
            Channel c = lab.getChannel(p);
            intent.putExtra("channel", c);
            startActivity(intent);
        });
        this.channelRv.setAdapter(rvAdapter);
        this.channelRv.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onResume() {
        super.onResume();
        //把主线程handler传给子线程
        lab.getData(handler);
    }


    @Override
    public void onChannelClick(int position) {

    }
}
