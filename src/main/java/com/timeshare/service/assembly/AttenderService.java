package com.timeshare.service.assembly;

import com.timeshare.domain.assembly.Attender;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface AttenderService {

    String saveAttender(Attender Attender);

    String modifyAttender(Attender Attender);

    Attender findAttenderById(String AttenderId);

    List<Attender> findAttenderList(Attender Attender, int startIndex, int loadSize);

    int findAttenderCount(Attender Attender);
    List<Attender> getListByAssemblyId(String assemblyId);

}
