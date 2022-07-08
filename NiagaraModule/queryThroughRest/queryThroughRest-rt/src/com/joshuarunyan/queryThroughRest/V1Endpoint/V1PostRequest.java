package com.joshuarunyan.queryThroughRest.V1Endpoint;

import com.tridium.bql.projection.BProjectionTable;

import javax.baja.query.BQueryResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.baja.collection.TableCursor;
import javax.baja.naming.BOrd;
import javax.baja.sys.BComponent;
import javax.baja.web.WebOp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class V1PostRequest {
    WebOp op;
    String path;

    public V1PostRequest(WebOp opp, String pathp) {
        op = opp;
        path = pathp;
    }

    public void handleRequest() throws IOException {
        switch(path) {
            case "query":
                postQuery();
                break;
            default:
                HttpServletResponse resp = op.getResponse();
                resp.setStatus(404);
        }
    }

    private void postQuery() throws IOException {
        HttpServletRequest req = op.getRequest();
        if (req.getContentType() == "application/json") {
            String queryString = parseQueryFromPostedJson(req);
            BOrd ord = BOrd.make(queryString);

            JSONArray queryResults;
            try {
                BProjectionTable executedQuery = (BProjectionTable) ord.get();
                queryResults = handleBQLQuery(executedQuery);
            } catch (ClassCastException x) {
                BQueryResult executedQuery = (BQueryResult)ord.get(null);
                queryResults = handleNEQLQuery(executedQuery);
            }

            JSONObject data = new JSONObject();
            data.put("results", queryResults);
            data.put("count", queryResults.length());

            op.setContentType("application/json; charset=utf-8");
            op.getWriter().write(data.toString());
        } else {
            HttpServletResponse resp = op.getResponse();
            resp.sendError(406, "Query must be formatted as a JSON request, you sent " + req.getContentType());
        }
    }

    // Helper Functions

    private String parseQueryFromPostedJson(HttpServletRequest req) throws JSONException, IOException {
        BufferedReader reader = req.getReader();
        String body = (String)reader.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject obj = new JSONObject(body);
        return obj.getString("data");
    }

    private JSONArray handleBQLQuery(BProjectionTable executedQuery) {
        TableCursor cur = executedQuery.cursor();
        int cols = executedQuery.getColumns().size();

        JSONArray result = new JSONArray();
        while (cur.next()) {
            JSONObject row = new JSONObject();
            for (int i = 0; i < cols; i++) {
                String columnName = executedQuery.getColumns().get(i).getName();
                String value = cur.cell(executedQuery.getColumns().get(i)).toString();
                row.put(columnName, value);
            }
            result.put(row);
        };

        cur.close();
        return result;
    }

    private JSONArray handleNEQLQuery(BQueryResult executedQuery) {
        TableCursor cur = executedQuery.cursor();
        int cols = executedQuery.getColumns().size();

        JSONArray result = new JSONArray();
        while (cur.next()) {
            JSONObject row = new JSONObject();
            // NEQL queries (regardless of the select statement) return a list of ords to matching objects
            BOrd objOrd = BOrd.make(cur.cell(executedQuery.getColumns().get(0)).toString());
            BComponent obj = objOrd.get().asComponent();
            String name = obj.getName();

            row.put("name", name);
            row.put("out", obj.get("out"));
            result.put(row);
        };

        cur.close();
        return result;
    }


}
