package com.timeshare.service.assembly;

import com.timeshare.domain.assembly.Collection;
import com.timeshare.domain.assembly.Comment;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface CollectionService {

    String saveCollection(Collection Collection);

    List<Collection> getCollectionByAssemblyId(String assemblyId);

}
