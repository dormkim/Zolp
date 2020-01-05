package com.example.zolup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PrivateListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<MapInfo> arrayList;

    public PrivateListAdapter(Context context, ArrayList<MapInfo> data) {
        mContext = context;
        arrayList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converview, ViewGroup viewGroup) {
        View view = mLayoutInflater.inflate(R.layout.private_listview, null);

        TextView placeName = view.findViewById(R.id.placeName);
        TextView placecontext = view.findViewById(R.id.placeContext);

        placeName.setText(arrayList.get(i).getName());
        placecontext.setText(arrayList.get(i).getContent());


        return view;
    }
}
