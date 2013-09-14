/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao;

import java.util.List;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.Goods;

/**
 *
 * @author rugal
 */
public interface GoodsDao {

    Goods deleteById(Integer id);

    Goods findById(Integer id);

    List<Goods> findByName(String name);

    Pagination getPage(int pageNo, int pageSize);

    Goods save(Goods bean);

    Goods updateByUpdater(Updater<Goods> updater);
}
