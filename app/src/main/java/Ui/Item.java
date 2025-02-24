package Ui;

public class Item {

    private String name;
    private int amount;
    private int image;
    private String desc;
    private double price;

    public Item(String name, int amount, int image, int id_, String desc, double price) {
        this.name = name;;
        this.image = image;
        this.amount = amount;
        this.desc   = desc;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
