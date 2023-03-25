package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;

import java.util.ArrayList;
import java.util.List;

public class GetMitraNKaryawan {

    String status_code;
    String status;
    List<TokoModelData> result_mitra = new ArrayList<TokoModelData>();
    List<KaryawanModelData> result_karyawan = new ArrayList<KaryawanModelData>();

    public GetMitraNKaryawan(String status_code, String status,
                             List<TokoModelData> result_mitra,
                             List<KaryawanModelData> result_karyawan) {
        this.status_code = status_code;
        this.status = status;
        this.result_mitra = result_mitra;
        this.result_karyawan = result_karyawan;
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

    public List<TokoModelData> getResult_mitra() {
        return result_mitra;
    }

    public List<KaryawanModelData> getResult_karyawan() {
        return result_karyawan;
    }

    public void setResult_mitra(List<TokoModelData> result_mitra) {
        this.result_mitra = result_mitra;
    }

    public void setResult_karyawan(List<KaryawanModelData> result_karyawan) {
        this.result_karyawan = result_karyawan;
    }
}
