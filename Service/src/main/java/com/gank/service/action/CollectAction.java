package com.gank.service.action;

import com.gank.service.bean.CollectBean;
import com.gank.service.bean.RestFulBean;
import com.gank.service.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author shijunxing
 */
@Controller
@RequestMapping("/collect")
public class CollectAction {

    @Autowired
    private CollectService collectService;

    /**
     * 收藏
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public RestFulBean<Boolean> collect(String uid, String json)  throws Exception {
        return collectService.collect(uid,json);
    }
    /**
     * 取消收藏
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unCollect", method = RequestMethod.POST)
    public RestFulBean<Boolean> unCollect(String uid, String id)  throws Exception {
        return collectService.unCollect(uid,id);
    }

    /**
     * 判断是否收藏
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isCollect", method = RequestMethod.POST)
    public RestFulBean<Boolean> isCollect(String uid, String id)  throws Exception {
        return collectService.isCollect(uid,id);
    }

    /**
     * 获取收藏
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCollects", method = RequestMethod.POST)
    public RestFulBean<List<CollectBean>> getCollects(String uid, int page , int pageSize)  throws Exception {
        return collectService.getCollects(uid,page , pageSize);
    }

}
