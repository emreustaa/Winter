package com.fsm.Controller;

import com.fsm.Annotation.Controller;
import com.fsm.Annotation.Mapping;
import com.fsm.Enums.TYPE;
import com.fsm.Models.Person;
import com.fsm.Models.PersonRepository;

@Controller(path = "hello")
public class HelloController {

   public HelloController() {

    }

    @Mapping(path = "index", type = TYPE.GET)
    public String index() {
        return "Hello";
    }

    @Mapping(path = "person", type = TYPE.GET)
    public Person getPerson() {
       return PersonRepository.findById(1);
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
