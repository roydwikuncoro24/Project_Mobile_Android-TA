<?php

require 'config.php';

$idadmin = $_POST['idadmin'];
$idtoko = $_POST['idtoko'];
$kode = $_POST['kode'];
$namabarang = $_POST['namabarang'];
$stok = $_POST['stok'];
$hargadasar = $_POST['hargadasar'];
$hargajual = $_POST['hargajual'];
$keterangan = $_POST['keterangan'];
$image = $_POST['image'];

$sql = "INSERT INTO tb_barang (idadmin,idtoko,kode,namabarang,stok,
hargadasar,hargajual,hargajualtoko,keterangan,image,idtipe,aset) 
VALUES (" . $idadmin . "," . $idtoko . ",'" . $kode . "',
'" . $namabarang . "','" . $stok . "','" . $hargadasar . "',
" . $hargajual . ",0,'" . $keterangan . "','" . $image . "',1,
 '" . $stok . "'*'" . $hargadasar . "')";
if ($conn->query($sql) === TRUE) {
    echo json_encode(array('status_code' => '1', 'status' => 'Success!'));
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Failed!'));
}

mysqli_close($conn);
