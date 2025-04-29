/**********************************************
Workshop #04
Course: APD545 - Winter
Last Name: BEHZADFAR
First Name: RADMEHR
ID: 148786221
Section:NDD
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: RadmehrBehzadfar
Date:2025-03-02
**********************************************/
package grocerystore;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemInCart {
    private SimpleStringProperty itemName;
    private SimpleDoubleProperty unitPrice;
    private SimpleIntegerProperty quantity;
    private SimpleDoubleProperty totalPrice;

    public ItemInCart(String itemName, double unitPrice, int quantity) {
        this.itemName = new SimpleStringProperty(itemName);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(unitPrice * quantity);
    }

    public String getItemName() {
        return itemName.get();
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
        updateTotalPrice();
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
        updateTotalPrice();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        updateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    private void updateTotalPrice() {
        this.totalPrice.set(getUnitPrice() * getQuantity());
    }

    public SimpleStringProperty itemNameProperty() {
        return itemName;
    }

    public SimpleDoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public SimpleDoubleProperty totalPriceProperty() {
        return totalPrice;
    }
}