<?php
$servername = "localhost";
$username = "u599365724_roykuncoro";
$password = "elbastard24";
$dbname = "u599365724_pengembangan";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
