/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service;

import java.util.List;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.Customer;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.entity.Operator;

/**
 *
 * @author rugal
 */
public interface GoodsService {

	Goods deleteById(Integer id);

	Goods findById(Integer id);

	Pagination getPage(int pageNo, int pageSize);

//    Goods save(Goods bean);
	List<Goods> findByName(String name);

	Goods sell(Goods bean, Customer customer, Operator operator, Integer quantity, Float price);

	Goods stock(Goods bean, Operator operator, Integer num);

	Goods updateSellPrice(Goods bean, float price);

	Goods updateGoods(Goods bean);
}
