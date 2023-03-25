<?php

require 'config.php';

$idkaryawan = $_POST['idkaryawan'];


$sql = "SELECT * FROM tb_karyawan WHERE aktif>0 AND idkaryawan=" . $idkaryawan;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    array_push($rows, $row);
}

$array = array();

if ($rows != NULL) {
    echo json_encode(array('status_code' => '1', 'status' => "found!", 'result_karyawan' => $rows));
} else {
    echo json_encode(array('status_code' => '0', 'status' => "not found!", 'result_karyawan' => $rows));
}
mysqli_close($conn);
