<?php

require 'config.php';

$idtoko = $_POST['idtoko'];
$namakaryawan = $_POST['namakaryawan'];
$email = $_POST['email'];
$username = $_POST['username'];
$alamat = $_POST['alamat'];
$notelp = $_POST['notelp'];
$password = $_POST['password'];

$sql2 = "SELECT * FROM tb_karyawan WHERE username='" . $username . "'";
$result2 = mysqli_query($conn, $sql2);
$rows2 = mysqli_fetch_array($result2, MYSQLI_ASSOC);

if (($rows2 == NULL) && (($username != "") || ($password != ""))) {
    $sql = "INSERT INTO tb_karyawan (idtoko,namakaryawan,email,username,alamat,notelp,password,aktif) 
    VALUES ('" . $idtoko . "','" . $namakaryawan . "','" . $email . "','" . $username . "','" . $alamat . "','" . $notelp . "','" . $password . "',1)";
    $result = array();
    if ($conn->query($sql) === TRUE) {
        echo json_encode(array('status_code' => '1', 'status' => 'Register karyawan Success!'));
    } else {
        echo json_encode(array('status_code' => '0', 'status' => 'Register karyawan Gagal!'));
    }
} else {
    echo json_encode(array('status_code' => '2', 'status' => 'Username sudah terdaftar!'));
}

mysqli_close($conn);
