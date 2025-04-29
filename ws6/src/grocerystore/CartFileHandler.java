/**********************************************
Workshop #06
Course: APD545 - Winter
Last Name: BEHZADFAR
First Name: RADMEHR
ID: 148786221
Section:NDD
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: RadmehrBehzadfar
Date:2025-03-29
**********************************************/
package grocerystore;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class CartFileHandler {
    private static final String CARTS_DIR = "saved_carts";

    public static void saveCart(List<ItemInCart> cart) {
        try {
            File dir = new File(CARTS_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String filename = CARTS_DIR + File.separator + System.currentTimeMillis() + ".ser";
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(cart);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<ItemInCart> loadCart(File file) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            List<ItemInCart> loaded = (List<ItemInCart>) ois.readObject();
            ois.close();
            return loaded;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<File> listSavedCarts() {
        List<File> result = new ArrayList<>();
        File dir = new File(CARTS_DIR);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".ser"));
            if (files != null) {
                for (File f : files) {
                    result.add(f);
                }
            }
        }
        return result;
    }
}