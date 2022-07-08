/*
    This file sets up what get returned at https://station/query
    I'm not using "rest" as my endpoint because that is likely to overlap
    This is mostly just a welcome page and all the action will happen at versioned endpoints

    @author Joshua Runyan on 7/2/2022.
 */
package com.joshuarunyan.queryThroughRest;

//import com.joshuarunyan.queryThroughRest.V1Endpoint.BGetRequest;

import com.joshuarunyan.queryThroughRest.V1Endpoint.V1GetRequest;
import com.joshuarunyan.queryThroughRest.V1Endpoint.V1PostRequest;

import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.Flags;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.web.BWebServlet;
import javax.baja.web.WebOp;
import javax.servlet.http.HttpServletResponse;

@NiagaraType
@NiagaraProperty(
        name = "servletName",
        type = "baja:String",
        flags = Flags.HIDDEN | Flags.READONLY,
        defaultValue = "query"
)
public class BRestServlet extends BWebServlet{
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.joshuarunyan.queryThroughRest.BRestServlet(3468477866)1.0$ @*/
/* Generated Sat Jul 02 04:24:07 EDT 2022 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "servletName"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code servletName} property.
   * @see #getServletName
   * @see #setServletName
   */
  public static final Property servletName = newProperty(Flags.HIDDEN | Flags.READONLY, "query", null);
  
  /**
   * Get the {@code servletName} property.
   * @see #servletName
   */
  public String getServletName() { return getString(servletName); }
  
  /**
   * Set the {@code servletName} property.
   * @see #servletName
   */
  public void setServletName(String v) { setString(servletName, v, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BRestServlet.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  @Override
  public void doGet(WebOp op) throws Exception
  {
    String path = op.getPathInfo();

    if (path.contains("v1")) {
      V1GetRequest req = new V1GetRequest(op, path.replace("/v1/", ""));
      req.handleRequest();
    } else {
      op.setContentType("text/html; charset=utf-8");

      op.getWriter().write("<!DOCTYPE html><html><body>" +
                              "<h1>Welcome to the Query Through Rest Tool!</h1>" +
                              "<p>There isn't really anything to see here, you'll need to use the 'v1' endpoint to start using the tool</p>" +
                              "</body></html>");
    }
  }

  @Override
  public void doPost(WebOp op) throws Exception
  {
    String path = op.getPathInfo();

    if (path.contains("v1")) {
      V1PostRequest req = new V1PostRequest(op, path.replace("/v1/", ""));
      req.handleRequest();
    } else {
      HttpServletResponse resp = op.getResponse();
      resp.setStatus(404);
    }
  }
}
