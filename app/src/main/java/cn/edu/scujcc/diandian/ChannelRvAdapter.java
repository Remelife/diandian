package cn.edu.scujcc.diandian;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ChannelRvAdapter extends RecyclerView.Adapter<ChannelRvAdapter.ChannelRowHoler> {
    private  ChannelLab lab=ChannelLab.getInstance();
    private  Context context;
    /*
    *
    * 当需要一行时，此方法负责创建一行的对象
    * */
    private  ChannelClickListener listener;
    public ChannelRvAdapter(Context context,ChannelClickListener listener){
        this.context= context;
          this.listener = listener;
    }
    @NonNull
    @Override


    public ChannelRowHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //将xml
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_row,parent,false);
        ChannelRowHoler holder= new ChannelRowHoler(rowView);
        return holder;
    }
/*
* 用于确定每一行的内容是什么，即填充各个视图的内容
* */
    @Override
    public void onBindViewHolder(@NonNull ChannelRowHoler holder, int position) {
//        holder.bind("中央一台","1080P",R.drawable.CCTV1);
      Channel c=lab.getChannel(position);
        holder.bind(c);
    }
/*
*
* 用于确定列表有几行;
*
* */
public  interface  ChannelClickListener{
    public void onChannelClick(int position);
}

    @Override
    public int getItemCount() {
        return lab.getsize();
    }
/*
*
* 单行布局对应Java控制类
*
* */

    public class ChannelRowHoler extends RecyclerView.ViewHolder{
        private TextView title;//频道标题
        private TextView quality;
        private ImageView cover;
        public ChannelRowHoler(@NonNull View row) {
            super(row);
            this.title = row.findViewById(R.id.channel_title);
            this.quality = row.findViewById(R.id.channel_quality);
            this.cover = row.findViewById(R.id.channel_cover);
            row.setOnClickListener((v) -> {
                int position =getLayoutPosition();
                Log.d("diandian",position+"行被点击了");
                listener.onChannelClick(position);
            });
        }
        public  void  bind(Channel c){
            this.title.setText(c.getTitle());
            this.quality.setText(c.getQuality());
            //从网络加载图片，弃用本地图片
//            this.img.setImageResource(c.getImg());
            Log.d("diandian",c.getTitle()+"：准备从网络加载图片:"+c.getCover());
            Glide.with(context)
                    .load(c.getCover())
                    .into(this.cover);
        }

    }
}
