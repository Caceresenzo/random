<?php

class User {
  public $id;
  public $name;

  function __construct($id, $name) {
    $this->id = $id;
    $this->name = $name;
  }

  static function find_by_id($id) {
    global $database;

    $users = [];

    $sql = "SELECT `id`, `name` FROM `users` WHERE `id` = '" . $id . "';";
    $result = $database->query($sql);

    if ($result->num_rows > 0) {
      while($row = $result->fetch_assoc()) {
        return new User($row["id"], $row["name"]);
      }
    }

    return null;
  }

  static function find_all() {
    global $database;

    $users = [];

    $sql = "SELECT `id`, `name` FROM `users`";
    $result = $database->query($sql);

    if ($result->num_rows > 0) {
      while($row = $result->fetch_assoc()) {
        array_push($users, new User($row["id"], $row["name"]));
      }
    }

    return $users;
  }
}
