package com.fsm.Utils;

import java.util.Collections;
import java.util.Map;

public class Path {

    String controller, action;
    Map<String, Object> parameters = Collections.EMPTY_MAP;

    public Path(String controller, String action) {
        this.controller = controller;
        this.action = action;
    }

    public void fillParameters(Map<String, Object> params) {
        this.parameters = params;
    }

}
