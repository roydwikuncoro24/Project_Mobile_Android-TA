package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;

import java.util.ArrayList;
import java.util.List;

public class GetProfilResponse {

    int status_code;
    String status;
    List<AdminModelData> result_admin = new ArrayList<AdminModelData>();
    List<TokoModelData> result_toko = new ArrayList<TokoModelData>();
    List<KaryawanModelData> result_karyawan = new ArrayList<KaryawanModelData>();

//    public GetProfilResponse(int status_code, String status,
//                                  List<AdminModelData> result_admin,
//                                  List<TokoModelData> result_toko,
//                                  List<KaryawanModelData> result_karyawan) {
//        this.status_code = status_code;
//        this.status = status;
//        this.result_admin = result_admin;
//        this.result_toko = result_toko;
//        this.result_karyawan = result_karyawan;
//    }

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


    public List<AdminModelData> getResult_admin() {
        return result_admin;
    }

    public void setResult_admin(List<AdminModelData> result_admin) {
        this.result_admin = result_admin;
    }

    public List<TokoModelData> getResult_toko() {
        return result_toko;
    }

    public void setResult_toko(List<TokoModelData> result_toko) {
        this.result_toko = result_toko;
    }

    public List<KaryawanModelData> getResult_karyawan() {
        return result_karyawan;
    }

    public void setResult_karyawan(List<KaryawanModelData> result_karyawan) {
        this.result_karyawan = result_karyawan;
    }
}
