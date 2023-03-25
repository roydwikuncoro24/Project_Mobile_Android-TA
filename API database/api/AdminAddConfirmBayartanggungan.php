<?php

require 'config.php';

$idtoko = $_POST['idtoko'];
$idadmin = $_POST['idadmin'];
$tanggal = date("Y-m-d");
$pembayaran = $_POST['pembayaran'];

$sql = "INSERT INTO tb_bayartanggungan (idtoko,idadmin,tanggal,pembayaran) 
VALUES (" . $idtoko . "," . $idadmin . ",'" . $tanggal . "','" . $pembayaran . "')";
if ($conn->query($sql) === TRUE) {
    echo json_encode(array('status_code' => '1', 'status' => 'Success!'));
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Failed!'));
}
mysqli_close($conn);
