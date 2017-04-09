package com.timeshare.dao;

import com.timeshare.domain.ImageObj;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

/**
 * Created by adam on 2016/6/28.
 */

public interface ImageObjDAO {

    ImageObj findImgByObjIdAndType(String objId,String objType);

    void updateImg(ImageObj obj);

    String saveImg(ImageObj obj);

    List<ImageObj> findImgByObjIds(String objIds);

    ImageObj findById(String imageId);

}
