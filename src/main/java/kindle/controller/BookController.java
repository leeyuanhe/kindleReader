package kindle.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/book")
public class BookController extends BaseController{


    /**
     * @description 我的分享
     * @author hely
     * @date 2017-09-15
     * @param
     */
    @RequestMapping(value = "/sharebook",method = RequestMethod.POST)
    public ModelAndView shareBook(HttpServletRequest request){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/admin3/table_basic");
        return mv;
    }
}
