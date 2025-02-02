package Ui;

public class Item {

    private String name;
    private int amount;
    private int image;
    public Item(String name, int amount, int image, int id_) {
        this.name = name;;
        this.image = image;
        this.amount = amount;
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
}
