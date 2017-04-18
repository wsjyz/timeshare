package com.timeshare.dao.assembly;

import com.timeshare.domain.assembly.Assembly;

import java.util.List;


public interface AssemblyDAO {

     String saveAssembly(Assembly Assembly);

    String modifyAssembly(Assembly Assembly);

    Assembly findAssemblyById(String AssemblyId);

    List<Assembly> findAssemblyList(Assembly Assembly, int startIndex, int loadSize);

    int findAssemblyCount(Assembly Assembly);
    List<Assembly> findSignAssemblyList(Assembly Assembly,String userId, int startIndex, int loadSize);


    int deleteAssembly(String assemblyId);
}
