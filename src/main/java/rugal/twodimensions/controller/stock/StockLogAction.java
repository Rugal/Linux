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
import rugal.twodimensions.core.service.StockLogService;

/**
 *
 * @author Rugal Bernstein
 */
@Controller
public class StockLogAction {

	@Autowired
	private StockLogService stockLogService;

	@RequestMapping(value = "/stock/listStockLog.do", method = RequestMethod.GET)
	public ModelAndView listStockLog(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "true") boolean desc) {
		Pagination page = stockLogService.getPage(desc, pageNo, pageSize);
		ModelAndView mav = new ModelAndView("stock/listStockLog");
		mav.getModelMap().put("page", page);
		if (true == desc) {
			mav.getModelMap().put("desc", "false");
		} else {
			mav.getModelMap().put("desc", "true");
		}
		return mav;
	}
}
