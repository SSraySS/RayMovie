package me.synology.ssray73.raymovie;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by raymond on 12/7/15.
 */
public class GridviewThumbnailAdapter extends BaseAdapter{
    private final String LOG_TAG = GridviewThumbnailAdapter.class.getSimpleName();

    private Context mContext;

    private final List<String> urls = new ArrayList<String>();

    private final List<String> movieIds = new ArrayList<String>();




//    private String[] items;

    public GridviewThumbnailAdapter(Context mContext, ArrayList<String> urls, ArrayList<String> movieIds) {
        this.mContext = mContext;
        this.urls.addAll(urls);
        this.movieIds.addAll(movieIds);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = new ImageView(mContext);
        }
        ImageView imageView = (ImageView) convertView;



        String url = (String) getItem(position);
        Glide.with(mContext).load(url).into(imageView);
        return convertView;
    }


    public String getMovieId(int position) {
        return movieIds.get(position);
    }

    public void update(ArrayList<String> urls, ArrayList<String> movieIds){
       this.urls.clear();
       this.urls.addAll(urls);
       this.movieIds.clear();
       this.movieIds.addAll(movieIds);
       notifyDataSetChanged();
   }
}
