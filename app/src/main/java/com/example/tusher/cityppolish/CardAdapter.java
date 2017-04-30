package com.example.tusher.cityppolish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Acer on 4/19/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private ImageLoader imageLoader;
    private Context context;

    List<citypol> citypols;

    public CardAdapter(List<citypol> citypols, Context context){
        super();
        this.citypols=citypols;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.city_polish,parent, false);
        ViewHolder viewHolder=new ViewHolder(v, context, (ArrayList<citypol>) citypols); // added two more params in this
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        citypol cpol= citypols.get(position);
        imageLoader=CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(cpol.getImageUrl(),ImageLoader.getImageListener(holder.imageView, R.drawable.image, android.R.drawable.ic_dialog_alert));
        holder.imageView.setImageUrl(cpol.getImageUrl(), imageLoader);
        holder.textViewName.setText(cpol.getName());
        holder.textViewPublisher.setText(cpol.getTime());
        holder.textViewLocation.setText(cpol.getLocation());


    }
    public int getItemCount(){
        return citypols.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Views
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewPublisher;
        public TextView textViewLocation;
        ArrayList<citypol> citypols=new ArrayList<citypol>();
        Context ctx;
        //Initializing Views
        public ViewHolder(View itemView, Context ctx,ArrayList<citypol> citypols) {
            super(itemView);
            this.citypols=citypols;
            this.ctx=ctx;
            itemView.setOnClickListener(this);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewPublisher = (TextView) itemView.findViewById(R.id.textViewPublisher);
            textViewLocation=(TextView) itemView.findViewById(R.id.textViewLocation);
        }
        //The below code was newly added
        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            citypol cpol=this.citypols.get(position);

            Intent intent= new Intent(this.ctx, FeedDetails.class);
            BitmapDrawable bd=(BitmapDrawable) ((NetworkImageView) v.findViewById(R.id.imageViewHero)).getDrawable();
            Bitmap b=bd.getBitmap();
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            bd.getBitmap().compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] imB=bos.toByteArray();


            intent.putExtra("image", imB);
            intent.putExtra("name",cpol.getName());
            intent.putExtra("location", cpol.getLocation());
            intent.putExtra("time", cpol.getTime());
            this.ctx.startActivity(intent);

        }
    }


}
