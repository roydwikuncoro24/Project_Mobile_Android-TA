package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;

import java.util.ArrayList;
import java.util.List;

public class GetInfoBarangResponse {

    int status_code;
    String status;
    List<BarangModelData> result_barang = new ArrayList<BarangModelData>();

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<BarangModelData> getResult_barang() {
        return result_barang;
    }
}
