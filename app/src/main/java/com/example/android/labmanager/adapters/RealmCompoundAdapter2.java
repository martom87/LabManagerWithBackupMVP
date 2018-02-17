package com.example.android.labmanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.labmanager.R;
import com.example.android.labmanager.db.DataBaseRealm;
import com.example.android.labmanager.model.Property;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;

import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Admin on 2017-07-10.
 */

public class RealmCompoundAdapter2 extends RealmRecyclerViewAdapter<Property, RealmCompoundAdapter2.CompoundHolder2> {

    public DataBaseRealm dataBaseRealm;
    public Context context;


    @Inject
    public RealmCompoundAdapter2(@Nullable OrderedRealmCollection<Property> propertyRealmResults, boolean autoUpdate, DataBaseRealm dataBaseRealm) {
        super(propertyRealmResults, autoUpdate);
        this.dataBaseRealm = dataBaseRealm;

    }


    @Override
    public CompoundHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RealmCompoundAdapter2.CompoundHolder2(LayoutInflater.from(context)
                .inflate(R.layout.item_compound_adapter2, parent, false));
    }

    @Override
    public void onBindViewHolder(CompoundHolder2 holder, int position) {
        if (getData() != null) {
            holder.setCompound(getData().get(position));
            holder.setStore(getData().get(position));


        }
    }

    class CompoundHolder2 extends RecyclerView.ViewHolder {

        Property deleteProperty;
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.cidNumber)
        TextView cidNumber;

        @BindView(R.id.cmpStore)
        TextView store;


        @OnClick(R.id.button2)
        public void onClick() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.makeSureToRemoveCmp).setTitle("Delete");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dataBaseRealm.deleteCompoundsFromDb(Integer.valueOf(deleteProperty.getCID()));
                    Toast.makeText(context, R.string.deletedCompound, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();


        }


        public CompoundHolder2(View itemView2) {
            super(itemView2);
            ButterKnife.bind(this, itemView2);
        }


        public Property setCompound(Property property) {
            name.setText(property.getIUPACName());
            cidNumber.setText(String.valueOf(property.getCID()));
            deleteProperty = property;
            return deleteProperty;
        }

        public void setStore(Property property) {
            store.setText(String.valueOf(property.getStore()));
        }


    }

}
