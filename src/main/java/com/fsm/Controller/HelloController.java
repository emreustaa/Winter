package com.fsm.Controller;

import com.fsm.Annotation.Controller;
import com.fsm.Annotation.Mapping;
import com.fsm.Annotation.QueryParam;
import com.fsm.Enums.TYPE;
import com.fsm.Models.Person;
import com.fsm.Models.PersonRepo;

@Controller(path = "hello")
public class HelloController {

    public HelloController() {

    }

    @Mapping(type = TYPE.GET)
    public String hello() {
        return "Hello";
    }

    @Mapping(path = "person", type = TYPE.GET)
    public Person getPerson(@QueryParam(name = "id") int id) {
        return PersonRepo.findById(id);
    }

    @Mapping(path = "person", type = TYPE.GET)
    public int getPerson(@QueryParam(name = "id") int id,
                         @QueryParam(name = "name") int name) {
        return id;
    }

    @Mapping(path = "person", type = TYPE.GET)
    public Person getPerson(@QueryParam(name = "id") int id,
                            @QueryParam(name = "name") String name) {
        return PersonRepo.findByIdAndName(id, name);
    }

}
