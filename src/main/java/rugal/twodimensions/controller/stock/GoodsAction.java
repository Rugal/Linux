/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.controller.stock;

import java.text.MessageFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import rugal.common.page.Pagination;
import rugal.common.springmvc.bind.annotation.FormModel;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.service.GoodsService;
import rugal.twodimensions.core.service.impl.GoodsServiceImpl;

/**
 *
 * @author Rugal Bernstein
 */
@Controller
public class GoodsAction {

	private static final Logger LOG = Logger.getLogger(GoodsServiceImpl.class.getName());
	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/stock/listGoods.do", method = RequestMethod.GET)
	public ModelAndView listGoods(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "20") int pageSize) {
		Pagination page = goodsService.getPage(pageNo, pageSize);
		ModelAndView mav = new ModelAndView("stock/listGoods");
		mav.getModelMap().put("page", page);
		return mav;
	}

	@RequestMapping(value = "/stock/showGoods.do")
	public ModelAndView showGoods(int gid) {
		Goods g = goodsService.findById(gid);
		ModelAndView mav = new ModelAndView("stock/showGoods");
		mav.getModelMap().put("goods", g);
		return mav;
	}

	//---------------------
	@RequestMapping(value = "/stock/modifyGoods.do", method = RequestMethod.POST)
	public ModelAndView modifyGoods(@FormModel("goods") Goods goods) {
		Goods g = goodsService.updateGoods(goods);
//		ModelAndView mav = new ModelAndView(MessageFormat.format("redirect:/stock/showGoods.do?gid={0}", g.getGid()));
		ModelAndView mav = new ModelAndView(MessageFormat.format("stock/showGoods", g.getGid()));
		LOG.info(MessageFormat.format("Includes {0} includes {1} includes {2}", goods.getName(), goods.getUnit(), goods.getSellPrice()));
		LOG.info(MessageFormat.format("Includes {0} includes {1} includes {2}", g.getName(), g.getUnit(), g.getSellPrice()));
		mav.getModelMap().put("goods", g);
		mav.getModelMap().put("modify", 1);
		return mav;
	}
}