package com.xerox.amazonws.ec2;

import com.xerox.amazonws.typica.jaxb.SpotInstanceRequestSetItemType;

import java.util.Calendar;
import java.util.Map;

public class SpotInstanceRequest {
    protected String spotInstanceRequestId;
    protected double spotPrice;
    protected SpotInstanceType type;
    protected SpotInstanceState state;
//    protected SpotInstanceStateFaultType fault;
//    protected Calendar validFrom;
//    protected Calendar validUntil;
    protected String launchGroup;
    protected String availabilityZoneGroup;
//    protected LaunchSpecificationResponseType launchSpecification;
    protected String instanceId;
    protected Calendar createTime;
    protected String productDescription;
	protected Map<String, String> tagSet;

    public SpotInstanceRequest() {
    }

    SpotInstanceRequest(SpotInstanceRequestSetItemType item, Map<String, String> tagSet) {
        this.spotInstanceRequestId = item.getSpotInstanceRequestId();
        this.spotPrice = Double.parseDouble(item.getSpotPrice());
        this.type = SpotInstanceType.getTypeFromString(item.getType());
        this.state = SpotInstanceState.getStateFromString(item.getState());
        this.launchGroup = item.getLaunchGroup();
        this.availabilityZoneGroup = item.getAvailabilityZoneGroup();
        this.instanceId = item.getInstanceId();
        this.createTime = item.getCreateTime().toGregorianCalendar();
        this.productDescription = item.getProductDescription();
		this.tagSet = tagSet;
    }

    public String getSpotInstanceRequestId() {
        return spotInstanceRequestId;
    }

    public double getSpotPrice() {
        return spotPrice;
    }

    public SpotInstanceType getType() {
        return type;
    }

    public SpotInstanceState getState() {
        return state;
    }

    public String getLaunchGroup() {
        return launchGroup;
    }

    public String getAvailabilityZoneGroup() {
        return availabilityZoneGroup;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public String getProductDescription() {
        return productDescription;
    }

	public Map<String, String> getTagSet() {
		return tagSet;
	}

    @Override
    public String toString() {
        return "SpotInstanceRequest[" +
                "spotInstanceRequestId='" + spotInstanceRequestId + '\'' +
                ", spotPrice=" + spotPrice +
                ", type='" + type + '\'' +
                ", state=" + state +
                ", launchGroup='" + launchGroup + '\'' +
                ", availabilityZoneGroup='" + availabilityZoneGroup + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", createTime=" + createTime +
                ", productDescription='" + productDescription + '\'' +
                ']';
    }
}
