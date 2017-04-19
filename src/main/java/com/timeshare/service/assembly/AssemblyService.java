package com.timeshare.service.assembly;

import com.timeshare.domain.Bid;
import com.timeshare.domain.assembly.Assembly;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface AssemblyService {

    String saveAssembly(Assembly Assembly);

    String modifyAssembly(Assembly Assembly);

    Assembly findAssemblyById(String AssemblyId);

    List<Assembly> findAssemblyList(Assembly Assembly, int startIndex, int loadSize);

    int findAssemblyCount(Assembly Assembly);
    List<Assembly> findSignAssemblyList(Assembly Assembly,String userId, int startIndex, int loadSize);
    int deleteAssembly(String assemblyId);

    List<Assembly> findCollectionAssemblyList(Assembly assembly, String userId, int startIndex, int loadSize);
}
