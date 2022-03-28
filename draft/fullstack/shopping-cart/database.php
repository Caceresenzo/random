<?php

include_once('config.php');

$database = new mysqli(DATABASE_HOST, DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME);

if ($database->connect_error) {
    die("Connection failed: " . $database->connect_error);
}
