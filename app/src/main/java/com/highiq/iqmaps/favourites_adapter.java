package com.highiq.iqmaps;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class favourites_adapter extends ArrayAdapter<favouritesClass> {
    private Activity context;
    private List<favouritesClass> fAdapter;

    public favourites_adapter(Activity context, List<favouritesClass> fAdapter) {
        super(context,R.layout.favourites_items, fAdapter);
        this.context = context;
        this.fAdapter = fAdapter;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.favourites_items, null, true);
        TextView textFavourite = (TextView) listViewItem.findViewById(R.id.txtFavouriteListItems);

        favouritesClass favourites = fAdapter.get(position);
        String txt = "Description: " + favourites.getFavDesc() +
                " \n Lat: " +favourites.getFavLat()+
                " \n Long: " + favourites.getFavLong();
        textFavourite.setText(txt);
        return listViewItem;

    }
}
