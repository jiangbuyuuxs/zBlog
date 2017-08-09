package cn.mrz.service;

/**
 * Created by Administrator on 2017/8/5.
 */
public interface VisitService {
    /**
     * 访问博文时
     * @param blogId
     */
    void visitPage(Long blogId);

    /**
     * 当添加一条新评论时
     * @param blogId
     */
    void addNewComment(Long blogId);

    /**
     * 当添加一条评论回复
     * @param blogId
     */
    void addReComment(Long blogId);

}
