package com.timeshare.dao.assembly;

import com.timeshare.domain.assembly.Collection;

import java.util.List;


public interface CollectionDAO {

     String saveCollection(Collection Collection);

    List<Collection> getCollectionByAssemblyId(String assemblyId);

}
