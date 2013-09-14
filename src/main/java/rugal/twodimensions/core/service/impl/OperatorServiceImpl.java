/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.OperatorDao;
import rugal.twodimensions.core.entity.Operator;
import rugal.twodimensions.core.service.OperatorService;

/**
 *
 * @author rugal
 */
@Service
public class OperatorServiceImpl implements OperatorService {

    @Autowired
    private OperatorDao operatorDao;

    @Override
    public Operator deleteById(Integer id) {
        return operatorDao.deleteById(id);
    }

    @Override
    public Operator findById(Integer id) {
        return operatorDao.findById(id);
    }

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        return operatorDao.getPage(pageNo, pageSize);
    }

    @Override
    public Operator save(Operator bean) {
        return operatorDao.save(bean);
    }

    @Override
    public boolean authenticate(Operator bean) {
        if (null == bean) {
            return false;
        }
        Operator inner = operatorDao.findById(bean.getOid());
        if (inner.equals(bean)) {
            return true;
        } else {
            return false;
        }
    }
}
