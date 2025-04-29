package pizzaapp.model;

public class Order {
    private Customer customer;
    private String pizzaType;
    private String pizzaSize;
    private int quantity;
    
    private static final double TAX_RATE = 0.13;

    public Order(Customer customer, String pizzaType, String pizzaSize, int quantity) {
        this.customer = customer;
        this.pizzaType = pizzaType;
        this.pizzaSize = pizzaSize;
        this.quantity = quantity;
    }

    public double calculateSubtotal() {
        double basePrice = 0.0;
        switch (pizzaType) {
            case "Cheese":
                if ("Small".equals(pizzaSize)) basePrice = 8.00;
                else if ("Medium".equals(pizzaSize)) basePrice = 10.00;
                else basePrice = 12.00; // Large
                break;
            case "Vegetarian":
                if ("Small".equals(pizzaSize)) basePrice = 9.00;
                else if ("Medium".equals(pizzaSize)) basePrice = 11.00;
                else basePrice = 13.00; // Large
                break;
            case "Meat Lover":
                if ("Small".equals(pizzaSize)) basePrice = 10.00;
                else if ("Medium".equals(pizzaSize)) basePrice = 13.00;
                else basePrice = 15.00; // Large
                break;
            default:
                basePrice = 8.00;
                break;
        }
        return basePrice * quantity;
    }

    public double calculateTotal() {
        double subtotal = calculateSubtotal();
        return subtotal + (subtotal * TAX_RATE);
    }

    public Customer getCustomer() { 
        return customer; 
    }
    public void setCustomer(Customer customer) { 
        this.customer = customer; 
    }
    public String getPizzaType() { 
        return pizzaType; 
    }
    public void setPizzaType(String pizzaType) { 
        this.pizzaType = pizzaType; 
    }
    public String getPizzaSize() { 
        return pizzaSize; 
    }
    public void setPizzaSize(String pizzaSize) { 
        this.pizzaSize = pizzaSize; 
    }
    public int getQuantity() { 
        return quantity; 
    }
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }
}