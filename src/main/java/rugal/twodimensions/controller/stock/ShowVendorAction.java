/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.controller.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import rugal.twodimensions.core.entity.Vendor;
import rugal.twodimensions.core.service.VendorService;

/**
 *
 * @author Rugal Bernstein
 */
@Controller
public class ShowVendorAction {

	@Autowired
	private VendorService vendorService;

	@RequestMapping(value = "/stock/showVendor.do")
	public ModelAndView showVendor(@RequestParam(required = true) int vid) {
		Vendor v = vendorService.findById(vid);
		ModelAndView mav = new ModelAndView("stock/showVendor");
		mav.getModelMap().put("vendor", v);
		return mav;
	}
}
