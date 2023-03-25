<?php
require 'config.php';

$username = $_POST['username'];
$password = $_POST['password'];


$sql = "SELECT * FROM tb_admin WHERE username = '" . $username . "' AND password = '" . $password . "' AND aktif>0";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_array($result, MYSQLI_ASSOC);

if ($row != NULL) {
    echo json_encode(array('status_code' => 1, 'status' => "User found!", 'result_admin' => array($row)));
} else {
    echo json_encode(array('status_code' => 0, 'status' => "Username atau password salah!"));
}

mysqli_close($conn);
