package cn.mrz.view;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/28.
 */
public class JsonView extends AbstractView {
    public static final String CONTENT_TYPE = "application/json";
    public static final String CHAR_ENCODING = "UTF-8";

    /**
     * 获取model中的data,并将其直接转换为json格式.
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object data = model.get("data");

        response.setCharacterEncoding(CHAR_ENCODING);
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSONString(data));
    }
}
