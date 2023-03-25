package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.PenjualanModelData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PenjualanGetResponse {
    @SerializedName("status_code")
    @Expose
    private String status_code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result_penjualan")
    @Expose
    private ArrayList <PenjualanModelData> result_penjualan = null;

    public String getStatus_code() {
        return status_code;
    }

    public String getStatus() {
        return status;
    }


    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<PenjualanModelData> getResult_penjualan() {
        return result_penjualan;
    }

    public void setResult_penjualan(ArrayList<PenjualanModelData> result_penjualan) {
        this.result_penjualan = result_penjualan;
    }
}
