/**
 * 
 */
package com.xier.rabbit.rmq.test;

import java.io.Serializable;

public class PersonRecognizeLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7484761817407286887L;

	private String luid;
	
	/** 底库用户ID */
	private long personId;
	
	/** 关联用户ID */
	private long relationUserId;

	/** 关联用户所在机构 */
	private long relationOrgId;

	/** 识别类型 ，取值{@link RecognizeType}*/
	private int recognizeType;

	/** 识别记录的设备ID */
	private String deviceId;

	/** 识别记录的所在子机构 */
	private String recognizeSubOrgId;

	/** 识别记录的时间 */
	private long recognizeTime;
	
	/** 被识别为的目标图片id */
	private long targetFacePicId;

	/** 用于识别的图片质量 */
	private float quality;

	/** 识别出结果的可信度 */
	private float confidence;

	/** 识别出的年纪 */
	private float age;

	/** 是男人的可能性 */
	private float male;

	/** 是女人的可能性 */
	private float female;

	/** 识别图片url */
	private String imageUrl;
	
	/** 备注 */
	private String remark;
	
	private byte[] matchImageData;

	/**
	 * @return the luid
	 */
	public String getLuid() {
		return luid;
	}

	/**
	 * @param luid the luid to set
	 */
	public void setLuid(String luid) {
		this.luid = luid;
	}

	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}

	/**
	 * @return the relationUserId
	 */
	public long getRelationUserId() {
		return relationUserId;
	}

	/**
	 * @param relationUserId the relationUserId to set
	 */
	public void setRelationUserId(long relationUserId) {
		this.relationUserId = relationUserId;
	}

	/**
	 * @return the relationOrgId
	 */
	public long getRelationOrgId() {
		return relationOrgId;
	}

	/**
	 * @param relationOrgId the relationOrgId to set
	 */
	public void setRelationOrgId(long relationOrgId) {
		this.relationOrgId = relationOrgId;
	}

	/**
	 * @return the recognizeType
	 */
	public int getRecognizeType() {
		return recognizeType;
	}

	/**
	 * @param recognizeType the recognizeType to set
	 */
	public void setRecognizeType(int recognizeType) {
		this.recognizeType = recognizeType;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the recognizeSubOrgId
	 */
	public String getRecognizeSubOrgId() {
		return recognizeSubOrgId;
	}

	/**
	 * @param recognizeSubOrgId the recognizeSubOrgId to set
	 */
	public void setRecognizeSubOrgId(String recognizeSubOrgId) {
		this.recognizeSubOrgId = recognizeSubOrgId;
	}

	/**
	 * @return the recognizeTime
	 */
	public long getRecognizeTime() {
		return recognizeTime;
	}

	/**
	 * @param recognizeTime the recognizeTime to set
	 */
	public void setRecognizeTime(long recognizeTime) {
		this.recognizeTime = recognizeTime;
	}

	/**
	 * @return the targetFacePicId
	 */
	public long getTargetFacePicId() {
		return targetFacePicId;
	}

	/**
	 * @param targetFacePicId the targetFacePicId to set
	 */
	public void setTargetFacePicId(long targetFacePicId) {
		this.targetFacePicId = targetFacePicId;
	}

	/**
	 * @return the quality
	 */
	public float getQuality() {
		return quality;
	}

	/**
	 * @param quality the quality to set
	 */
	public void setQuality(float quality) {
		this.quality = quality;
	}

	/**
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	/**
	 * @return the age
	 */
	public float getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(float age) {
		this.age = age;
	}

	/**
	 * @return the male
	 */
	public float getMale() {
		return male;
	}

	/**
	 * @param male the male to set
	 */
	public void setMale(float male) {
		this.male = male;
	}

	/**
	 * @return the female
	 */
	public float getFemale() {
		return female;
	}

	/**
	 * @param female the female to set
	 */
	public void setFemale(float female) {
		this.female = female;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the matchImageData
	 */
	public byte[] getMatchImageData() {
		return matchImageData;
	}

	/**
	 * @param matchImageData the matchImageData to set
	 */
	public void setMatchImageData(byte[] matchImageData) {
		this.matchImageData = matchImageData;
	}
	
}
