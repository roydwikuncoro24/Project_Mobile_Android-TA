package com.example.stocki.ModelResponse;

import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse {

    @SerializedName("status_code")
    int status_code;
    @SerializedName("status")
    String status;
    @SerializedName("result_toko")
    List<TokoModelData> result_toko;
    List<KaryawanModelData> result_karyawan;
    List<AdminModelData> result_admin;

    public LoginResponse(int status_code, String status,
                         List<TokoModelData> result_toko,
                         List<AdminModelData> result_admin,
                         List<KaryawanModelData> result_karyawan) {
        this.status_code = status_code;
        this.status = status;
        this.result_toko = result_toko;
        this.result_karyawan = result_karyawan;
        this.result_admin = result_admin;
    }

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

//{"status_code":1,"status":"User found!","result_toko":[{"idtoko":"68","idadmin":"2","namatoko":"Toko
//Ku","namapemilik":"Dwi Kuncoro","username":"kuncoro24","email":"dwi@gmail.com","alamat":"Jl. Nasional No
//3","notelp":"08953840","password":"inisandi24","aktif":"1"}]}
