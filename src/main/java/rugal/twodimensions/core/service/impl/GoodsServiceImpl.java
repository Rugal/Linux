/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.GoodsDao;
import rugal.twodimensions.core.dao.StockLogDao;
import rugal.twodimensions.core.dao.VenditionLogDao;
import rugal.twodimensions.core.entity.Customer;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.entity.Operator;
import rugal.twodimensions.core.entity.StockLog;
import rugal.twodimensions.core.entity.VenditionLog;
import rugal.twodimensions.core.service.GoodsService;

/**
 *
 * @author rugal
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	private static final Logger LOG = Logger.getLogger(GoodsServiceImpl.class.getName());
	@Autowired
	private GoodsDao dao;
	@Autowired
	private StockLogDao stockLogDao;
	@Autowired
	private VenditionLogDao venditionLogDao;

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
	public List<Goods> findByName(String name) {
		return dao.findByName(name);
	}

	/**
	 *
	 * @param bean
	 * @param customer
	 * @param operator
	 * @param quantity
	 * @param price
	 * @return
	 */
	@Override
	public Goods sell(Goods bean, Customer customer, Operator operator, Integer quantity, Float price) {
		// if not enough quantity of goods. return null value
		if (quantity <= 0 || bean.getQuantity() < quantity) {
			return null;
		}
		Updater<Goods> updater = new Updater<Goods>(bean, Updater.UpdateMode.MIN);
		updater.include("quantity");
		bean.sell(quantity);
		VenditionLog log = new VenditionLog(bean, customer, operator, quantity, price);
		venditionLogDao.save(log);
		return dao.updateByUpdater(updater);
	}

	/**
	 *
	 * @param bean
	 * @param operator
	 * @param num
	 * @return
	 */
	@Override
	public Goods stock(Goods bean, Operator operator, Integer num) {
		if (num <= 0) {
			return null;
		}
		if (null == bean.getGid()) {
			// for the first time of stock in this goods
			bean.setQuantity(num);
			dao.save(bean);
		} else {
			//stock this goods as not first time
			Updater<Goods> updater = new Updater<Goods>(bean, Updater.UpdateMode.MIN);
			updater.include("quantity");
			bean.stock(num);
			dao.updateByUpdater(updater);
		}// logging stock event
		StockLog log = new StockLog(bean, bean.getVid(), operator, num);
		stockLogDao.save(log);
		return bean;
	}

	@Override
	public Goods updateSellPrice(Goods bean, float price) {
		// if not enough quantity of goods. return null value
		Updater<Goods> updater = new Updater<Goods>(bean, Updater.UpdateMode.MIN);
		updater.include("sellPrice");
		bean.setSellPrice(price);
		return dao.updateByUpdater(updater);
	}

	@Override
	public Goods updateGoods(Goods bean) {
		Updater<Goods> updater = new Updater<Goods>(bean);
		LOG.info(MessageFormat.format("Updating goods id: {0}", bean.getGid()));
		return dao.updateByUpdater(updater);
	}
}
