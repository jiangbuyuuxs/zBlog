package cn.mrz.pojo;

/**
 * Created by Administrator on 2017/8/18.
 */
public class CommentUpDown {
    private Long id;
    private Long userId;
    private Long direction;
    private Long cId;
    private Long flag;

    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
    }

    public CommentUpDown(Long cId, Long userId, Long direction) {
        this.cId = cId;
        this.userId = userId;
        this.direction = direction;
    }

    public CommentUpDown() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDirection() {
        return direction;
    }

    public void setDirection(Long direction) {
        this.direction = direction;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }
}
