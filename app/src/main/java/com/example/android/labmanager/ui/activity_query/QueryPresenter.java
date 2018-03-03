package com.example.android.labmanager.ui.activity_query;

import com.example.android.labmanager.R;
import com.example.android.labmanager.model.Compound;
import com.example.android.labmanager.model.Property;
import com.example.android.labmanager.network.pubChem.PubChemClient;
import com.example.android.labmanager.utilis.SharedPrefStorage;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 2017-07-20.
 */

public class QueryPresenter {

    private PubChemClient pubChemClient;
    private Property property;
    private QueryView queryView;
    private SharedPrefStorage sharedPrefStorage;

    @Inject
    public QueryPresenter(PubChemClient pubChemClient, SharedPrefStorage sharedPrefStorage) {
        this.pubChemClient = pubChemClient;
        this.sharedPrefStorage = sharedPrefStorage;
    }

    public void attachQueryView(QueryView queryView) {
        this.queryView = queryView;
    }

    public void detachQueryView() {
        this.queryView = null;
    }


    public void setCidInput(String cidValue) {
        if (cidValue.isEmpty() || (!cidValue.matches("-?\\d+(\\.\\d+)?")) || cidValue.matches("[^123456789]+")) {

            queryView.showErrorMessage(R.string.errorMessage);
        } else {

            getCmpData(Integer.valueOf(cidValue));
            getCmpPng(Integer.valueOf(cidValue));

        }
    }

    public void getCmpData(Integer cidValue) {

        pubChemClient.getService().getPubChemData(cidValue).enqueue(new Callback<Compound>() {

            @Override
            public void onResponse(Call<Compound> call, Response<Compound> response) {
                if (response.isSuccessful()) {
                    Compound compound = response.body();

                    property = compound.getPropertyTable().getProperties().get(0);
                    sharedPrefStorage.writeProperty(property);


                    queryView.goToCompoundsCardActivity();
                } else {
                    queryView.showErrorMessage(R.string.errorMessage);
                }


            }

            @Override
            public void onFailure(Call<Compound> call, Throwable t) {
                queryView.showErrorMessage(R.string.InternetConnectionFailure);
            }
        });


    }

    public void getCmpPng(Integer cidValue) {

        pubChemClient.getService().getPng(Integer.valueOf(String.valueOf(cidValue))).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                } else {
                    queryView.showErrorMessage(R.string.errorMessage);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                queryView.showErrorMessage(R.string.InternetConnectionFailure);
            }
        });
    }



}


