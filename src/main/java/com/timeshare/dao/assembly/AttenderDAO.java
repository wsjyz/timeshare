package com.timeshare.dao.assembly;

import com.timeshare.domain.assembly.Attender;

import java.util.List;


public interface AttenderDAO {

     String saveAttender(Attender Attender);

    String modifyAttender(Attender Attender);

    Attender findAttenderById(String AttenderId);

    List<Attender> findAttenderList(Attender Attender, int startIndex, int loadSize);

    int findAttenderCount(Attender Attender);
}
