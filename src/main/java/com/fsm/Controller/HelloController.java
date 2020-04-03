package com.fsm.Controller;

import com.fsm.Annotation.Controller;
import com.fsm.Annotation.Mapping;
import com.fsm.Enums.TYPE;

@Controller(path = "/hello")
public class HelloController {

   public HelloController() {

    }

    @Mapping(type = TYPE.GET)
    public String hello() {
        return "Hello";
    }
    //public String hello(String message){return "hello " + message;}

//    public static void main(String[] args) {
//        for (Method m : HelloController.class.getMethods()) {
//            System.out.println("method name: " +m.getName() + "\n");
//            System.out.println("Parameter Types: ");
//            printParamTypes(m.getParameterTypes());
//            System.out.println("");
//            System.out.println("returnType: " +m.getReturnType().getSimpleName() + "\n");
//        }
//    }
//
//    static void printParamTypes(Class<?>[] types) {
//        for (Class<?> cls : types) {
//            System.out.println(cls.getSimpleName());
//            System.out.println(cls.getTypeName());
//        }
//    }

}
