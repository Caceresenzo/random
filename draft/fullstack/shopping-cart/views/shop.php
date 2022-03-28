<?php
$currentUser = $_SESSION['user'];
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
    <style>
        body {
            padding: 2rem;
            margin: 0;
        }

        .content {
            display: flex;
            justify-content: space-between;
        }

        .section {
            width: 100%;
        }

        .list {
            display: flex;
            flex-wrap: wrap;
            list-style: none;
            padding: 0;
            margin: 0;
            width: 100%;
            gap: 2rem;
        }

        .item {
            border: 1px solid black;
            display: flex;
            align-items: center;
            position: relative;
            width: 45%;
        }

        .item-informations {
            margin-left: 2rem;
        }

        .item-price {
            position: absolute;
            bottom: 2rem;
            right: 2rem;
        }

        .item > img {
          max-width: 300px;
        }

        .cart {
            min-width: 200px;
            max-height: calc(100vh - 8rem);
            position: sticky;
            top: 2rem;
            border: 1px solid black;
            padding: 2rem;
            margin-left: 2rem;
        }

        footer {
            margin-top: 2rem;
        }

        form {
            display: flex;
            align-items: flex-start;
            gap: 1rem;
        }
    </style>
</head>
<body>
  
    <form action="?action=logout" method="post" enctype="multipart/form-data">
        <input type="submit" value="Se deconnecter">
    </form>

    <main class="content">
        <section class="section">
            <ul class="list">
<?php
foreach (Product::findALl() as $product) {
  $category = $product->getCategory();

  ?>
              <li class="item">
                  <img class="item-image" src="<?php echo htmlspecialchars($product->image_url); ?>" alt="Preview">
                  <div class="item-informations">
                      <h1><?php echo htmlspecialchars($product->name); ?></h1>
                      <p><?php echo htmlspecialchars($product->description); ?></p>
                      <form action="?action=add-to-cart" method="post">
                          <input type="number" name="product_id" hidden value="<?php echo htmlspecialchars($product->id); ?>">
                          <input type="submit" value="Ajouter">
                      </form>
                  </div>
                  <span class="item-price">Prix: <?php echo htmlspecialchars($product->price); ?></span>
                  <span class="item-category">
                    <a href=""><?php echo htmlspecialchars($category->name); ?></a>
                  </span>
              </li>
  <?php
}
?>
            </ul>
        </section>
        <aside class="cart">
            <h1>Panier de <?php echo htmlspecialchars($currentUser->name); ?></h1>
            <ul>
<?php
foreach (CartProduct::findAllByUser($currentUser) as $cardProduct) {
  $product = $cardProduct->getProduct();
  ?>
                <li>
                    <h3><?php echo htmlspecialchars($product->name); ?></h3>
                    <p><?php echo htmlspecialchars($product->description); ?></p>
                    <span>Prix: <?php echo htmlspecialchars($product->price); ?></span>
                    <form action="?action=remove-from-cart" method="post">
                        <input type="number" name="product_id" hidden value="<?php echo htmlspecialchars($product->id); ?>">
                        <input type="submit" value="Retirer">
                    </form>
                </li>
  <?php
}
?>
            </ul>
        </aside>
    </main>
    <footer>
        <form action="?action=create-product" method="post" enctype="multipart/form-data">
            <input type="text" id="title" name="title" placeholder="Titre du produit">
            <label for="title"></label>

            <textarea rows="5" cols="33" id="description" name="description" placeholder="Description du produit"></textarea>
            <label for="description"></label>

            <input type="number" id="price" name="price" placeholder="0">
            <label for="price"></label>

            <input type="file" id="image" name="image" accept="image/*">
            <label for="image"></label>

            <input type="submit" value="Envoyer">
        </form>
    </footer>
</body>
</html>