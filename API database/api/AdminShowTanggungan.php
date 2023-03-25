<?php

require 'config.php';

$idadmin = $_POST['idadmin'];


$sql = "SELECT * FROM tb_toko JOIN tb_tanggungan ON
tb_toko.idtoko = tb_tanggungan.idtoko WHERE 
tb_tanggungan.idadmin=" . $idadmin;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    $source = $row['tanggal'];
    $date = new DateTime($source);
    $row['tanggal'] = $date->format('Y-m-d');
    array_push($rows, $row);
}
$array = array();

if ($rows != NULL) {
    echo json_encode(array('status_code' => '1', 'status' => "found!", 'result_tanggungan' => $rows));
} else {
    echo json_encode(array('status_code' => '0', 'status' => "not found!", 'result_tanggungan' => $rows));
}
mysqli_close($conn);
