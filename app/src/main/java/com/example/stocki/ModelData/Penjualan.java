package com.example.stocki.ModelData;

import java.util.List;

public class Penjualan {
    String idtoko;
    String modal;
    String jual;
    String jumlah;
    String tipe;
    String namatoko;
    String namapetugas;
    List<BarangModelData> barangList;

    public Penjualan(String idtoko, String modal, String jual, String jumlah,
                     String tipe, String namapetugas,String namatoko, List<BarangModelData> barangList) {
        this.idtoko = idtoko;
        this.modal = modal;
        this.jual = jual;
        this.jumlah = jumlah;
        this.tipe = tipe;
        this.namapetugas = namapetugas;
        this.namatoko = namatoko;
        this.barangList = barangList;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public String getModal() {
        return modal.replace(",","");
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getJual() {
        return jual.replace(",","");
    }

    public void setJual(String jual) {
        this.jual = jual;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public void setNamapetugas(String namapetugas) {
        this.namapetugas = namapetugas;
    }

    public String getNamapetugas() {
        return namapetugas;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public List<BarangModelData> getBarangList() {
        return barangList;
    }

    public void setBarangList(List<BarangModelData> barangList) {
        this.barangList = barangList;
    }
}
