<?php

require 'config.php';

$idtoko = $_POST['idtoko'];
$flag = 1;

$sql = "DELETE from tb_penjualan WHERE idtoko=" . $idtoko;
if ($conn->query($sql) === TRUE) {
    $flag = 1;
} else {
    $flag = 0;
}

$sql = "DELETE from tb_transaksi WHERE idtoko=" . $idtoko;
if ($conn->query($sql) === TRUE) {
    $flag = 1;
} else {
    $flag = 0;
}

$sql = "DELETE from tb_bayartanggungan WHERE idtoko=" . $idtoko;
if ($conn->query($sql) === TRUE) {
    $flag = 1;
} else {
    $flag = 0;
}

$sql = "DELETE from tb_tanggungan WHERE idtoko=" . $idtoko;
if ($conn->query($sql) === TRUE) {
    $flag = 1;
} else {
    $flag = 0;
}

if ($flag == 1) {
    echo json_encode(array('status_code' => '1', 'status' => 'Success!'));
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Gagal!'));
}

mysqli_close($conn);
