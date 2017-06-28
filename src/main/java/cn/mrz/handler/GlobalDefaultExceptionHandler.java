package cn.mrz.handler;

import cn.mrz.view.JsonView;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(value = RuntimeException.class)
    public ModelAndView commErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // 如果异常使用了ResponseStatus注解，那么重新抛出该异常，Spring框架会处理该异常。
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        String ajaxHeader = req.getHeader("X-Requested-With");
        ModelAndView mv = new ModelAndView();
        // 否则创建ModleAndView，处理该异常。
        if(ajaxHeader==null) {
            mv.addObject("message", e.getMessage());
            mv.addObject("url", req.getRequestURL());
            mv.addObject("ex", e.getLocalizedMessage());
            mv.setViewName(DEFAULT_ERROR_VIEW);
            return mv;
        }else{
            Map map = new HashMap();
            map.put("success",false);
            map.put("message",e.getMessage());
            mv.addObject("data", map);
            mv.setView(new JsonView());
            return mv;
        }
    }
}
