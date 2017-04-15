package com.timeshare.service.impl.assembly;

import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.assembly.CollectionDAO;
import com.timeshare.dao.assembly.CommentDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Collection;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.CollectionService;
import com.timeshare.service.assembly.CommentService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("CollectionService")
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    CollectionDAO collectionDAO;

    @Override
    public String saveCollection(Collection Collection) {
        return collectionDAO.saveCollection(Collection);
    }

    @Override
    public List<Collection> getCollectionByAssemblyId(String assemblyId) {
        return collectionDAO.getCollectionByAssemblyId(assemblyId);
    }
}
