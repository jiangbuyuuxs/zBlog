package cn.mrz.service.impl;

import cn.mrz.service.VisitService;

/**
 * Created by Administrator on 2017/8/5.
 */
public class VisitServiceImpl implements VisitService {
    @Override
    public void visitPage(Long blogId) {
        //TODO 访问数 +1
    }

    @Override
    public void addNewComment(Long blogId) {
        //TODO 新评论 +10
    }

    @Override
    public void addReComment(Long blogId) {
        //TODO 评论回复 +2
    }
}
