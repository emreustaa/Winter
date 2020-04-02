package com.fsm;

import com.fsm.Utlis.InvokeParams;
import com.fsm.Utlis.InvokeResult;
import com.fsm.Utlis.Utils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloWorld extends AbstractHandler {


    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException,
            ServletException {

        //GET RESPONSE

        InvokeResult result = getResponse(target.substring(1));

        response.setContentType(
                getContentType("text/html", "utf-8"));

        response.setStatus(HttpServletResponse.SC_OK);

        String responseText = "<h1>" + result.getResultType().cast(result.getResult()) + "</h1>";

        response.getWriter().println(responseText);

        baseRequest.setHandled(true);

    }

    InvokeResult getResponse(String target) {
        //if (!hasQueryParam(target)) {
          return invokeRelatedMapping(target, new InvokeParams());
       // }
    }

    InvokeResult invokeRelatedMapping(String targetMethod, InvokeParams params) {
           return Utils.getInstance()
                    .invokeMethod(targetMethod, params);
    }

    boolean hasQueryParam(String target) {
        return target.contains("?");
    }

    String getContentType(String type, String charset) {
        return String.format("%s; charset=%s", type, charset);
    }

}
