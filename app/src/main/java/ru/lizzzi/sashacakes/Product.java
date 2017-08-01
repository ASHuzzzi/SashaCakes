package ru.lizzzi.sashacakes;

/**
 * Created by Liza on 30.01.2017.
 */

public class Product {

    String name;
    String price;
    int image;
    int quantity;

    Product(String _describe, String _price, int _image, int _quantity) {
        name = _describe;
        price = _price;
        image = _image;
        quantity = _quantity;
    }
}
