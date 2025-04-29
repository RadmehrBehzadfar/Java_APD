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
import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient SimpleStringProperty name;
    private transient SimpleDoubleProperty unitPrice;
    private String nameCache;
    private double priceCache;
    public Item(String name, double unitPrice) {
        this.name = new SimpleStringProperty(name);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.nameCache = name;
        this.priceCache = unitPrice;
    }
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
        this.nameCache = name;
    }
    public double getUnitPrice() {
        return unitPrice.get();
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
        this.priceCache = unitPrice;
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public SimpleDoubleProperty unitPriceProperty() {
        return unitPrice;
    }
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
    }
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        name = new SimpleStringProperty(nameCache);
        unitPrice = new SimpleDoubleProperty(priceCache);
    }
    @Override
    public String toString() {
        return getName();
    }
}