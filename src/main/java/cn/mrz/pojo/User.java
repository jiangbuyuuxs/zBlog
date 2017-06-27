package cn.mrz.pojo;

import com.baomidou.mybatisplus.annotations.TableName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/1.
 */
@TableName("users")
public class User implements Serializable{

    public static final String[] COLNAMES = {"USERNAME","NICKNAME","EMAIL","ENABLED"};

    private Long id;
    @NotEmpty
    private String username;
    private String nickname;
    @NotEmpty
    private String password;
    @NotEmpty
    @Email
    private String email;
    private int enabled;

    public User() {
    }

    public User(Long id,String username, String nickname, String password, String email, int enabled) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
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
