package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;


public class PlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    private Channel currentChannel;
    private PlayerView playerView;
    private TextView tvName, tvQuality;
    private ChannelLab lab = ChannelLab.getInstance();
    private static final String TAG = "diandian";
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ImageButton sendButton;

    //TODO 完成接收到数据后更新界面的代码
    private Handler handler = new Handler(){
        @Override
        public void  handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ChannelLab.MSG_HOT_COMMENTS:
                    if (msg.obj != null) {
                        List<Comment> hotComments = (List<Comment>) msg.obj;
                        updateHotComments(hotComments);
                    }
                    break;
                case ChannelLab.MSG_ADD_COMMENTS:
                    Toast.makeText(PlayerActivity.this, "感谢您的留言！",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
                case ChannelLab.MSG_FAILURE:
                    Toast.makeText(PlayerActivity.this, "评论失败， 请稍后再试",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
            }
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Serializable s = getIntent().getSerializableExtra("channel");
        Log.d(TAG, "取得的当前对象是" + s);
        if (s != null && s instanceof Channel) {
            currentChannel = (Channel) s;

            sendButton = findViewById(R.id.send);
            sendButton.setOnClickListener(v -> {
                EditText t = findViewById(R.id.comment);
                Comment c = new Comment();
                c.setAuthor("MyApp");
                c.setContent(t.getText().toString());
                //改进，随机点赞(0至100)
                Random random = new Random();
                c.setStar(random.nextInt(100));
                //TODO 调用retrofit上传评论
                lab.addComment(currentChannel.getId(), c, handler);
            });
        }
        updateUI();
    }

    private void updateUI() {
        tvName = findViewById(R.id.tv_name);
        tvQuality = findViewById(R.id.tv_quality);
        tvName.setText(currentChannel.getTitle());
        tvQuality.setText(currentChannel.getQuality());
    }

    private void updateHotComments(List<Comment> hotComments) {
        if (hotComments != null && hotComments.size() > 0) {
            //用户1
            Comment c = hotComments.get(0);
            TextView username1 = findViewById(R.id.username1);
            username1.setText(c.getAuthor());
            TextView date1 = findViewById(R.id.date1);
            date1.setText(dateFormat.format(c.getDt()));
            TextView content1 = findViewById(R.id.content1);
            content1.setText(c.getContent());
            TextView star1 = findViewById(R.id.star1);
            star1.setText(c.getStar() + "");
        }
        if (hotComments != null && hotComments.size() > 1) {
            //用户2
            Comment c = hotComments.get(1);
            TextView username2 = findViewById(R.id.username2);
            username2.setText(c.getAuthor());
            TextView date2 = findViewById(R.id.date2);
            date2.setText(dateFormat.format(c.getDt()));
            TextView content2 = findViewById(R.id.content2);
            content2.setText(c.getContent());
            TextView star2 = findViewById(R.id.star2);
            star2.setText(c.getStar() + "");
        }
        if (hotComments != null && hotComments.size() > 2) {
            //用户3
            Comment c = hotComments.get(2);
            TextView username3 = findViewById(R.id.username3);
            username3.setText(c.getAuthor());
            TextView date3 = findViewById(R.id.date3);
            date3.setText(dateFormat.format(c.getDt()));
            TextView content3 = findViewById(R.id.content3);
            content3.setText(c.getContent());
            TextView star3 = findViewById(R.id.star3);
            star3.setText(c.getStar() + "");
        }
    }


    //快捷键ctrl o
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    protected void onStart() {
        super.onStart();
        init();
        if (playerView != null) {
            playerView.onResume();
        }

    }

    protected void onStop() {
        super.onStop();
        if (playerView != null) {
            playerView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player == null) {
            init();
            if (playerView != null) {
                playerView.onResume();
            }
        }
        //1、获取最新的热门评论
        lab.getHoytComments(currentChannel.getId(), handler);
    }

    /*
     * 自定义方法，初始化播放器
     * */
    private void init() {
        player = ExoPlayerFactory.newSimpleInstance(this);
        player.setPlayWhenReady(true);
        //从界面找视图
        PlayerView playerView = findViewById(R.id.tv_player);
        //关联视图与播放器
        playerView.setPlayer(player);
        //准备播放的媒体
        Uri videoUrl = Uri.parse("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        if (null != currentChannel) {
            videoUrl = Uri.parse(currentChannel.getUrl());
        }
        DataSource.Factory factory = new DefaultDataSourceFactory(this, "diandian");
        MediaSource videoSource = new HlsMediaSource.Factory(factory).createMediaSource(videoUrl);
        player.prepare(videoSource);
    }

    private void clean() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
