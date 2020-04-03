package com.fsm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsm.Utils.InvokeParams;
import com.fsm.Utils.Utils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class HelloWorld extends AbstractHandler {


    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {


        Object invokeResult = getResponse(target);
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(invokeResult);

        response.setContentType(
                getContentType("text/html", "utf-8"));

        response.setStatus(HttpServletResponse.SC_OK);

        String responseText = String.format("<h1>%s</h1>", result);
        //String responseText = getResponse(target).getResult();
        response.getWriter().println(responseText);

        baseRequest.setHandled(true);

    }

    Object getResponse(String target) {
        //if (!hasQueryParam(target)) {
        return invokeRelatedMapping(target, new InvokeParams());
        // }
    }

    Object invokeRelatedMapping(String target, InvokeParams params) {
        try {
            return Utils.getInstance()
                    .invoke2();
        } catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
//           return Utils.getInstance()
//                    .invokeMethod(
//                            new Path(target,
//                                    target), params);
    }

    boolean hasQueryParam(String target) {
        return target.contains("?");
    }

    String getContentType(String type, String charset) {
        return String.format("%s; charset=%s", type, charset);
    }

}
