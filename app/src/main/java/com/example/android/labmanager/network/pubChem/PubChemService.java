package com.example.android.labmanager.network.pubChem;

import com.example.android.labmanager.model.Compound;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by katar on 25.06.2017.
 */

public interface PubChemService {


    @GET("compound/cid/{cidValue}/property/IUPACName,CanonicalSMILES,MolecularFormula,MolecularWeight/JSON")
    Call<Compound> getPubChemData(@Path("cidValue")Integer cidValue);

    @GET ("compound/cid/{cidValue}/PNG")
    Call<ResponseBody> getPng (@Path("cidValue")Integer cidValue);


}
