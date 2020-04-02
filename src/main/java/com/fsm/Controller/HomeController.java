package com.fsm.Controller;

import com.fsm.Enums.TYPE;
import com.fsm.Annotation.*;

@Controller(path="/hello")
public class HomeController {

    @Mapping(type = TYPE.GET)
    public String hello() {
        return "Hello";
    }
    public String hello(String message){return "hello " + message;}

}
