/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.controller.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.service.GoodsService;

/**
 *
 * @author Rugal Bernstein
 */
@Controller
public class ShowGoodsAction {

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/stock/showGoods.do")
	public ModelAndView showGoods(int gid) {
		Goods g = goodsService.findById(gid);
		ModelAndView mav = new ModelAndView("stock/showGoods");
		mav.getModelMap().put("goods", g);
		return mav;
	}
}
