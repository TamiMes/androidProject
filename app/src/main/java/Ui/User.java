package Ui;


public class User {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String adress;
    private String cardNumber;
    private String cvv;
    private String id;

    public User(String name,String email, String password, String phone){
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public User() {
    }

    public User(String name, String phone,String cardNumber, String cardCVV,String id, String adress) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.cvv = cardCVV;
        this.id = id;
        this.adress = adress;
        this.phone = phone;

    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
