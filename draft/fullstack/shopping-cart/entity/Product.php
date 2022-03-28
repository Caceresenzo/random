<?php

include_once 'BaseEntity.php';

class Product extends BaseEntity
{
    public $name;
    public $description;
    public $price;
    public $image_url;
    public $created_at;
    public $category_id;

    function __construct(
        $id,
        $name,
        $description,
        $price,
        $image_url,
        $created_at,
        $category_id
    ) {
        $this->id = $id;
        $this->name = $name;
        $this->description = $description;
        $this->price = $price;
        $this->image_url = $image_url;
        $this->created_at = $created_at;
        $this->category_id = $category_id;
    }

    function getCategory()
    {
        return Category::find_by_id($this->category_id);
    }

    function save()
    {
        $this->_save([
            'name' => $this->name,
            'description' => $this->description,
            'price' => $this->price,
            'image_url' => $this->image_url,
            'category_id' => $this->category_id,
        ]);
    }

    function _getEntityTable()
    {
        return 'products';
    }

    static function findById($id)
    {
        global $database;

        $sql =
            "SELECT `id`, `name`, `description`, `price`, `image_url`, `created_at`, `category_id` FROM `products` WHERE `id` = '" . $id . "';";
        $result = $database->query($sql);

        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                return new Product(
                    $row['id'],
                    $row['name'],
                    $row['description'],
                    $row['price'],
                    $row['image_url'],
                    $row['created_at'],
                    $row['category_id']
                );
            }
        }

        return null;
    }

    static function findAll()
    {
        global $database;

        $products = [];

        $sql =
            'SELECT `id`, `name`, `description`, `price`, `image_url`, `created_at`, `category_id` FROM `products`';
        $result = $database->query($sql);

        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                array_push(
                    $products,
                    new Product(
                        $row['id'],
                        $row['name'],
                        $row['description'],
                        $row['price'],
                        $row['image_url'],
                        $row['created_at'],
                        $row['category_id']
                    )
                );
            }
        }

        return $products;
    }
}
