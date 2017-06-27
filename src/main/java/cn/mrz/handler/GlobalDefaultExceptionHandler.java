package cn.mrz.handler;

import cn.mrz.exception.NoSuchBlogException;
import cn.mrz.exception.NoSuchWordException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/6/14.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    final String DEFAULT_ERROR_VIEW = "error";
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ModelAndView pathVariableTypeConvertErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // 如果异常使用了ResponseStatus注解，那么重新抛出该异常，Spring框架会处理该异常。
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        // 否则创建ModleAndView，处理该异常。
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "非法参数~~~");
        mv.addObject("url", req.getRequestURL());
        mv.addObject("ex", e.getLocalizedMessage());
        mv.setViewName(DEFAULT_ERROR_VIEW);
        return mv;
    }
    @ExceptionHandler(value = NoSuchBlogException.class)
    public ModelAndView noSuchBlogErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // 如果异常使用了ResponseStatus注解，那么重新抛出该异常，Spring框架会处理该异常。
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        // 否则创建ModleAndView，处理该异常。
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "找不到这样的博客~~~");
        mv.addObject("url", req.getRequestURL());
        mv.addObject("ex",e.getLocalizedMessage());
        mv.setViewName(DEFAULT_ERROR_VIEW);
        return mv;
    }
    @ExceptionHandler(value = NoSuchWordException.class)
    public ModelAndView noSuchWordErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // 如果异常使用了ResponseStatus注解，那么重新抛出该异常，Spring框架会处理该异常。
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        // 否则创建ModleAndView，处理该异常。
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "找不到这样的热词~~~");
        mv.addObject("url", req.getRequestURL());
        mv.addObject("ex",e.getLocalizedMessage());
        mv.setViewName(DEFAULT_ERROR_VIEW);
        return mv;
    }
    @ExceptionHandler(value = RuntimeException.class)
    public ModelAndView commErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // 如果异常使用了ResponseStatus注解，那么重新抛出该异常，Spring框架会处理该异常。
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        // 否则创建ModleAndView，处理该异常。
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", e.getMessage());
        mv.addObject("url", req.getRequestURL());
        mv.addObject("ex",e.getLocalizedMessage());
        mv.setViewName(DEFAULT_ERROR_VIEW);
        return mv;
    }
}
