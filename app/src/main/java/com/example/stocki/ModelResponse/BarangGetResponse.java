package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.BarangModelData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BarangGetResponse {
    @SerializedName("status_code")
    @Expose
    private String status_code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result_barang")
    @Expose
    private ArrayList <BarangModelData> result_barang = null;

//    public BarangGetResponse(int status_code, String status, List<BarangModelData> result_barang) {
//        this.status_code = status_code;
//        this.status = status;
//        this.result_barang = result_barang;
//    }

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

    public ArrayList<BarangModelData> getResult_barang() {
        return result_barang;
    }

    public void setResult_barang(ArrayList<BarangModelData> result_barang) {
        this.result_barang = result_barang;
    }
}
