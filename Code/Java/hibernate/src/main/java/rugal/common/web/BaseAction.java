/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.common.web;

import com.opensymphony.xwork2.ActionContext;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.dispatcher.SessionMap;

/**
 *
 * @author Rugal Bernstein
 */
public class BaseAction
{

    protected ActionContext context = ActionContext.getContext();

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected SessionMap session;

    public void setSession(Map map)
    {
        this.session = (SessionMap) map;
    }

    public void setServletRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response)
    {
        this.response = response;
    }

    public void setRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response)
    {
        this.response = response;
    }

    public void setSession(SessionMap session)
    {
        this.session = session;
    }


}
