/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.GoodsDao;
import rugal.twodimensions.core.entity.Customer;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.entity.Operator;
import rugal.twodimensions.core.entity.StockLog;
import rugal.twodimensions.core.entity.VenditionLog;
import rugal.twodimensions.core.entity.Vendor;
import rugal.twodimensions.core.service.GoodsService;
import rugal.twodimensions.core.service.StockLogService;
import rugal.twodimensions.core.service.VenditionLogService;

/**
 *
 * @author rugal
 */
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao dao;
    @Autowired
    private StockLogService stockLogService;
    @Autowired
    private VenditionLogService venditionLogService;

    @Override
    public Goods deleteById(Integer id) {
        return dao.deleteById(id);
    }

    @Override
    public Goods findById(Integer id) {
        return dao.findById(id);
    }

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        return dao.getPage(pageNo, pageSize);
    }

    @Override
    public Goods save(Goods bean) {
        return dao.save(bean);
    }

    public List<Goods> findByName(String name) {
        return dao.findByName(name);
    }

    /**
     *
     * @param bean goods type
     * @param num the quantity needs to be sell
     * @return null if transaction fail
     */
    @Override
    public Goods sell(Goods bean, Customer customer, Operator operator, Integer quantity, Float price) {
        // if not enough quantity of goods. return null value
        if (bean.getQuantity() < quantity) {
            return null;
        }
        Updater<Goods> updater = new Updater<Goods>(bean, Updater.UpdateMode.MIN);
        updater.include("quantity");
        bean.sell(quantity);
        VenditionLog log = new VenditionLog(bean, customer, operator, quantity, price);
        venditionLogService.save(log);
        return dao.updateByUpdater(updater);
    }

    @Override
    public Goods stock(Goods bean, Vendor vendor, Operator operator, Integer num) {
        // if not enough quantity of goods. return null value
        Updater<Goods> updater = new Updater<Goods>(bean, Updater.UpdateMode.MIN);
        updater.include("quantity");
        bean.stock(num);
        StockLog log = new StockLog(bean, vendor, operator, num);
        stockLogService.save(log);
        return dao.updateByUpdater(updater);
    }

    @Override
    public Goods updateSellPrice(Goods bean, float price) {
        // if not enough quantity of goods. return null value
        Updater<Goods> updater = new Updater<Goods>(bean, Updater.UpdateMode.MIN);
        updater.include("sellPrice");
        bean.setSellPrice(price);
        return dao.updateByUpdater(updater);
    }
}
