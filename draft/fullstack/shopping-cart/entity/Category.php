<?php

include_once("BaseEntity.php");

class Category extends BaseEntity {
  public $name;

  function __construct($id, $name) {
    $this->id = $id;
    $this->name = $name;
  }

  static function find_by_id($id) {
    global $database;

    // TODO Fix SQL Injection
    $sql = "SELECT `id`, `name` FROM `categories` WHERE `id` = " . $id;
    $result = $database->query($sql);

    if ($result->num_rows > 0) {
      while($row = $result->fetch_assoc()) {
        return new Category($row["id"], $row["name"]);
      }
    }

    return null;
  }

  static function find_all() {
    global $database;

    $categories = [];

    $sql = "SELECT `id`, `name` FROM `categories`";
    $result = $database->query($sql);

    if ($result->num_rows > 0) {
      while($row = $result->fetch_assoc()) {
        array_push($categories, new Category($row["id"], $row["name"]));
      }
    }

    return $categories;
  }
}