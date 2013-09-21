/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.controller.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.service.GoodsService;

/**
 *
 * @author Rugal Bernstein
 */
@Controller
public class ListGoodsAction {

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/stock/listGoods.do", method = RequestMethod.GET)
	public ModelAndView listGoods(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "20") int pageSize) {
		Pagination page = goodsService.getPage(pageNo, pageSize);
		ModelAndView mav = new ModelAndView("stock/listGoods");
		mav.getModelMap().put("page", page);
		return mav;
	}
}