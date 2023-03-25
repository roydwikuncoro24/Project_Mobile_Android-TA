package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.TipeBarangModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTipeBarangResponse {
    @SerializedName("status_code")
    @Expose
    String status_code;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("result_tipebarang")
    @Expose
    List<TipeBarangModel> result_tipebarang;

    public GetTipeBarangResponse(String status_code, String status,
                                 List<TipeBarangModel> result_tipebarang) {
        this.status_code = status_code;
        this.status = status;
        this.result_tipebarang = result_tipebarang;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TipeBarangModel> getResult_tipebarang() {
        return result_tipebarang;
    }

    public void setResult_tipebarang(List<TipeBarangModel> result_tipebarang) {
        this.result_tipebarang = result_tipebarang;
    }
}
