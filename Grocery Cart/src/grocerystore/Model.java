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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
public class Model {
    private ObservableList<Item> itemsObservableList;
    public Model() {
        itemsObservableList = FXCollections.observableArrayList();
    }
    public ObservableList<Item> getItemsObservableList() {
        return itemsObservableList;
    }
    public void loadData(InputStream csvInputStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvInputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 0) {
                    String name = values[0].trim();
                    double unitPrice = 10.0;
                    if (values.length > 1) {
                        try {
                            unitPrice = Double.parseDouble(values[1].trim());
                        } catch (NumberFormatException e) {}
                    }
                    itemsObservableList.add(new Item(name, unitPrice));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}