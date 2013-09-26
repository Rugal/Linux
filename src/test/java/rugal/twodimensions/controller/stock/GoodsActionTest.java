/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.controller.stock;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import rugal.twodimensions.controller.TestBase;
import rugal.twodimensions.core.entity.Goods;

/**
 *
 * @author rugal
 */
public class GoodsActionTest extends TestBase {

	@Autowired
	private GoodsAction goodsAction;

	@BeforeClass
	public static void before() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
	}

	public GoodsActionTest() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

//	@Test
//	public void testListGoods() {
//		System.out.println("listGoods");
//		int pageNo = 0;
//		int pageSize = 0;
//		GoodsAction instance = new GoodsAction();
//		ModelAndView expResult = null;
//		ModelAndView result = instance.listGoods(pageNo, pageSize);
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testShowGoods() {
//		System.out.println("showGoods");
//		int gid = 0;
//		GoodsAction instance = new GoodsAction();
//		ModelAndView expResult = null;
//		ModelAndView result = instance.showGoods(gid);
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testModifyGoods() {
//		System.out.println("modifyGoods");
//		Goods goods = null;
//		GoodsAction instance = new GoodsAction();
//		ModelAndView expResult = null;
//		ModelAndView result = instance.modifyGoods(goods);
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}
	@Test
	public void testModifyGoods() {
		request.setRequestURI("/stock/modifyGoods.do");
		request.setMethod(HttpMethod.POST.name());
		request.setParameter("goods.gid", "20");
		request.setParameter("goods.name", "海贼王");
		request.setParameter("goods.unit", "只");
		request.setParameter("goods.stockPrice", "200");
		request.setParameter("goods.sellPrice", "8000");
		ModelAndView mv = null;
		try {
			Class[] parameterTypes = new Class[1];
			parameterTypes[0] = Goods.class;

			mv = handlerAdapter.handle(request, response, new HandlerMethod(goodsAction, "modifyGoods", parameterTypes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Goods goods = (Goods) mv.getModelMap().get("goods");

		System.out.println(goods.getName());
		System.out.println(goods.getSellPrice());
		System.out.println(goods.getUnit());
		System.out.println("Rugal Bernstein");
	}
}
