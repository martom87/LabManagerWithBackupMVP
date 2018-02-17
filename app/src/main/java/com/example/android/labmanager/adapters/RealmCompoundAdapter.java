package com.example.android.labmanager.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.labmanager.R;
import com.example.android.labmanager.model.Property;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Admin on 2017-07-10.
 */

public class RealmCompoundAdapter extends RealmRecyclerViewAdapter<Property, RealmCompoundAdapter.CompoundHolder> {

    public RealmCompoundAdapter(@Nullable OrderedRealmCollection data, boolean autoUpdate) {
        super(data, autoUpdate);

    }

    @Override
    public CompoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RealmCompoundAdapter.CompoundHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compound_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(CompoundHolder holder, int position) {
        if (getData() != null) {
            holder.setCompound(getData().get(position));
            holder.setStore(getData().get(position));
        }
    }

    class CompoundHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.cidNumber)
        TextView cidNumber;

        @BindView(R.id.cmpStore)
        TextView store;


        public CompoundHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setCompound(Property property) {
            name.setText(property.getIUPACName());
            cidNumber.setText(String.valueOf(property.getCID()));

        }

        public void setStore(Property property) {
            store.setText(String.valueOf(property.getStore()));
        }


    }

}
