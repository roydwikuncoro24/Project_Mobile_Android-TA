<?php

require 'config.php';

$idadmin = $_POST['idadmin'];
$password = $_POST['password'];

$sql = "UPDATE tb_admin SET password='".$password."' 
WHERE idadmin=".$idadmin;

if ($conn->query($sql) === TRUE) {
    $result = mysqli_insert_id($conn);
    echo json_encode(array('status_code'=>'1', 'status'=>'Success!'));
}else{
    echo json_encode(array('status_code'=>'0', 'status'=>'Failed!'));
}
 
mysqli_close($conn);
