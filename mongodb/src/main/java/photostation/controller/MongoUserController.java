package photostation.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import photostation.core.base.Result;
import photostation.core.constant.CodeConst;
import photostation.model.MongoUser;
import photostation.service.MongoUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
 * Date: 2016/11/15 15:49
 * Copyright(©) 2015 by xiaomo.
 **/

@RestController
@RequestMapping("mongodb")
@Api("mongodb測試")
public class MongoUserController {

    private final MongoUserService service;

    @Autowired
    public MongoUserController(MongoUserService service) {
        this.service = service;
    }

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public Result get(@PathVariable("id") Long id) {
        MongoUser mongoUser = service.findById(id);
        return new Result<>(mongoUser);
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    public Result findAll() {
        return new Result<>(service.findAll());
    }


    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody MongoUser user) {
        return new Result<>(service.add(user));
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public Result delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new Result(CodeConst.SUCCESS.getResultCode(), CodeConst.SUCCESS.getMessage());
    }

    @RequestMapping(value = "{user_id}/upload", method = RequestMethod.POST)
    public Result upload(@RequestBody MultipartFile file, @RequestParam(value = "fileName", required = false, defaultValue = "") String fileName, @PathVariable String user_id) {
        return new Result<>(service.upload(file, fileName, user_id));
    }

    @RequestMapping(value = "img/{fileName}", method = RequestMethod.GET)
    public byte[] get(@PathVariable("fileName") String fileName, @RequestParam(value = "w", required = false, defaultValue = "375") int w) throws IOException {
        return service.fileImage(fileName, w);
    }

}
