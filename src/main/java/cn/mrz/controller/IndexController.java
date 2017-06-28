package cn.mrz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController  extends BaseController{

    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = {"/", "index", "home"})
    public String goIndex() {
        return "/index";
    }

    @RequestMapping(value = {"/go/{pageName}"})
    public String goPage(@PathVariable String pageName) {
        return "/" + pageName;
    }

}
