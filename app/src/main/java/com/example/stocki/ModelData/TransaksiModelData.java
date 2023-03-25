package com.example.stocki.ModelData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransaksiModelData {
    String id,namatoko,idtoko,idadmin,tanggal,modal,jual,jumlah,
           petugas,tipe;

    // Getter Methods

    public String getId() {
        return id;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public String getIdadmin() {
        return idadmin;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getModal() {
        return modal;
    }

    public String getJual() {
        return jual;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getPetugas() {
    return petugas;
    }

    public String getTipe() {
        return tipe;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public void setIdadmin(String idadmin) {
        this.idadmin = idadmin;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public void setJual(String jual) {
        this.jual = jual;
    }

    public void setPetugas(String petugas) {
        this.petugas = petugas;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
}
