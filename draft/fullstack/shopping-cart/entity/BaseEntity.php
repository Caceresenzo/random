<?php

class BaseEntity {

  public $id;

  function _getEntityTable() {
    return "_unknown";
  }

  function _save($properties) {
    global $database;

    $columns = array_keys($properties);

    $sql = null;
    if ($this->id == 0) {
      $sql = "INSERT INTO `" . $this->_getEntityTable() . "` (";
      foreach ($columns as $column) {
        $sql .= "`" . $column . "`, ";
      }
      $sql = substr($sql, 0, strlen($sql) - 2);
      $sql .= ") VALUES (";
      foreach ($columns as $column) {
        $sql .= "'" . $properties[$column] . "', ";
      }
      $sql = substr($sql, 0, strlen($sql) - 2);
      $sql .= ");";
    } else {
      $sql = "UPDATE `" . $this->_getEntityTable() . "` SET ";
      foreach ($columns as $column) {
        $sql .= "`" . $column . "` = '". $properties[$column] . "', ";
      }
      $sql = substr($sql, 0, strlen($sql) - 2);
      $sql .= " WHERE `id` = '" . $this->id . "';";
    }

    
    if ($database->query($sql) == FALSE) {
      throw new Exception($database->error);
    }
    
    if ($this->id == 0) {
      $this->id = $database->insert_id;
    }
  }
}
