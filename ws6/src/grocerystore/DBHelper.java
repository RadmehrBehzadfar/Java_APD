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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:shopcart.db";
    static {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String createUsers = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)";
            String createCarts = "CREATE TABLE IF NOT EXISTS carts (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, cart_data BLOB, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(createUsers);
            stmt.execute(createCarts);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }
    public static boolean registerUser(String username, String password) {
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users(username, password) VALUES(?,?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch(Exception e){
            return false;
        }
    }
    public static User loginUser(String username, String password) {
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT id, username FROM users WHERE username=? AND password=?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return new User(rs.getInt("id"), rs.getString("username"));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static boolean saveCart(List<ItemInCart> cart, int userId) {
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO carts(user_id, cart_data) VALUES(?,?)")) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(cart);
            oos.close();
            byte[] cartBytes = baos.toByteArray();
            pstmt.setInt(1, userId);
            pstmt.setBytes(2, cartBytes);
            pstmt.executeUpdate();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @SuppressWarnings("unchecked")
    public static List<ItemInCart> loadCart(int userId) {
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT cart_data FROM carts WHERE user_id=? ORDER BY timestamp DESC LIMIT 1")) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                byte[] cartBytes = rs.getBytes("cart_data");
                ByteArrayInputStream bais = new ByteArrayInputStream(cartBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                List<ItemInCart> cart = (List<ItemInCart>) ois.readObject();
                ois.close();
                return cart;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}