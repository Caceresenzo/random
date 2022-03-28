<?php

class CartProduct {
  public $product_id;
  public $user_id;
  private $exists;

  function __construct($user_id, $product_id) {
    $this->user_id = $user_id;
    $this->product_id = $product_id;
    $this->exists = false;
  }

  function getProduct() {
    return Product::findById($this->product_id);
  }

  function save() {
    global $database;

    $sql = "INSERT IGNORE INTO `cart_products` (`user_id`, `product_id`) VALUES ('" . $this->user_id . "', '" . $this->product_id . "');";
    if ($database->query($sql) == FALSE) {
      throw new Exception($database->error);
    }

    $this->exists = true;
  }

  function delete() {
    global $database;
    
    $sql = "DELETE FROM `cart_products` WHERE `product_id` = '" . $this->product_id . "' AND `user_id` = '" . $this->user_id . "';";
    $database->query($sql);
  }

  static function findByProductAndUser($product, $user) {
    global $database;

    // TODO Fix SQL Injection
    $sql = "SELECT `user_id`, `product_id` FROM `cart_products` WHERE `product_id` = '" . $product->id . "' AND `user_id` = '" . $user->id . "';";
    $result = $database->query($sql);

    if ($result->num_rows > 0) {
      while($row = $result->fetch_assoc()) {
        $entry = new CartProduct($row["user_id"], $row["product_id"]);
        $entry->exists = true;
        
        return $entry;
      }
    }

    return null;
  }

  static function findAllByUser($user) {
    global $database;

    $entries = [];
    // TODO Fix SQL Injection
    $sql = "SELECT `user_id`, `product_id` FROM `cart_products` WHERE `user_id` = '" . $user->id . "';";
    $result = $database->query($sql);

    if ($result->num_rows > 0) {
      while($row = $result->fetch_assoc()) {
        $entry = new CartProduct($row["user_id"], $row["product_id"]);
        $entry->exists = true;
        
        array_push($entries, $entry);
      }
    }

    return $entries;
  }
}