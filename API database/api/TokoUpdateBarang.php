<?php

require 'config.php';

$idbarang = $_POST['id'];
$kode = $_POST['kode'];
$namabarang = $_POST['namabarang'];
$stok = $_POST['stok'];
$hargajual = $_POST['hargajual'];
$hargajualtoko = $_POST['hargajualtoko'];
$keterangan = $_POST['keterangan'];
$image = $_POST['image'];
$idtipe = $_POST['idtipe'];

$flag = 1;
$deleteImage = 0;

$sql = "SELECT * FROM tb_barang WHERE id=" . $idbarang;
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_array($result, MYSQLI_ASSOC);

if ($row != NULL) {
    if ($row['image'] == $image) {
        $deleteImage = 1;
    } else {
        $destination_path = getcwd() . DIRECTORY_SEPARATOR;
        $destination_path =  substr($destination_path, 0, (strlen($destination_path) - 4));
        $target_file = preg_split("/usahaskripsi/", $row['image']);
        $target_file = $destination_path . substr($target_file[2], 1);
        if (file_exists($target_file)) {
            unlink($target_file);
        }
        $deleteImage = 0;
    }
}

if ($idtipe == '1') {
    $sql = "UPDATE tb_barang SET hargajualtoko='" . $hargajualtoko . "', 
    keterangan='" . $keterangan . "' WHERE id=" . $idbarang;
    if ($conn->query($sql) === TRUE) {
        $flag = 1;
    }
}
if ($idtipe == '2') {
    $sql = "UPDATE tb_barang SET namabarang='" . $namabarang . "',
    kode='" . $kode . "', 
    hargajual='" . $hargajual . "', 
    stok=" . $stok . ", hargajualtoko='" . $hargajualtoko . "', 
    keterangan='" . $keterangan . "', image='" . $image . "',
    aset=" . $stok . "*" . $hargajual . " 
    WHERE id=" . $idbarang;
    if ($conn->query($sql) === TRUE) {
        $flag = 1;
    }
}


if ($flag = 1) {
    echo json_encode(array('status_code' => '1', 'status' => 'Success!'));
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Failed!'));
}

mysqli_close($conn);
