<?php

require 'config.php';

$idtoko = $_POST['idtoko'];
$kode = $_POST['kode'];
$namabarang = $_POST['namabarang'];
$stok = $_POST['stok'];
$hargajual = $_POST['hargajual'];
$hargajualtoko = $_POST['hargajualtoko'];
$keterangan = $_POST['keterangan'];
$image = $_POST['image'];


$sql = "INSERT INTO tb_barang (
    idadmin,idtoko,kode,namabarang,stok,hargadasar,
hargajual,hargajualtoko,keterangan,image,idtipe,aset) 
VALUES (0," . $idtoko . ",'" . $kode . "',
'" . $namabarang . "','" . $stok . "',0,'" . $hargajual . "',
" . $hargajualtoko . ",'" . $keterangan . "','" . $image . "',2,
'" . $stok . "'*'" . $hargajual . "')";
if ($conn->query($sql) === TRUE) {
    echo json_encode(array('status_code' => '1', 'status' => 'Success!'));
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Failed!'));
}

mysqli_close($conn);
