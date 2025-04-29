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
import javafx.beans.property.SimpleStringProperty;

public class Item {
    private SimpleStringProperty name;
    private SimpleDoubleProperty unitPrice;

    public Item(String name, double unitPrice) {
        this.name = new SimpleStringProperty(name);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return getName();
    }
}