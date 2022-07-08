package com.joshuarunyan.queryThroughRest.V1Endpoint;

import org.json.JSONObject;

import javax.baja.web.WebOp;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class V1GetRequest {
    WebOp op;
    String path;

    public V1GetRequest(WebOp opp, String pathp) {
        op = opp;
        path = pathp;
    }

    public void handleRequest() throws IOException {
        switch(path) {
            case "context":
                getTree();
                break;
            default:
                HttpServletResponse resp = op.getResponse();
                resp.setStatus(404);
        }
    }

    private void getTree() throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("data", "empty");
        op.setContentType("application/json; charset=utf-8");
        op.getWriter().write(obj.toString());
    }
}
