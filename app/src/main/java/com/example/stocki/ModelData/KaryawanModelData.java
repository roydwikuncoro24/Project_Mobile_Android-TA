package com.example.stocki.ModelData;

public class KaryawanModelData {
    String idkaryawan, idtoko,namatoko, namakaryawan, email, alamat, username, notelp, aktif;

    //GET
    public String getIdkaryawan(){
        return idkaryawan;
    }

    public String getIdtoko(){
        return idtoko;
    }

    public String getNamakaryawan(){
        return namakaryawan;
    }

    public String getEmail(){
        return email;
    }

    public String getNamatoko(){
        return namatoko;
    }

    public String getAlamat(){
        return alamat;
    }

    public String getUsername(){
        return username;
    }

    public String getNotelp(){
        return notelp;
    }

    public String getAktif(){
        return aktif;
    }

    //SET
    public void setIdkaryawan(String idkaryawan){
        this.idkaryawan = idkaryawan;
    }

    public void setIdtoko(String idtoko){
        this.idtoko = idtoko;
    }

    public void setNamakaryawan(String namakaryawan){
        this.namakaryawan = namakaryawan;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setNamatoko(String namatoko){
        this.namatoko = namatoko;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setNotelp(String notelp){
        this.notelp = notelp;
    }

    public void setAktif(String aktif){
        this.aktif = aktif;
    }
}
