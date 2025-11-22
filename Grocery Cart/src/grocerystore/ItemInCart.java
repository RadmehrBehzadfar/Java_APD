/**********************************************
Workshop #05
Course: APD545 - Winter
Last Name: BEHZADFAR
First Name: RADMEHR
ID: 148786221
Section:NDD
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: RadmehrBehzadfar
Date:2025-03-16
**********************************************/
package grocerystore;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;
public class ItemInCart implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient SimpleStringProperty itemName;
    private transient SimpleDoubleProperty unitPrice;
    private transient SimpleIntegerProperty quantity;
    private transient SimpleDoubleProperty totalPrice;
    private String itemNameCache;
    private double unitPriceCache;
    private int quantityCache;
    private double totalPriceCache;
    public ItemInCart(String itemName, double unitPrice, int quantity) {
        this.itemName = new SimpleStringProperty(itemName);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(unitPrice * quantity);
        this.itemNameCache = itemName;
        this.unitPriceCache = unitPrice;
        this.quantityCache = quantity;
        this.totalPriceCache = unitPrice * quantity;
    }
    public String getItemName() {
        return itemName.get();
    }
    public void setItemName(String itemName) {
        this.itemName.set(itemName);
        this.itemNameCache = itemName;
        updateTotalPrice();
    }
    public double getUnitPrice() {
        return unitPrice.get();
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
        this.unitPriceCache = unitPrice;
        updateTotalPrice();
    }
    public int getQuantity() {
        return quantity.get();
    }
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        this.quantityCache = quantity;
        updateTotalPrice();
    }
    public double getTotalPrice() {
        return totalPrice.get();
    }
    private void updateTotalPrice() {
        totalPrice.set(getUnitPrice() * getQuantity());
        totalPriceCache = getUnitPrice() * getQuantity();
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
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        updateTotalPrice();
        out.defaultWriteObject();
    }
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        itemName = new SimpleStringProperty(itemNameCache);
        unitPrice = new SimpleDoubleProperty(unitPriceCache);
        quantity = new SimpleIntegerProperty(quantityCache);
        totalPrice = new SimpleDoubleProperty(totalPriceCache);
    }
}