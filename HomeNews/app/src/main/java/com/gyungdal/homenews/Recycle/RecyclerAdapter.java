package com.gyungdal.homenews.Recycle;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyungdal.homenews.Event.Card;
import com.gyungdal.homenews.Network.Read;
import com.gyungdal.homenews.R;
import java.util.List;

/**
 * Created by GyungDal on 2015-11-23.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    List<Recycle> items;
    int item_layout;
    private ViewHolder Holder;
    private byte Num;
    public RecyclerAdapter(Context context, List<Recycle> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.Holder = holder;
        final Recycle item=items.get(position);
        Drawable drawable=context.getResources().getDrawable(item.getImage());
        holder.image.setBackground(drawable);
        holder.title.setText(item.getTitle());
        holder.des.setText(item.getDes());
        this.Num = item.getNum();
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card move_activity = new Card();
                move_activity.intent(new Read(item.getNum()).get(),item.getNum(), context);
                //Toast.makeText(context,item.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView des;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
            title=(TextView)itemView.findViewById(R.id.title);
            des=(TextView)itemView.findViewById(R.id.description);
            cardview=(CardView)itemView.findViewById(R.id.cardview);
        }
    }
}
