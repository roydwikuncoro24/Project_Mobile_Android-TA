package com.example.stocki.service;

import com.example.stocki.ModelData.Penjualan;
import com.example.stocki.ModelResponse.BarangGetResponse;
import com.example.stocki.ModelResponse.BayarTanggunganGetResponse;
import com.example.stocki.ModelResponse.GetInfoBarangResponse;
import com.example.stocki.ModelResponse.GetMitraNKaryawan;
import com.example.stocki.ModelResponse.GetProfilResponse;
import com.example.stocki.ModelResponse.GetTipeBarangResponse;
import com.example.stocki.ModelResponse.GetTransaksiResponse;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.ModelResponse.LoginResponse;
import com.example.stocki.ModelResponse.PenjualanGetResponse;
import com.example.stocki.ModelResponse.TanggunganGetResponse;
import com.example.stocki.ModelResponse.UploadImageResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    //LOGIN
    @FormUrlEncoded
    @POST("AdminLogin.php")
    Call<LoginResponse> loginAdmin(@Field("username") String username,
                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("TokoLogin.php")
    Call<LoginResponse> loginToko(@Field("username") String username,
                                 @Field("password") String password);
    @FormUrlEncoded
    @POST("KaryawanLogin.php")
    Call<LoginResponse> loginKaryawan(@Field("username") String username,
                                      @Field("password") String password);
    //LupaPassword
    @FormUrlEncoded
    @POST("AdminLupaPassword.php")
    Call<LoginResponse> lupaPassAdmin(@Field("username") String username);
    @FormUrlEncoded
    @POST("TokoLupaPassword.php")
    Call<LoginResponse> lupaPassToko(@Field("username") String username);
    @FormUrlEncoded
    @POST("KaryawanLupaPassword.php")
    Call<LoginResponse> lupaPassKaryawan(@Field("username") String username);

    //UpdatePassword
    @FormUrlEncoded
    @POST("AdminUpdatePassword.php")
    Call<InsertResponse> updatePassAdmin(@Field("idadmin") String idadmin,
                                         @Field("password") String password);
    @FormUrlEncoded
    @POST("TokoUpdatePassword.php")
    Call<InsertResponse> updatePassToko(@Field("idtoko") String idtoko,
                                         @Field("password") String password);
    @FormUrlEncoded
    @POST("KaryawanUpdatePassword.php")
    Call<InsertResponse> updatePassKaryawan(@Field("idkaryawan") String idkaryawan,
                                            @Field("password") String password);

    //Profil
    @FormUrlEncoded
    @POST("AdminGet.php")
    Call<GetProfilResponse> getAdmin(@Field("idadmin") String idadmin);
    @FormUrlEncoded
    @POST("TokoGet.php")
    Call<GetProfilResponse> getToko(@Field("idtoko") String idtoko);
    @FormUrlEncoded
    @POST("KaryawanGet.php")
    Call<GetProfilResponse> getKaryawan(@Field("idkaryawan") String idkaryawan);

    //RegisterTambahMitraKaryawan
    @FormUrlEncoded
    @POST("AdminRegister.php")
    Call<InsertResponse> AdminRegister(@Field("nama") String nama,
                                       @Field("username") String username,
                                       @Field("email") String email,
                                       @Field("alamat") String alamat,
                                       @Field("notelp") String notelp,
                                       @Field("password") String password);
    @FormUrlEncoded
    @POST("AdminAddMitra.php")
    Call<InsertResponse> AdminAddMitra(@Field("idadmin") String idadmin,
                                       @Field("namatoko") String namatoko,
                                       @Field("namapemilik") String namapemilik,
                                       @Field("email") String email,
                                       @Field("username") String username,
                                       @Field("alamat") String alamat,
                                       @Field("notelp") String notelp,
                                       @Field("password") String password);
    @FormUrlEncoded
    @POST("TokoAddKaryawan.php")
    Call<InsertResponse> TokoAddKaryawan(@Field("idtoko") String idtoko,
                                         @Field("namakaryawan") String namakaryawan,
                                         @Field("username") String username,
                                         @Field("email") String email,
                                         @Field("alamat") String alamat,
                                         @Field("notelp") String notelp,
                                         @Field("password") String password);


    //UpdateProfil
    @FormUrlEncoded
    @POST("AdminUpdateProfil.php")
    Call<InsertResponse> updateProfilAdmin(@Field("idadmin") String idadmin,
                                           @Field("email") String email,
                                           @Field("notelp") String notelp,
                                           @Field("alamat") String alamat);
    @FormUrlEncoded
    @POST("KaryawanUpdateProfil.php")
    Call<InsertResponse> updateProfilKaryawan(@Field("idkaryawan") String idkaryawan,
                                           @Field("email") String email,
                                           @Field("notelp") String notelp,
                                           @Field("alamat") String alamat);
    @FormUrlEncoded
    @POST("TokoUpdateProfil.php")
    Call<InsertResponse> updateProfilToko(@Field("idtoko") String idtoko,
                                          @Field("email") String email,
                                          @Field("namatoko") String namatoko,
                                          @Field("notelp") String notelp,
                                          @Field("alamat") String alamat);

    //ShowKaryawanMitra
    @FormUrlEncoded
    @POST("AdminShowMitra.php")
    Call<GetMitraNKaryawan> showMitra(@Field("idadmin") String idadmin);
    @FormUrlEncoded
    @POST("TokoShowKaryawan.php")
    Call<GetMitraNKaryawan> showKaryawan(@Field("idtoko") String idtoko);

    //DeleteKaryawanMitra
    @FormUrlEncoded
    @POST("AdminDeleteMitra.php")
    Call<InsertResponse> deleteMitra(@Field("idtoko") String idtoko);
    @FormUrlEncoded
    @POST("TokoDeleteKaryawan.php")
    Call<InsertResponse> deleteKaryawan(@Field("idkaryawan") String idkaryawan);

    //KELOLA BARANG ADMIN//
    @Multipart
    @POST("uploadImage.php")
    Call<UploadImageResponse> uploadImage(@Part MultipartBody.Part fileToUpload);

    @FormUrlEncoded
    @POST("AdminShowBarang.php")
    Call<BarangGetResponse> showBarangAdmin(@Field("idadmin") String idadmin);
    @FormUrlEncoded
    @POST("AdminAddBarang.php")
    Call<InsertResponse> addBarang(@Field("idadmin") String idadmin,
                                   @Field("idtoko") String idtoko,
                                   @Field("kode") String kode,
                                   @Field("namabarang") String namabarang,
                                   @Field("stok") String stok,
                                   @Field("hargadasar") String hargadasar,
                                   @Field("hargajual") String hargajual,
                                   @Field("keterangan") String keterangan,
                                   @Field("image") String image);
    @FormUrlEncoded
    @POST("AdminUpdateBarang.php")
    Call<InsertResponse> updateBarang(@Field("id") String id,
                                      @Field("idtoko") String idtoko,
                                      @Field("namabarang") String namabarang,
                                      @Field("kode") String kode,
                                      @Field("hargadasar") String hargadasar,
                                      @Field("stok") String stok,
                                      @Field("keterangan") String keterangan,
                                      @Field("hargajual") String hargajual,
                                      @Field("image") String image);
    @FormUrlEncoded
    @POST("DeleteBarang.php")
    Call<InsertResponse> deleteBarang(@Field("id") String id);


    //KELOLABARANG TOKO
    @GET("showTipeBarang.php")
    Call<GetTipeBarangResponse> showTipebarang();
    @FormUrlEncoded
    @POST("TokoShowBarang.php")
    Call<BarangGetResponse> showBarangToko(@Field("idtoko") String idtoko);
    @FormUrlEncoded
    @POST("TokoAddBarang.php")
    Call<InsertResponse> TokoAddBarang(
            @Field("idtoko") String idtoko,
            @Field("kode") String kode,
            @Field("namabarang") String namabarang,
            @Field("stok") String stok,
            @Field("hargajual") String hargajual,
            @Field("hargajualtoko") String hargajualtoko,
            @Field("keterangan") String keterangan,
            @Field("image") String image);
    @FormUrlEncoded
    @POST("TokoUpdateBarang.php")
    Call<InsertResponse> TokoUpdateBarang(@Field("id") String id,
                                          @Field("namabarang") String namabarang,
                                          @Field("kode") String kode,
                                          @Field("hargajual") String hargajual,
                                          @Field("stok") String stok,
                                          @Field("keterangan") String keterangan,
                                          @Field("hargajualtoko") String hargajualtoko,
                                          @Field("image") String image,
                                          @Field("idtipe") String idtipe);

    @POST("TokoAddTransaksi.php")
    Call<InsertResponse> addTransaksi(@Body Penjualan penjualan);

    @FormUrlEncoded
    @POST("TokoShowPenjualan.php")
    Call<PenjualanGetResponse> showPenjualanToko(@Field("idtoko") String idtoko);
    @FormUrlEncoded
    @POST("TokoShowTransaksi.php")
    Call<GetTransaksiResponse> showDataTransaksiToko(@Field("idtoko") String idtoko);
    @FormUrlEncoded
    @POST("TokoShowTransaksiDetail.php")
    Call<GetTransaksiResponse> showDataTransaksiTokoDetail(@Field("id") String idtransaksi);

    @FormUrlEncoded
    @POST("TokoShowListTransaksi.php")
    Call<PenjualanGetResponse> showListTransaksiToko(@Field("idtoko") String idtoko,
                                                  @Field("idtransaksi") String idtransaksi);

    @FormUrlEncoded
    @POST("TokoShowTanggungan.php")
    Call<TanggunganGetResponse> showTanggunganToko(@Field("idtoko") String idtoko);
    @FormUrlEncoded
    @POST("TokoShowBayarTanggungan.php")
    Call<BayarTanggunganGetResponse> showBayarTanggunganToko(@Field("idtoko") String idtoko);

    @FormUrlEncoded
    @POST("AdminShowPenjualan.php")
    Call<PenjualanGetResponse> showDataPenjualanAdmin(@Field("idadmin") String idadmin);
    @FormUrlEncoded
    @POST("AdminAddConfirmBayartanggungan.php")
    Call<InsertResponse> addBayarTanggungan(@Field("idtoko") String idtoko,
                                            @Field("idadmin") String idadmin,
                                            @Field("pembayaran") String pembayaran,
                                            @Field("keterangan") String keterangan);
    @FormUrlEncoded
    @POST("AdminResetJualanToko.php")
    Call<InsertResponse> resetData(@Field("idtoko") String idtoko);
    @FormUrlEncoded
    @POST("AdminShowTanggungan.php")
    Call<TanggunganGetResponse> showTanggunganAdmin(@Field("idadmin") String idadmin);
    @FormUrlEncoded
    @POST("AdminShowBayarTanggungan.php")
    Call<BayarTanggunganGetResponse> showBayarTanggunganAdmin(@Field("idadmin") String idadmin);

    @FormUrlEncoded
    @POST("BarangShowPenjualan.php")
    Call<PenjualanGetResponse> showDataPenjualanBarang(@Field("idbarang") String idbarang);
    @FormUrlEncoded
    @POST("BarangGet.php")
    Call<GetInfoBarangResponse> showDataBarang(@Field("idbarang") String idbarang);
}
