<?php

require 'config.php';

$idadmin = $_POST['idadmin'];
$email = $_POST['email'];
$notelp = $_POST['notelp'];
$alamat = $_POST['alamat'];

$sql = "UPDATE tb_admin SET email='".$email."', 
notelp='".$notelp."', alamat='".$alamat."' 
WHERE idadmin=".$idadmin;

if ($conn->query($sql) === TRUE) {
    $result = mysqli_insert_id($conn);
    echo json_encode(array('status_code'=>'1', 'status'=>'Success!'));
}else{
    echo json_encode(array('status_code'=>'0', 'status'=>'Failed!'));
}
 
mysqli_close($conn);
