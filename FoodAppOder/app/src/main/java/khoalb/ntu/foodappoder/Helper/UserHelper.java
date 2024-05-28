package khoalb.ntu.foodappoder.Helper;

public class UserHelper {
    String name, email, username, password;

    public UserHelper(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public UserHelper() {
    }

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
    public static String getCurrentUsername() {
        // Trả về tên người dùng hiện tại, có thể lấy từ SharedPreferences hoặc từ một nguồn khác
        return "username"; // Thay thế "username" bằng tên người dùng thực tế
    }
}
