package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.BayarTanggunganModelData;
import com.example.stocki.ModelData.TanggunganModelData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BayarTanggunganGetResponse {

    @SerializedName("status_code")
    @Expose
    private String status_code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result_bayartanggungan")
    @Expose
    private ArrayList<BayarTanggunganModelData> result_bayartanggungan = null;


    // Getter Methods

    public String getStatus_code() {
        return status_code;
    }

    public String getStatus() {
        return status;
    }

    // Setter Methods

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<BayarTanggunganModelData> getResult_bayartanggungan() {
        return result_bayartanggungan;
    }

    public void setResult_bayartanggungan(ArrayList<BayarTanggunganModelData> result_bayartanggungan) {
        this.result_bayartanggungan = result_bayartanggungan;
    }
}
