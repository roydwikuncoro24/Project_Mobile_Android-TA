<?php

require 'config.php';

$idkaryawan = $_POST['idkaryawan'];
$email = $_POST['email'];
$notelp = $_POST['notelp'];
$alamat = $_POST['alamat'];

$sql = "UPDATE tb_karyawan SET email='".$email."', 
notelp='".$notelp."', alamat='".$alamat."' 
WHERE idkaryawan=".$idkaryawan;

if ($conn->query($sql) === TRUE) {
    $result = mysqli_insert_id($conn);
    echo json_encode(array('status_code'=>'1', 'status'=>'Success!'));
}else{
    echo json_encode(array('status_code'=>'0', 'status'=>'Failed!'));
}
 
mysqli_close($conn);
