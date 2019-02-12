package photostation.mongodb.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import photostation.core.base.Result;
import photostation.mongodb.service.MongoUserService;

import java.io.IOException;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 *
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email: xiaomo@xiaomo.info
 * <p>
 * Date: 2016/11/16 10:19
 * Copyright(©) 2015 by xiaomo.
 **/

@Controller
@RequestMapping("/gui")
public class ThymeleafController {

    @Autowired
    MongoUserService mongoUserService;

    @RequestMapping("/")
    public String hello(ModelMap map) throws IOException {
        map.put("hello", "使用thymeleaf!");
        map.put("photos", this.get("origin_20190123132509.jpg",375));
        return "index";
    }

    @RequestMapping("/attach")
    public String attach(ModelMap map) {
        map.put("attach", "...");
        return "attach";
    }

    @RequestMapping(value = "upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "fileName", required = false, defaultValue = "") String fileName) {
        return new Result<>(mongoUserService.upload(file, fileName));
    }

    @RequestMapping(value = "img/{fileName}")
    public byte[] get(@PathVariable("fileName") String fileName, @RequestParam(value = "w", required = false, defaultValue = "375") int w) throws IOException {
        return mongoUserService.fileImage(fileName, w);
    }

}
