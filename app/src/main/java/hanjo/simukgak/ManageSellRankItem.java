package hanjo.simukgak;

/**
 * Created by Kwon Ohhyun on 2016-12-04.
 */

public class ManageSellRankItem {

    private String product;
    private int price = 0;
    private int num = 0;


    public void setProduct(String string) {product = string;}
    public void setPrice(int n) {price = n;}
    public void setNum(int n) {num = n;}
    public void increaseNum(int n) {num = num + n;}

    public String getProduct() {return product;}
    public int getPrice() {return price;}
    public int getTotPrice() {return price * num;}
    public int getNum() {return num;}
}
