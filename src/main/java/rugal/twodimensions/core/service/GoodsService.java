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
import rugal.twodimensions.core.entity.Vendor;

/**
 *
 * @author rugal
 */
public interface GoodsService {

    Goods deleteById(Integer id);

    Goods findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    Goods save(Goods bean);

    List<Goods> findByName(String name);

    /**
     *
     * @param bean goods type
     * @param num the quantity needs to be sell
     * @return null if transaction fail
     */
    Goods sell(Goods bean, Customer customer, Operator operator, Integer quantity, Float price);

    Goods stock(Goods bean, Vendor vendor, Operator operator, Integer num);

    Goods updateSellPrice(Goods bean, float price);
}
