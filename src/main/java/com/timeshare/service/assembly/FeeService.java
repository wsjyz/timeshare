package com.timeshare.service.assembly;

import com.timeshare.domain.assembly.Fee;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface FeeService {


    String saveFee(Fee Fee);

    String modifyFee(Fee Fee);

    Fee findFeeById(String FeeId);

    List<Fee> findFeeList(Fee Fee, int startIndex, int loadSize);

    int findFeeCount(Fee Fee);
    List<Fee> findFeeByAssemblyId(String assemblyId);
    void delete(String feeId);
}
