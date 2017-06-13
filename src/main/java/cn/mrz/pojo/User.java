package cn.mrz.pojo;

/**
 * Created by Administrator on 2017/4/1.
 */

public class User {

    public static final String[] COLNAMES = {"USERNAME","NICKNAME","EMAIL","ENABLED"};

    private String username;
    private String nickname;
    private String password;
    private String email;
    private int enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
}
