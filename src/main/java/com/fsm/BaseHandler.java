package com.fsm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsm.Utils.Path;
import com.fsm.Utils.Utils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BaseHandler extends AbstractHandler {


    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        Object invokeResult = getResult(request);

        ObjectMapper mapper = new ObjectMapper();

        String res = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(invokeResult);

        //String result = mapper.writeValueAsString(invokeResult);

        response.setContentType(
                getContentType("text/html", "utf-8"));

        response.setStatus(HttpServletResponse.SC_OK);

        String responseText = String.format("<h1>%s</h1>", res);
        response.getWriter().println(responseText);
        baseRequest.setHandled(true);

        //request.getPathInfo();
        //request.getParameterMap()
        //        .forEach((key, val) -> System.out.println(key + ":" + val[0]));
    }

    Object getResult(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        Map<String, String[]> paramMap = request.getParameterMap();

        return paramMap.isEmpty() ?
                getResponse(pathInfo) :
                getResponse(pathInfo, paramMap);
    }

    Path getPath(String path) {
        String[] parts = path.split("/");
        return parts.length > 2 ?
                new Path(parts[1], parts[2]) :
                new Path(parts[1], "index");
    }

    Object getResponse(String path) {
        try {
            return Utils.getInstance()
                    .invoke2(getPath(path));
        } catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    Object getResponse(String path, Map<String, String[]> reqParams) {
        return new Object();
    }

    String getContentType(String type, String charset) {
        return String.format("%s; charset=%s", type, charset);
    }

}
