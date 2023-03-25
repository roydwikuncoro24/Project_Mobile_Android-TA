package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.TransaksiModelData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetTransaksiResponse {
    @SerializedName("status_code")
    @Expose
    private String status_code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result_transaksi")
    @Expose
    private ArrayList <TransaksiModelData> result_transaksi = null;


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

    public ArrayList<TransaksiModelData> getResult_transaksi() {
        return result_transaksi;
    }

    public void setResult_transaksi(ArrayList<TransaksiModelData> result_transaksi) {
        this.result_transaksi = result_transaksi;
    }
}
