package com.timeshare.service.impl.assembly;

import com.timeshare.dao.assembly.FeeDAO;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.service.assembly.AttenderService;
import com.timeshare.service.assembly.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("FeeService")
public class FeeServiceImpl implements FeeService {

    @Autowired
    FeeDAO feeDAO;
    @Override
    public String saveFee(Fee Fee) {
        return feeDAO.saveFee(Fee);
    }

    @Override
    public String modifyFee(Fee Fee) {
        return feeDAO.modifyFee(Fee);
    }

    @Override
    public Fee findFeeById(String FeeId) {
        return feeDAO.findFeeById(FeeId);
    }

    @Override
    public List<Fee> findFeeList(Fee Fee, int startIndex, int loadSize) {
        return feeDAO.findFeeList(Fee,startIndex,loadSize);
    }

    @Override
    public int findFeeCount(Fee Fee) {
        return feeDAO.findFeeCount(Fee);
    }

    @Override
    public List<Fee> findFeeByAssemblyId(String assemblyId) {
        return feeDAO.findFeeByAssemblyId(assemblyId);
    }

    @Override
    public void delete(String feeId) {
        feeDAO.delete(feeId);
    }
}
