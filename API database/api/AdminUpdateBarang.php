<?php

require 'config.php';

$idbarang = $_POST['id'];
$idtoko = $_POST['idtoko'];
$kode = $_POST['kode'];
$namabarang = $_POST['namabarang'];
$stok = $_POST['stok'];
$hargadasar = $_POST['hargadasar'];
$hargajual = $_POST['hargajual'];
$keterangan = $_POST['keterangan'];
$image = $_POST['image'];

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
        $target_file = preg_split("/usahakripsi/", $row['image']);
        $target_file = $destination_path . substr($target_file[2], 1);
        if (file_exists($target_file)) {
            unlink($target_file);
        }
        $deleteImage = 0;
    }
}

$sql = "UPDATE tb_barang SET idtoko=" . $idtoko . ", 
namabarang='" . $namabarang . "',
kode='" . $kode . "', 
hargadasar='" . $hargadasar . "',
stok=" . $stok . ", 
hargajual='" . $hargajual . "',
keterangan='" . $keterangan . "', 
image='" . $image . "',
aset=" . $stok . "*" . $hargadasar . "
WHERE id=" . $idbarang;
if ($conn->query($sql) === TRUE) {
    $last = mysqli_insert_id($conn);
    echo json_encode(array('status_code' => '1', 'status' => 'Success!'));
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Failed!'));
}

mysqli_close($conn);
