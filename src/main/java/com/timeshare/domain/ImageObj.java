package com.timeshare.domain;


import com.timeshare.domain.annotation.Column;
import com.timeshare.domain.annotation.Table;

/**
 * Created by dam on 2014/7/3.
 */
@Table(name = "t_img_obj", comment = "图片")
public class ImageObj extends BaseDomain {
	@Column(name = "image_id", pk = true, length = 32)
	private String imageId;
	@Column(name = "image_type", length = 32, comment = "用途PORTRAIT头像|WORKCASE案例")
	private String imageType;
	@Column(name = "obj_id", length = 32, comment = "所属对象ID")
	private String objId;
	@Column(name = "hpixel", length = 5, comment = "图片高像素值")
	private int hpixel;
	@Column(name = "wpixel", length = 5, comment = "图片宽像素值")
	private int wpixel;

	private String imageUrl;

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public int getHpixel() {
		return hpixel;
	}

	public void setHpixel(int hpixel) {
		this.hpixel = hpixel;
	}

	public int getWpixel() {
		return wpixel;
	}

	public void setWpixel(int wpixel) {
		this.wpixel = wpixel;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}
}
