package com.fsm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsm.Utils.Path;
import com.fsm.Utils.Utils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseHandler extends AbstractHandler {


    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        String jsonResult = getResultAsJson(request);
        respondWithJson(response, jsonResult);
        baseRequest.setHandled(true);
    }

    void respondWithJson(HttpServletResponse response, String jsonResult) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(
                getContentType("application/json", "utf-8"));
        try {
            response.getWriter().println(jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getResultAsJson(HttpServletRequest request) {
        Object invokeResult = getResult(request);
        ObjectMapper mapper = new ObjectMapper();
        String result = "ERROR";
        try {
            result = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(invokeResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    Object getResult(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        Map<String, String[]> paramMap = request.getParameterMap();

        return paramMap.isEmpty() ?
                getResponse(pathInfo) :
                getResponse(pathInfo, paramMap);
    }

    Object getResponse(String path) {
        return Utils.getInstance()
                .invoke(getPath(path));
    }

    Object getResponse(String path, Map<String, String[]> reqParams) {
        return Utils.getInstance()
                .invoke(getPath(path, reqParams));
    }

    Path getPath(String path) {
        //substring to remove space at the beginning of parts
        String[] parts = path.substring(1).split("/");
        return parts.length > 1 ?
                new Path(parts[0], parts[1]) :
                new Path(parts[0], "index");
    }

    Path getPath(String path, Map<String, String[]> reqParams) {
        Path p = getPath(path);
        Map<String, Object> parameters = getFlattened(reqParams);
        p.fillParameters(parameters);
        return p;
    }

    Map<String, Object> getFlattened(Map<String, String[]> reqParams) {
        return reqParams.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue()[0]));
    }

    String getContentType(String type, String charset) {
        return String.format("%s; charset=%s", type, charset);
    }

}

























