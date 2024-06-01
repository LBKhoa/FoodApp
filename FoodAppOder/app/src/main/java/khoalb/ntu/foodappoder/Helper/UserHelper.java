package khoalb.ntu.foodappoder.Helper;

public class UserHelper {
    private String name;
    private String email;
    private String username;
    private String password;
    private CartHelper cart;

    public UserHelper(String name, String email, String username, String password, CartHelper cart) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.cart = cart;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CartHelper getCart() {
        return cart;
    }

    public void setCart(CartHelper cart) {
        this.cart = cart;
    }

    public static String getCurrentUsername() {
        // Trả về tên người dùng hiện tại, có thể lấy từ SharedPreferences hoặc từ một nguồn khác
        return "username"; // Thay thế "username" bằng tên người dùng thực tế
    }
}
