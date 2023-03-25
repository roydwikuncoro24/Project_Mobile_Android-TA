<?php
require 'config.php';

$username = $_POST['username'];
$password = $_POST['password'];


$sql = "SELECT * FROM tb_karyawan WHERE username = '" . $username . "' AND password = '" . $password . "' AND aktif>0";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_array($result, MYSQLI_ASSOC);

if ($row != NULL) {
    $sql = "SELECT * FROM tb_toko WHERE idtoko = " . $row['idtoko'];
    $result2 = mysqli_query($conn, $sql);
    $row2 = mysqli_fetch_array($result2, MYSQLI_ASSOC);
    echo json_encode(array('status_code' => 1, 'status' => "User found!", 'result_karyawan' => array($row), 'result_toko' => array($row2)));
    // echo json_encode(array('status_code' => 1, 'status' => "User found!", 'result_karyawan' => array($row)));
} else {
    echo json_encode(array('status_code' => 0, 'status' => "Username atau password salah!"));
}

mysqli_close($conn);
