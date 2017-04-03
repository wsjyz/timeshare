package com.timeshare.dao.assembly;

import com.timeshare.domain.assembly.Fee;

import java.util.List;


public interface FeeDAO {

     String saveFee(Fee Fee);

    String modifyFee(Fee Fee);

    Fee findFeeById(String FeeId);

    List<Fee> findFeeList(Fee Fee, int startIndex, int loadSize);

    int findFeeCount(Fee Fee);

}
