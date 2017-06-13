package cn.mrz.service;

/**
 * Created by Administrator on 2017/6/6.
 */
public interface BaseService<T,PK> {
    void update(T t);
    T getById(PK id);
    boolean delete(PK id);
}
