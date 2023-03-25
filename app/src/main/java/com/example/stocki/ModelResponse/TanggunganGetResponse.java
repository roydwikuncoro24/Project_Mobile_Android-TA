package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.TanggunganModelData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TanggunganGetResponse {

    @SerializedName("status_code")
    @Expose
    private String status_code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result_tanggungan")
    @Expose
    private ArrayList<TanggunganModelData> result_tanggungan = null;


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

    public ArrayList<TanggunganModelData> getResult_tanggungan() {
        return result_tanggungan;
    }

    public void setResult_tanggungan(ArrayList<TanggunganModelData> result_tanggungan) {
        this.result_tanggungan = result_tanggungan;
    }
}
