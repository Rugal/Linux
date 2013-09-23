/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.controller.stock;

import java.util.Iterator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.StockLog;

/**
 *
 * @author rugal
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/dispatcher-servlet.xml", "classpath:/applicationContext.xml"})
public class ListStockLogTest {

	@Autowired
	public RequestMappingHandlerAdapter handlerAdapter;
	@Autowired
	private StockLogAction listStockLogAction;
	private static MockHttpServletRequest request;
	private static MockHttpServletResponse response;

	public void setHandlerAdapter(RequestMappingHandlerAdapter handlerAdapter) {
		this.handlerAdapter = handlerAdapter;
	}

	@BeforeClass
	public static void before() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
	}

	@Test
	public void testList() {
		request.setRequestURI("/stock/list.do");
		request.setMethod(HttpMethod.GET.name());
//		request.setParameter("pageNo", "0");
//		request.setParameter("pageSize", "20");
//		request.setParameter("desc", "true");
		ModelAndView mv = null;
		try {
			Class[] parameterTypes = new Class[3];
			parameterTypes[0] = int.class;
			parameterTypes[1] = int.class;
			parameterTypes[2] = boolean.class;
			mv = handlerAdapter.handle(request, response, new HandlerMethod(listStockLogAction, "list", parameterTypes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Pagination p = (Pagination) mv.getModelMap().get("page");
		for (Iterator<?> it = p.getList().iterator(); it.hasNext();) {
			StockLog log = (StockLog) it.next();
			System.out.println(log.getGid().getName());
		}
		System.out.println("Rugal Bernstein");
//		Assert.assertNotNull(mv);
//		Assert.assertEquals(response.getStatus(), 200);
//		Assert.assertEquals(mv.getViewName(), "/job/job_list");
	}
}
