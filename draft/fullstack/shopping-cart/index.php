<?php

include_once 'database.php';
include_once 'entity/User.php';
include_once 'entity/Category.php';
include_once 'entity/Product.php';
include_once 'entity/CartProduct.php';

if (!session_id()) {
    session_start();
}

if (isset($_GET['action'])) {
    switch ($_GET['action']) {
        case 'login':
            $user_id = $_POST['user_id'];

            $user = User::find_by_id($user_id);

            if ($user != null) {
                $_SESSION['user'] = $user;
            } else {
                unset($_SESSION['user']);
            }

            header('Location: /');

            break;

        case 'logout':
            unset($_SESSION['user']);

            header('Location: /');

            break;

        case 'create-product':
            $name = $_POST['title'];
            $description = $_POST['description'];
            $price = $_POST['price'];
            $category_id = 1;

            $product = new Product(
                0,
                $name,
                $description,
                $price,
                null /* image_url */,
                null /* created_at */,
                $category_id
            );
            $product->save();

            header('Location: /');

            break;

        case 'add-to-cart':
            $product_id = $_POST['product_id'];

            $product = Product::findById($product_id);
            if ($product != null) {
                $cartProduct = new CartProduct($_SESSION['user']->id, $product->id);
                $cartProduct->save();
            }

            header('Location: /');

            break;

        case 'remove-from-cart':
            $product_id = $_POST['product_id'];

            $product = Product::findById($product_id);
            if ($product != null) {
                $cartProduct = CartProduct::findByProductAndUser(
                    $product,
                    $_SESSION['user']
                );

                if ($cartProduct != null) {
                    $cartProduct->delete();
                }
            }

            header('Location: /');

            break;
    }
}

if (isset($_SESSION['user'])) {
    require 'views/shop.php';
} else {
    require 'views/login.php';
}

?>
