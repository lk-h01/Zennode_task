import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Product {
    private int id;
    private String name;
    private double price;
    Product(int id, String name, double price){
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public double getPrice(){
        return this.price;
    }
    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
}

class Item extends Product {
    Product pid;
    private int quantity;
    private double price;
    private boolean giftWrap;
    Item(Product pid,int quantity,boolean isGift){
        super(pid.getId(),pid.getName(),pid.getPrice());
        this.quantity = quantity;
        this.price = this.quantity*pid.getPrice();
        this.giftWrap = isGift;
    }
    public int getItemQuantity(){
        return this.quantity;
    }
    public double getItemPrice(){
        return this.price;
    }
    public boolean isGift(){
        return this.giftWrap;
    }
}

class Cart {
    private List<Item> itemList;
    Cart(){
        itemList = new ArrayList<>();
    }
    public void addToCart(Item i){
        itemList.add(i);
    }
    public void displayCart(){
        System.out.println("-------------------------------------");
        for(Item i:itemList){
            System.out.println("Name: "+i.getName()+"\nQuantity: "+i.getItemQuantity()+"\nPrice: $"+i.getItemPrice()+"\nGift: "+(i.isGift()?"yes":"no"));
            System.out.println("-------------------------------------");
        }
    }
    public double getTotalCartPrice(){
        double totalCartPrice = 0.0d;
        for(Item item:this.itemList){
            totalCartPrice+= item.getItemPrice();
        }
        return totalCartPrice;
    }
    public int getTotalQuantity(){
        int totalQuantity = 0;
        for(Item item:this.itemList){
            totalQuantity+= item.getItemQuantity();
        }
        return totalQuantity;
    }
    public double getTotalPrice(){

        double cartPrice = getTotalCartPrice();
        int cartQuantity = getTotalQuantity();
        double finalAmout = cartPrice;
        System.out.println("Total Price: "+cartPrice);
        Coupon c = new Coupon();
        System.out.println("Discount that can be added: ");
        c.getCoupon(this.itemList,cartPrice,cartQuantity);
        System.out.println("Adding the discount "+c.name+" which gives a discount of "+c.discountValue);
        finalAmout-=c.discountValue;
        double giftWarpAmt=0;
        for(Item item:this.itemList){
            if(item.isGift())
                giftWarpAmt+= 1*item.getItemQuantity();
        }
        System.out.println("Gift wrap amount: $"+giftWarpAmt);
        finalAmout+=giftWarpAmt;

        System.out.println("Total Quantity: "+cartQuantity);
        int noOfPackage = cartQuantity%10==0?cartQuantity/10:cartQuantity/10+1;
        System.out.println("Packages required($5 for 1 package): "+noOfPackage);
        System.out.println("Shipping amount: $"+noOfPackage*5);
        finalAmout+=noOfPackage*5;
        return finalAmout;
    }
}

class Coupon {
    String name;
    double discountValue;
    public double getCoupon(List<Item> items,double totalCartPrice,int totalQuantity){
        Map<String, Double> couponToDiscount = new HashMap<>();
        double discount = 0;
        if(totalQuantity>30){
            for(Item item:items){
                if(item.getItemQuantity()>15){
                    int noOfQtyForDis = item.getItemQuantity() - 15;
                    double price = noOfQtyForDis*item.getPrice();
                    discount+= price*0.5;
                }
            }
            System.out.println("By applying the Coupon 'tiered_50_discount' you get a discount of "+discount);
        }
        couponToDiscount.put("tiered_50_discount",discount);

        discount=0;
        if(totalQuantity>20){
            discount = totalCartPrice*(0.1);
            System.out.println("By applying the Coupon 'bulk_10_discount' you get a discount of "+discount);
        }
        couponToDiscount.put("bulk_10_discount",discount);

        discount=0;
        if(totalCartPrice>200){
            discount = 10;
            System.out.println("By applying the Coupon 'flat_10_discount' you get a discount of "+discount);
        }
        couponToDiscount.put("flat_10_discount",discount);

        discount=0;
        for(Item item:items){
            if(item.getItemQuantity()>10){
                discount+= item.getItemPrice()*0.05;
                System.out.println("By applying the Coupon 'bulk_5_discount' you get a discount of "+discount);
            }
        }
        couponToDiscount.put("bulk_5_discount",discount);

        for(Map.Entry<String,Double> m:couponToDiscount.entrySet()){
            if(m.getValue()>this.discountValue){
                this.name = m.getKey();
                this.discountValue = m.getValue();
            }
        }
        return this.discountValue;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        Product product1 = new Product(1,"ProductA",20);
        Product product2 = new Product(2,"ProductB",40);
        Product product3 = new Product(3,"ProductC",50);
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        System.out.println("The available products are as follows:");
        for(Product p: productList)
            System.out.println(p.getName()+": $"+p.getPrice());

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Cart shoppingCart = new Cart();
        System.out.println("Enter the quantity for each item present below(enter 0 if you don't want to purchase the item) and do you want a gift wrap for that item?");
        for(Product p: productList){
            System.out.println(p.getName());
            System.out.println("Enter quantity? ");
            int quantity = Integer.parseInt(br.readLine());
            System.out.println("Gift wrap(costs $1 per Unit)? (y/n): ");
            String ch = br.readLine();
            boolean giftWrap;
            if(ch.equalsIgnoreCase("y")||ch.equalsIgnoreCase("yes"))
                giftWrap=true;
            else
                giftWrap=false;
            shoppingCart.addToCart(new Item(p,quantity,giftWrap));
        }

        System.out.println("Things available in the cart:");
        shoppingCart.displayCart();
        double finalPrice = shoppingCart.getTotalPrice();

        System.out.println("********************");
        System.out.println("Final Amount: $"+finalPrice);
    }
}