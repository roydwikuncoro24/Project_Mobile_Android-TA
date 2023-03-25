<?php

require 'config.php';

$idadmin = $_POST['idadmin'];


$sql = "SELECT * FROM tb_admin WHERE aktif>0 AND idadmin=" . $idadmin;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    array_push($rows, $row);
}

$array = array();

if ($rows != NULL) {
    echo json_encode(array('status_code' => '1', 'status' => "found!", 'result_admin' => $rows));
} else {
    echo json_encode(array('status_code' => '0', 'status' => "not found!", 'result_admin' => $rows));
}
mysqli_close($conn);
