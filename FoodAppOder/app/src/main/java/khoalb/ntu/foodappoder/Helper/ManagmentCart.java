package khoalb.ntu.foodappoder.Helper;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import khoalb.ntu.foodappoder.Domain.Foods;

/**
 * Lớp trợ giúp để quản lý chức năng giỏ hàng, bao gồm thêm, cập nhật và lấy các mặt hàng trong giỏ hàng.
 */
public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;
    private ArrayList<Foods> listCart;


    /**
     * Constructor để khởi tạo đối tượng ManagementCart với context được cung cấp.
     * @param context Context của activity hoặc fragment gọi đến.
     */
    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB=new TinyDB(context);
        this.listCart = new ArrayList<>();
    }

    /**
     * Phương thức để thêm một mặt hàng vào giỏ hàng.
     * @param item Mặt hàng thêm vào giỏ hàng.
     */
    public void insertFood(Foods item) {
        ArrayList<Foods> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;
        // Kiểm tra nếu mặt hàng đã tồn tại trong giỏ hàng
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        // Nếu mặt hàng đã tồn tại, cập nhật số lượng của nó, ngược lại thêm vào giỏ hàng
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        }else{
            listpop.add(item);
        }
        // Lưu danh sách giỏ hàng đã cập nhật vào TinyDB
        tinyDB.putListObject("CartList",listpop);
        // Hiển thị thông báo Toast thông báo mặt hàng đã được thêm vào giỏ hàng
        Toast.makeText(context, "Đã thêm vào giỏ hàng của bạn", Toast.LENGTH_SHORT).show();
    }

    /**
     * Phương thức để lấy danh sách các mặt hàng trong giỏ hàng.
     * @return ArrayList chứa các mặt hàng trong giỏ hàng.
     */
    public ArrayList<Foods> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    /**
     * Phương thức để tính tổng giá của tất cả các mặt hàng trong giỏ hàng.
     * @return Tổng giá của tất cả các mặt hàng trong giỏ hàng.
     */
    public Double getTotalFee(){
        ArrayList<Foods> listItem=getListCart();
        double fee=0;
        // Tính tổng giá bằng cách cộng tổng giá của từng mặt hàng nhân với số lượng của nó
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }

    /**
     * Phương thức để giảm số lượng của một mặt hàng cụ thể trong giỏ hàng.
     * @param listItem Danh sách các mặt hàng trong giỏ hàng.
     * @param position Vị trí của mặt hàng trong giỏ hàng để giảm số lượng của nó.
     * @param changeNumberItemsListener Lắng nghe thông báo khi số lượng mặt hàng trong giỏ hàng thay đổi.
     */
    public void minusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        // Nếu số lượng của mặt hàng là 1, loại bỏ nó khỏi giỏ hàng, ngược lại giảm số lượng của nó
        if(listItem.get(position).getNumberInCart()==1){
            listItem.remove(position);
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        // Lưu danh sách giỏ hàng đã cập nhật vào TinyDB
        tinyDB.putListObject("CartList",listItem);
        // Thông báo cho lắng nghe biết số lượng mặt hàng trong giỏ hàng đã thay đổi
        changeNumberItemsListener.change();
    }

    /**
     * Phương thức để tăng số lượng của một mặt hàng cụ thể trong giỏ hàng.
     * @param listItem Danh sách các mặt hàng trong giỏ hàng.
     * @param position Vị trí của mặt hàng trong giỏ hàng để tăng số lượng của nó.
     * @param changeNumberItemsListener Lắng nghe thông báo khi số lượng mặt hàng trong giỏ hàng thay đổi.
     */
    public  void plusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        // Tăng số lượng của mặt hàng
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        // Lưu danh sách giỏ hàng đã cập nhật vào TinyDB
        tinyDB.putListObject("CartList",listItem);
        // Thông báo cho lắng nghe biết số lượng mặt hàng trong giỏ hàng đã thay đổi
        changeNumberItemsListener.change();
    }

    public void clearCart() {
        // Xóa toàn bộ mục trong danh sách giỏ hàng
        listCart.clear();
        // Sau đó, lưu danh sách giỏ hàng đã cập nhật vào TinyDB
        tinyDB.putListObject("CartList", listCart);
    }
}
