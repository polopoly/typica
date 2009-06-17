//
// typica - A client library for Amazon Web Services
// Copyright (C) 2007,2008,2009 Xerox Corporation
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.xerox.amazonws.ec2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xerox.amazonws.common.AWSException;
import com.xerox.amazonws.common.AWSQueryConnection;
import com.xerox.amazonws.typica.jaxb.AllocateAddressResponse;
import com.xerox.amazonws.typica.jaxb.AssociateAddressResponse;
import com.xerox.amazonws.typica.jaxb.AttachmentSetResponseType;
import com.xerox.amazonws.typica.jaxb.AttachmentSetItemResponseType;
import com.xerox.amazonws.typica.jaxb.AttachVolumeResponse;
import com.xerox.amazonws.typica.jaxb.AvailabilityZoneItemType;
import com.xerox.amazonws.typica.jaxb.AvailabilityZoneSetType;
import com.xerox.amazonws.typica.jaxb.AuthorizeSecurityGroupIngressResponse;
import com.xerox.amazonws.typica.jaxb.BlockDeviceMappingType;
import com.xerox.amazonws.typica.jaxb.BlockDeviceMappingItemType;
import com.xerox.amazonws.typica.jaxb.BundleInstanceResponse;
import com.xerox.amazonws.typica.jaxb.BundleInstanceTaskType;
import com.xerox.amazonws.typica.jaxb.CancelBundleTaskResponse;
import com.xerox.amazonws.typica.jaxb.CreateKeyPairResponse;
import com.xerox.amazonws.typica.jaxb.CreateSnapshotResponse;
import com.xerox.amazonws.typica.jaxb.CreateVolumeResponse;
import com.xerox.amazonws.typica.jaxb.ConfirmProductInstanceResponse;
import com.xerox.amazonws.typica.jaxb.CreateSecurityGroupResponse;
import com.xerox.amazonws.typica.jaxb.DeleteKeyPairResponse;
import com.xerox.amazonws.typica.jaxb.DeleteSecurityGroupResponse;
import com.xerox.amazonws.typica.jaxb.DeleteSnapshotResponse;
import com.xerox.amazonws.typica.jaxb.DeleteVolumeResponse;
import com.xerox.amazonws.typica.jaxb.DeregisterImageResponse;
import com.xerox.amazonws.typica.jaxb.DescribeAddressesResponse;
import com.xerox.amazonws.typica.jaxb.DescribeAddressesResponseInfoType;
import com.xerox.amazonws.typica.jaxb.DescribeAddressesResponseItemType;
import com.xerox.amazonws.typica.jaxb.DescribeAvailabilityZonesResponse;
import com.xerox.amazonws.typica.jaxb.DescribeBundleTasksResponse;
import com.xerox.amazonws.typica.jaxb.DescribeBundleTasksItemType;
import com.xerox.amazonws.typica.jaxb.DescribeImageAttributeResponse;
import com.xerox.amazonws.typica.jaxb.DescribeImagesResponse;
import com.xerox.amazonws.typica.jaxb.DescribeImagesResponseInfoType;
import com.xerox.amazonws.typica.jaxb.DescribeImagesResponseItemType;
import com.xerox.amazonws.typica.jaxb.DescribeInstancesResponse;
import com.xerox.amazonws.typica.jaxb.DescribeReservedInstancesResponse;
import com.xerox.amazonws.typica.jaxb.DescribeReservedInstancesResponseSetItemType;
import com.xerox.amazonws.typica.jaxb.DescribeReservedInstancesOfferingsResponse;
import com.xerox.amazonws.typica.jaxb.DescribeReservedInstancesOfferingsResponseSetItemType;
import com.xerox.amazonws.typica.jaxb.DescribeSnapshotsResponse;
import com.xerox.amazonws.typica.jaxb.DescribeSnapshotsSetResponseType;
import com.xerox.amazonws.typica.jaxb.DescribeSnapshotsSetItemResponseType;
import com.xerox.amazonws.typica.jaxb.DescribeVolumesResponse;
import com.xerox.amazonws.typica.jaxb.DescribeVolumesSetResponseType;
import com.xerox.amazonws.typica.jaxb.DescribeVolumesSetItemResponseType;
import com.xerox.amazonws.typica.jaxb.DescribeKeyPairsResponse;
import com.xerox.amazonws.typica.jaxb.DescribeKeyPairsResponseInfoType;
import com.xerox.amazonws.typica.jaxb.DescribeKeyPairsResponseItemType;
import com.xerox.amazonws.typica.jaxb.DescribeRegionsResponse;
import com.xerox.amazonws.typica.jaxb.DescribeSecurityGroupsResponse;
import com.xerox.amazonws.typica.jaxb.DetachVolumeResponse;
import com.xerox.amazonws.typica.jaxb.DisassociateAddressResponse;
import com.xerox.amazonws.typica.jaxb.GetConsoleOutputResponse;
import com.xerox.amazonws.typica.jaxb.GroupItemType;
import com.xerox.amazonws.typica.jaxb.GroupSetType;
import com.xerox.amazonws.typica.jaxb.IpPermissionSetType;
import com.xerox.amazonws.typica.jaxb.IpPermissionSetType;
import com.xerox.amazonws.typica.jaxb.IpPermissionType;
import com.xerox.amazonws.typica.jaxb.IpRangeItemType;
import com.xerox.amazonws.typica.jaxb.IpRangeSetType;
import com.xerox.amazonws.typica.jaxb.LaunchPermissionItemType;
import com.xerox.amazonws.typica.jaxb.LaunchPermissionListType;
import com.xerox.amazonws.typica.jaxb.ModifyImageAttributeResponse;
import com.xerox.amazonws.typica.jaxb.MonitorInstancesResponseType;
import com.xerox.amazonws.typica.jaxb.MonitorInstancesResponseSetItemType;
import com.xerox.amazonws.typica.jaxb.NullableAttributeValueType;
import com.xerox.amazonws.typica.jaxb.ObjectFactory;
import com.xerox.amazonws.typica.jaxb.ProductCodeListType;
import com.xerox.amazonws.typica.jaxb.ProductCodeItemType;
import com.xerox.amazonws.typica.jaxb.ProductCodesSetType;
import com.xerox.amazonws.typica.jaxb.ProductCodesSetItemType;
import com.xerox.amazonws.typica.jaxb.PurchaseReservedInstancesOfferingResponse;
import com.xerox.amazonws.typica.jaxb.RebootInstancesResponse;
import com.xerox.amazonws.typica.jaxb.RegionItemType;
import com.xerox.amazonws.typica.jaxb.RegionSetType;
import com.xerox.amazonws.typica.jaxb.RegisterImageResponse;
import com.xerox.amazonws.typica.jaxb.ReleaseAddressResponse;
import com.xerox.amazonws.typica.jaxb.RevokeSecurityGroupIngressResponse;
import com.xerox.amazonws.typica.jaxb.ReservationSetType;
import com.xerox.amazonws.typica.jaxb.ReservationInfoType;
import com.xerox.amazonws.typica.jaxb.ResetImageAttributeResponse;
import com.xerox.amazonws.typica.jaxb.RunningInstancesItemType;
import com.xerox.amazonws.typica.jaxb.RunningInstancesSetType;
import com.xerox.amazonws.typica.jaxb.RunInstancesResponse;
import com.xerox.amazonws.typica.jaxb.SecurityGroupSetType;
import com.xerox.amazonws.typica.jaxb.SecurityGroupItemType;
import com.xerox.amazonws.typica.jaxb.TerminateInstancesResponse;
import com.xerox.amazonws.typica.jaxb.TerminateInstancesResponseInfoType;
import com.xerox.amazonws.typica.jaxb.TerminateInstancesResponseItemType;
import com.xerox.amazonws.typica.jaxb.UserIdGroupPairType;
import com.xerox.amazonws.typica.jaxb.UserIdGroupPairSetType;

/**
 * A Java wrapper for the EC2 web services API
 */
public class Jec2 extends AWSQueryConnection {

    private static Log logger = LogFactory.getLog(Jec2.class);

	/**
	 * Initializes the ec2 service with your AWS login information.
	 *
     * @param awsAccessId The your user key into AWS
     * @param awsSecretKey The secret string used to generate signatures for authentication.
	 */
    public Jec2(String awsAccessId, String awsSecretKey) {
        this(awsAccessId, awsSecretKey, true);
    }

	/**
	 * Initializes the ec2 service with your AWS login information.
	 *
     * @param awsAccessId The your user key into AWS
     * @param awsSecretKey The secret string used to generate signatures for authentication.
     * @param isSecure True if the data should be encrypted on the wire on the way to or from EC2.
	 */
    public Jec2(String awsAccessId, String awsSecretKey, boolean isSecure) {
        this(awsAccessId, awsSecretKey, isSecure, "ec2.amazonaws.com");
    }

	/**
	 * Initializes the ec2 service with your AWS login information.
	 *
     * @param awsAccessId The your user key into AWS
     * @param awsSecretKey The secret string used to generate signatures for authentication.
     * @param isSecure True if the data should be encrypted on the wire on the way to or from EC2.
     * @param server Which host to connect to.  Usually, this will be ec2.amazonaws.com
	 */
    public Jec2(String awsAccessId, String awsSecretKey, boolean isSecure,
                             String server)
    {
        this(awsAccessId, awsSecretKey, isSecure, server,
             isSecure ? 443 : 80);
    }

    /**
	 * Initializes the ec2 service with your AWS login information.
	 *
     * @param awsAccessId The your user key into AWS
     * @param awsSecretKey The secret string used to generate signatures for authentication.
     * @param isSecure True if the data should be encrypted on the wire on the way to or from EC2.
     * @param server Which host to connect to.  Usually, this will be ec2.amazonaws.com
     * @param port Which port to use.
     */
    public Jec2(String awsAccessId, String awsSecretKey, boolean isSecure,
                             String server, int port)
    {
		super(awsAccessId, awsSecretKey, isSecure, server, port);
		ArrayList<String> vals = new ArrayList<String>();
		vals.add("2009-04-04");
		super.headers.put("Version", vals);
    }

	/**
	 * Register the given AMI.
	 * 
	 * @param imageLocation An AMI path within S3.
	 * @return A unique AMI ID that can be used to create and manage instances of this AMI.
	 * @throws EC2Exception wraps checked exceptions
	 * TODO: need to return request id
	 */
	public String registerImage(String imageLocation) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageLocation", imageLocation);
		GetMethod method = new GetMethod();
		try {
			RegisterImageResponse response =
					makeRequestInt(method, "RegisterImage", params, RegisterImageResponse.class);
			return response.getImageId();
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Deregister the given AMI.
	 * 
	 * @param imageId An AMI ID as returned by {@link #registerImage(String)}.
	 * @throws EC2Exception wraps checked exceptions
	 * TODO: need to return request id
	 */
	public void deregisterImage(String imageId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageId", imageId);
		GetMethod method = new GetMethod();
		try {
			DeregisterImageResponse response =
					makeRequestInt(method, "DeregisterImage", params, DeregisterImageResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not deregister image : "+imageId+". No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Describe the given AMIs.
	 * 
	 * @param imageIds An array of AMI IDs as returned by {@link #registerImage(String)}.
	 * @return A list of {@link ImageDescription} instances describing each AMI ID.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ImageDescription> describeImages(String[] imageIds) throws EC2Exception {
		return describeImages(Arrays.asList(imageIds));
	}

	/**
	 * Describe the given AMIs.
	 * 
	 * @param imageIds A list of AMI IDs as returned by {@link #registerImage(String)}.
	 * @return A list of {@link ImageDescription} instances describing each AMI ID.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ImageDescription> describeImages(List<String> imageIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<imageIds.size(); i++) {
			params.put("ImageId."+(i+1), imageIds.get(i));
		}
		return describeImages(params);
	}

	/**
	 * Describe the AMIs belonging to the supplied owners.
	 * 
	 * @param owners A list of owners.
	 * @return A list of {@link ImageDescription} instances describing each AMI ID.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ImageDescription> describeImagesByOwner(List<String> owners) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<owners.size(); i++) {
			params.put("Owner."+(i+1), owners.get(i));
		}
		return describeImages(params);
	}

	/**
	 * Describe the AMIs executable by supplied users.
	 * 
	 * @param users A list of users.
	 * @return A list of {@link ImageDescription} instances describing each AMI ID.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ImageDescription> describeImagesByExecutability(List<String> users) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<users.size(); i++) {
			params.put("ExecutableBy."+(i+1), users.get(i));
		}
		return describeImages(params);
	}

	/**
	 * Describe the AMIs that match the intersection of the criteria supplied
	 * 
	 * @param imageIds A list of AMI IDs as returned by {@link #registerImage(String)}.
	 * @param owners A list of owners.
	 * @param users A list of users.
	 * @param type An image type.
	 * @return A list of {@link ImageDescription} instances describing each AMI ID.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ImageDescription> describeImages(List<String> imageIds, List<String> owners,
										List<String> users, ImageType type) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<imageIds.size(); i++) {
			params.put("ImageId."+(i+1), imageIds.get(i));
		}
		for (int i=0 ; i<owners.size(); i++) {
			params.put("Owner."+(i+1), owners.get(i));
		}
		for (int i=0 ; i<users.size(); i++) {
			params.put("ExecutableBy."+(i+1), users.get(i));
		}
		if (type != null) {
			params.put("ImageType", type.getTypeId());
		}
		return describeImages(params);
	}


	protected List<ImageDescription> describeImages(Map<String, String> params) throws EC2Exception {
		GetMethod method = new GetMethod();
		try {
			DescribeImagesResponse response =
					makeRequestInt(method, "DescribeImages", params, DescribeImagesResponse.class);
			List<ImageDescription> result = new ArrayList<ImageDescription>();
			DescribeImagesResponseInfoType set = response.getImagesSet();
			Iterator set_iter = set.getItems().iterator();
			while (set_iter.hasNext()) {
				DescribeImagesResponseItemType item = (DescribeImagesResponseItemType) set_iter
						.next();
				ArrayList<String> codes = new ArrayList<String>();
				ProductCodesSetType code_set = item.getProductCodes();
				if (code_set != null) {
					for (ProductCodesSetItemType code : code_set.getItems()) {
						codes.add(code.getProductCode());
					}
				}
				result.add(new ImageDescription(item.getImageId(),
						item.getImageLocation(), item.getImageOwnerId(),
						item.getImageState(), item.isIsPublic(), codes,
						item.getArchitecture(), item.getImageType(),
						item.getKernelId(), item.getRamdiskId(), item.getPlatform()));
			}
			return result;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Requests reservation of a number of instances.
	 * <p>
	 * This will begin launching those instances for which a reservation was
	 * successfully obtained.
	 * <p>
	 * If less than <code>minCount</code> instances are available no instances
	 * will be reserved.
	 * <p>
	 * NOTE: this method defaults to the AWS desired "public" addressing type.
	 * NOTE: this method defaults to the small(traditional) instance type.
	 * 
	 * @param imageId An AMI ID as returned by {@link #registerImage(String)}.
	 * @param minCount The minimum number of instances to attempt to reserve.
	 * @param maxCount The maximum number of instances to attempt to reserve.
	 * @param groupSet A (possibly empty) set of security group definitions.
	 * @param userData User supplied data that will be made available to the instance(s)
	 * @return A {@link com.xerox.amazonws.ec2.ReservationDescription} describing the instances that
	 *         have been reserved.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ReservationDescription runInstances(String imageId, int minCount,
			int maxCount, List<String> groupSet, String userData, String keyName)
				throws EC2Exception {
		return runInstances(imageId, minCount, maxCount, groupSet, userData, keyName, true, InstanceType.DEFAULT);
	}

	/**
	 * Requests reservation of a number of instances.
	 * <p>
	 * This will begin launching those instances for which a reservation was
	 * successfully obtained.
	 * <p>
	 * If less than <code>minCount</code> instances are available no instances
	 * will be reserved.
	 * NOTE: this method defaults to the small(traditional) instance type.
	 * 
	 * @param imageId An AMI ID as returned by {@link #registerImage(String)}.
	 * @param minCount The minimum number of instances to attempt to reserve.
	 * @param maxCount The maximum number of instances to attempt to reserve.
	 * @param groupSet A (possibly empty) set of security group definitions.
	 * @param userData User supplied data that will be made available to the instance(s)
	 * @param publicAddr sets addressing mode to public
	 * @return A {@link com.xerox.amazonws.ec2.ReservationDescription} describing the instances that
	 *         have been reserved.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ReservationDescription runInstances(String imageId, int minCount,
			int maxCount, List<String> groupSet, String userData, String keyName, boolean publicAddr)
				throws EC2Exception {
		return runInstances(imageId, minCount, maxCount, groupSet, userData, keyName, publicAddr, InstanceType.DEFAULT);
	}

	/**
	 * Requests reservation of a number of instances.
	 * <p>
	 * This will begin launching those instances for which a reservation was
	 * successfully obtained.
	 * <p>
	 * If less than <code>minCount</code> instances are available no instances
	 * will be reserved.
	 * NOTE: this method defaults to the AWS desired "public" addressing type.
	 * 
	 * @param imageId An AMI ID as returned by {@link #registerImage(String)}.
	 * @param minCount The minimum number of instances to attempt to reserve.
	 * @param maxCount The maximum number of instances to attempt to reserve.
	 * @param groupSet A (possibly empty) set of security group definitions.
	 * @param userData User supplied data that will be made available to the instance(s)
	 * @param type instance type
	 * @return A {@link com.xerox.amazonws.ec2.ReservationDescription} describing the instances that
	 *         have been reserved.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ReservationDescription runInstances(String imageId, int minCount,
			int maxCount, List<String> groupSet, String userData, String keyName, InstanceType type)
				throws EC2Exception {
		return runInstances(imageId, minCount, maxCount, groupSet, userData, keyName, true, type);
	}

	/**
	 * Requests reservation of a number of instances.
	 * <p>
	 * This will begin launching those instances for which a reservation was
	 * successfully obtained.
	 * <p>
	 * If less than <code>minCount</code> instances are available no instances
	 * will be reserved.
	 * 
	 * @param imageId An AMI ID as returned by {@link #registerImage(String)}.
	 * @param minCount The minimum number of instances to attempt to reserve.
	 * @param maxCount The maximum number of instances to attempt to reserve.
	 * @param groupSet A (possibly empty) set of security group definitions.
	 * @param userData User supplied data that will be made available to the instance(s)
	 * @param publicAddr sets addressing mode to public
	 * @param type instance type
	 * @return A {@link com.xerox.amazonws.ec2.ReservationDescription} describing the instances that
	 *         have been reserved.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ReservationDescription runInstances(String imageId, int minCount,
			int maxCount, List<String> groupSet, String userData, String keyName, boolean publicAddr, InstanceType type)
				throws EC2Exception {
		return runInstances(imageId, minCount, maxCount, groupSet, userData, keyName, publicAddr, type, null, null, null, null);
	}

	/**
	 * Requests reservation of a number of instances.
	 * <p>
	 * This will begin launching those instances for which a reservation was
	 * successfully obtained.
	 * <p>
	 * If less than <code>minCount</code> instances are available no instances
	 * will be reserved.
	 * 
	 * @param imageId An AMI ID as returned by {@link #registerImage(String)}.
	 * @param minCount The minimum number of instances to attempt to reserve.
	 * @param maxCount The maximum number of instances to attempt to reserve.
	 * @param groupSet A (possibly empty) set of security group definitions.
	 * @param userData User supplied data that will be made available to the instance(s)
	 * @param publicAddr sets addressing mode to public
	 * @param type instance type
	 * @param availabilityZone the zone in which to launch the instance(s)
	 * @param kernelId id of the kernel with which to launch the instance(s)
	 * @param ramdiskId id of the RAM disk with wich to launch the imstance(s)
	 * @param blockDeviceMappings mappings of virtual to device names
	 * @return A {@link com.xerox.amazonws.ec2.ReservationDescription} describing the instances that
	 *         have been reserved.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ReservationDescription runInstances(String imageId, int minCount,
			int maxCount, List<String> groupSet, String userData, String keyName,
			boolean publicAddr, InstanceType type, String availabilityZone,
			String kernelId, String ramdiskId, List<BlockDeviceMapping> blockDeviceMappings)
				throws EC2Exception {

		LaunchConfiguration lc = new LaunchConfiguration(imageId);
		lc.setMinCount(minCount);
		lc.setMaxCount(maxCount);
		lc.setSecurityGroup(groupSet);
		if (userData != null) {
			lc.setUserData(userData.getBytes());
		}
		lc.setKeyName(keyName);
		lc.setInstanceType(type);
		lc.setAvailabilityZone(availabilityZone);
		lc.setKernelId(kernelId);
		lc.setRamdiskId(ramdiskId);
		lc.setBlockDevicemappings(blockDeviceMappings);
		return runInstances(lc);
	}

	/**
	 * Requests reservation of a number of instances.
	 * <p>
	 * This will begin launching those instances for which a reservation was
	 * successfully obtained.
	 * <p>
	 * If less than <code>minCount</code> instances are available no instances
	 * will be reserved.
	 * 
	 * @param lc object containing launch configuration
	 * @return A {@link com.xerox.amazonws.ec2.ReservationDescription} describing the instances that
	 *         have been reserved.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ReservationDescription runInstances(LaunchConfiguration lc) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageId", lc.getImageId());
		params.put("MinCount", "" + lc.getMinCount());
		params.put("MaxCount", "" + lc.getMaxCount());

		byte[] userData = lc.getUserData();
		if (userData != null && userData.length > 0) {
			params.put("UserData",
			new String(Base64.encodeBase64(userData)));
		}
		params.put("AddressingType", "public");
		String keyName = lc.getKeyName();
		if (keyName != null && !keyName.trim().equals("")) {
			params.put("KeyName", keyName);
		}

		if (lc.getSecurityGroup() != null) {
			for(int i = 0; i < lc.getSecurityGroup().size(); i++) {
				params.put("SecurityGroup." + (i + 1), lc.getSecurityGroup().get(i));
			}
		}
		params.put("InstanceType", lc.getInstanceType().getTypeId());
		if (lc.getAvailabilityZone() != null && !lc.getAvailabilityZone().trim().equals("")) {
			params.put("Placement.AvailabilityZone", lc.getAvailabilityZone());
		}
		if (lc.getKernelId() != null && !lc.getKernelId().trim().equals("")) {
			params.put("KernelId", lc.getKernelId());
		}
		if (lc.getRamdiskId() != null && !lc.getRamdiskId().trim().equals("")) {
			params.put("RamdiskId", lc.getRamdiskId());
		}
		if (lc.getBlockDevicemappings() != null) {
			for(int i = 0; i < lc.getBlockDevicemappings().size(); i++) {
				BlockDeviceMapping bdm = lc.getBlockDevicemappings().get(i);
				params.put("BlockDeviceMapping." + (i + 1) + ".VirtualName",
				bdm.getVirtualName());
				params.put("BlockDeviceMapping." + (i + 1) + ".DeviceName",
				bdm.getDeviceName());
			}
		}
		if (lc.isMonitoring()) {
			params.put("Monitoring.Enabled", "true");
		}

		GetMethod method = new GetMethod();
		try {
			RunInstancesResponse response =
					makeRequestInt(method, "RunInstances", params, RunInstancesResponse.class);
			ReservationDescription res = new ReservationDescription(response.getRequestId(),
															response.getOwnerId(),
															response.getReservationId());
			GroupSetType grp_set = response.getGroupSet();
			Iterator groups_iter = grp_set.getItems().iterator();
			while (groups_iter.hasNext()) {
				GroupItemType rsp_item = (GroupItemType) groups_iter.next();
				res.addGroup(rsp_item.getGroupId());
			}
			RunningInstancesSetType set = response.getInstancesSet();
			Iterator instances_iter = set.getItems().iterator();
			while (instances_iter.hasNext()) {
				RunningInstancesItemType rsp_item = (RunningInstancesItemType) instances_iter
													.next();
				res.addInstance(rsp_item.getImageId(),
								rsp_item.getInstanceId(),
								rsp_item.getPrivateDnsName(),
								rsp_item.getDnsName(),
								rsp_item.getInstanceState(),
								rsp_item.getReason(),
								rsp_item.getKeyName(),
								rsp_item.getLaunchTime().toGregorianCalendar(),
								InstanceType.getTypeFromString(rsp_item.getInstanceType()),
								rsp_item.getPlacement().getAvailabilityZone(),
								rsp_item.getKernelId(), rsp_item.getRamdiskId(),
								rsp_item.getPlatform(),
								rsp_item.getMonitoring().getState().equals("true"));
			}
			return res;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Terminates a selection of running instances.
	 * 
	 * @param instanceIds An array of instances ({@link com.xerox.amazonws.ec2.ReservationDescription.Instance#instanceId}.
	 * @return A list of {@link TerminatingInstanceDescription} instances.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<TerminatingInstanceDescription> terminateInstances(String[] instanceIds) throws EC2Exception {
		return this.terminateInstances(Arrays.asList(instanceIds));
	}

	/**
	 * Terminates a selection of running instances.
	 * 
	 * @param instanceIds A list of instances ({@link com.xerox.amazonws.ec2.ReservationDescription.Instance#instanceId}.
	 * @return A list of {@link TerminatingInstanceDescription} instances.
	 * @throws EC2Exception wraps checked exceptions
	 * TODO: need to return request id
	 */
	public List<TerminatingInstanceDescription> terminateInstances(List<String> instanceIds)
			throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<instanceIds.size(); i++) {
			params.put("InstanceId."+(i+1), instanceIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			TerminateInstancesResponse response =
					makeRequestInt(method, "TerminateInstances", params, TerminateInstancesResponse.class);
			response.getInstancesSet();
			List<TerminatingInstanceDescription> res =
						new ArrayList<TerminatingInstanceDescription>();
			TerminateInstancesResponseInfoType set = response.getInstancesSet();
			Iterator instances_iter = set.getItems().iterator();
			while (instances_iter.hasNext()) {
				TerminateInstancesResponseItemType rsp_item =
						(TerminateInstancesResponseItemType) instances_iter.next();
				res.add(new TerminatingInstanceDescription(
						rsp_item.getInstanceId(), rsp_item.getPreviousState()
								.getName(), rsp_item.getPreviousState().getCode(),
						rsp_item.getShutdownState().getName(), rsp_item
								.getShutdownState().getCode()));
			}
			return res;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Gets a list of running instances.
	 * <p>
	 * If the array of instance IDs is empty then a list of all instances owned
	 * by the caller will be returned. Otherwise the list will contain
	 * information for the requested instances only.
	 * 
	 * @param instanceIds An array of instances ({@link com.xerox.amazonws.ec2.ReservationDescription.Instance#instanceId}.
	 * @return A list of {@link com.xerox.amazonws.ec2.ReservationDescription} instances.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ReservationDescription> describeInstances(String[] instanceIds) throws EC2Exception {
		return this.describeInstances(Arrays.asList(instanceIds));
	}

	/**
	 * Gets a list of running instances.
	 * <p>
	 * If the list of instance IDs is empty then a list of all instances owned
	 * by the caller will be returned. Otherwise the list will contain
	 * information for the requested instances only.
	 * 
	 * @param instanceIds A list of instances ({@link com.xerox.amazonws.ec2.ReservationDescription.Instance#instanceId}.
	 * @return A list of {@link com.xerox.amazonws.ec2.ReservationDescription} instances.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ReservationDescription> describeInstances(List<String> instanceIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<instanceIds.size(); i++) {
			params.put("InstanceId."+(i+1), instanceIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			DescribeInstancesResponse response =
					makeRequestInt(method, "DescribeInstances", params, DescribeInstancesResponse.class);
			List<ReservationDescription> result = new ArrayList<ReservationDescription>();
			ReservationSetType res_set = response.getReservationSet();
            for (ReservationInfoType item : res_set.getItems()) {
                ReservationDescription res = new ReservationDescription(response.getRequestId(),
								item.getOwnerId(),
								item.getReservationId());
                GroupSetType grp_set = item.getGroupSet();
                for (GroupItemType rsp_item : grp_set.getItems()) {
                    res.addGroup(rsp_item.getGroupId());
                }
                RunningInstancesSetType set = item.getInstancesSet();
                for (RunningInstancesItemType rsp_item : set.getItems()) {
                    res.addInstance(rsp_item.getImageId(),
                            rsp_item.getInstanceId(),
                            rsp_item.getPrivateDnsName(),
                            rsp_item.getDnsName(),
                            rsp_item.getInstanceState(),
                            rsp_item.getReason(),
                            rsp_item.getKeyName(),
                            rsp_item.getLaunchTime().toGregorianCalendar(),
                            InstanceType.getTypeFromString(rsp_item.getInstanceType()),
                            rsp_item.getPlacement().getAvailabilityZone(),
                            rsp_item.getKernelId(), rsp_item.getRamdiskId(),
							rsp_item.getPlatform(),
							rsp_item.getMonitoring().getState().equals("true"));
                }
                result.add(res);
            }
			return result;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Reboot a selection of running instances.
	 * 
	 * @param instanceIds A list of instances ({@link com.xerox.amazonws.ec2.ReservationDescription.Instance#instanceId}.
	 * @throws EC2Exception wraps checked exceptions
	 * TODO: need to return request id
	 */
	public void rebootInstances(String [] instanceIds) throws EC2Exception {
		this.rebootInstances(Arrays.asList(instanceIds));
	}

	/**
	 * Reboot a selection of running instances.
	 * 
	 * @param instanceIds A list of instances ({@link com.xerox.amazonws.ec2.ReservationDescription.Instance#instanceId}.
	 * @throws EC2Exception wraps checked exceptions
	 * TODO: need to return request id
	 */
	public void rebootInstances(List<String> instanceIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<instanceIds.size(); i++) {
			params.put("InstanceId."+(i+1), instanceIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			RebootInstancesResponse response =
					makeRequestInt(method, "RebootInstances", params, RebootInstancesResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not reboot instances. No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Get an instance's console output.
	 *
	 * @param instanceId An instance's id ({@link com.xerox.amazonws.ec2.ReservationDescription.Instance#instanceId}.
	 * @return ({@link ConsoleOutput})
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ConsoleOutput getConsoleOutput(String instanceId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("InstanceId", instanceId);
		GetMethod method = new GetMethod();
		try {
			GetConsoleOutputResponse response =
					makeRequestInt(method, "GetConsoleOutput", params, GetConsoleOutputResponse.class);
			return new ConsoleOutput(response.getRequestId(), response.getInstanceId(),
				response.getTimestamp().toGregorianCalendar(),
				new String(Base64.decodeBase64(response.getOutput().getBytes())));
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Creates a security group.
	 * 
	 * @param name The name of the security group. 
	 * @param desc The description of the security group.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void createSecurityGroup(String name, String desc) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", name);
		params.put("GroupDescription", desc);
		GetMethod method = new GetMethod();
		try {
			CreateSecurityGroupResponse response =
					makeRequestInt(method, "CreateSecurityGroup", params, CreateSecurityGroupResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not create security group : "+name+". No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Deletes a security group. 
	 *
	 * @param name The name of the security group. 
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void deleteSecurityGroup(String name) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", name);
		GetMethod method = new GetMethod();
		try {
			DeleteSecurityGroupResponse response =
					makeRequestInt(method, "DeleteSecurityGroup", params, DeleteSecurityGroupResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not delete security group : "+name+". No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Gets a list of security groups and their associated permissions.  
	 *
	 * @param groupNames An array of groups to describe.
	 * @return A list of groups ({@link GroupDescription}.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<GroupDescription> describeSecurityGroups(String[] groupNames)
			throws EC2Exception {
		return describeSecurityGroups(Arrays.asList(groupNames));
	}

	/**
	 * Gets a list of security groups and their associated permissions.  
	 * 
	 * @param groupNames A list of groups to describe.
	 * @return A list of groups ({@link GroupDescription}.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<GroupDescription> describeSecurityGroups(List<String> groupNames)
			throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<groupNames.size(); i++) {
			params.put("GroupName."+(i+1), groupNames.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			DescribeSecurityGroupsResponse response =
					makeRequestInt(method, "DescribeSecurityGroups", params, DescribeSecurityGroupsResponse.class);
			List<GroupDescription> result = new ArrayList<GroupDescription>();
			SecurityGroupSetType rsp_set = response.getSecurityGroupInfo();
			Iterator set_iter = rsp_set.getItems().iterator();
			while (set_iter.hasNext()) {
				SecurityGroupItemType item = (SecurityGroupItemType) set_iter
						.next();
				GroupDescription group = new GroupDescription(item.getGroupName(),
						item.getGroupDescription(), item.getOwnerId());
				IpPermissionSetType perms = item.getIpPermissions();
				Iterator perm_iter = perms.getItems().iterator();
				while (perm_iter.hasNext()) {
					IpPermissionType perm = (IpPermissionType) perm_iter.next();
					GroupDescription.IpPermission group_perms = group
							.addPermission(perm.getIpProtocol(),
									perm.getFromPort(), perm.getToPort());

					Iterator group_iter = perm.getGroups().getItems().iterator();
					while (group_iter.hasNext()) {
						UserIdGroupPairType uid_group = (UserIdGroupPairType) group_iter
								.next();
						group_perms.addUserGroupPair(uid_group.getUserId(),
								uid_group.getGroupName());
					}
					Iterator iprange_iter = perm.getIpRanges().getItems().iterator();
					while (iprange_iter.hasNext()) {
						IpRangeItemType range = (IpRangeItemType) iprange_iter
								.next();
						group_perms.addIpRange(range.getCidrIp());
					}
				}
				result.add(group);
			}
			return result;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Adds incoming permissions to a security group.
	 * 
	 * @param groupName name of group to modify
	 * @param secGroupName name of security group to authorize access to
	 * @param secGroupOwnerId owner of security group to authorize access to
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void authorizeSecurityGroupIngress(String groupName, String secGroupName,
											String secGroupOwnerId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", groupName);
		params.put("SourceSecurityGroupOwnerId", secGroupOwnerId);
		params.put("SourceSecurityGroupName", secGroupName);
		GetMethod method = new GetMethod();
		try {
			AuthorizeSecurityGroupIngressResponse response =
					makeRequestInt(method, "AuthorizeSecurityGroupIngress", params, AuthorizeSecurityGroupIngressResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not authorize security ingress : "+groupName+". No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Adds incoming permissions to a security group.
	 * 
	 * @param groupName name of group to modify
	 * @param ipProtocol protocol to authorize (tcp, udp, icmp)
	 * @param fromPort bottom of port range to authorize
	 * @param toPort top of port range to authorize
	 * @param cidrIp CIDR IP range to authorize (i.e. 0.0.0.0/0)
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void authorizeSecurityGroupIngress(String groupName, String ipProtocol,
											int fromPort, int toPort,
											String cidrIp) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", groupName);
		params.put("IpProtocol", ipProtocol);
		params.put("FromPort", ""+fromPort);
		params.put("ToPort", ""+toPort);
		params.put("CidrIp", cidrIp);
		GetMethod method = new GetMethod();
		try {
			AuthorizeSecurityGroupIngressResponse response =
					makeRequestInt(method, "AuthorizeSecurityGroupIngress", params, AuthorizeSecurityGroupIngressResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not authorize security ingress : "+groupName+". No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Revokes incoming permissions from a security group.
	 * 
	 * @param groupName name of group to modify
	 * @param secGroupName name of security group to revoke access from
	 * @param secGroupOwnerId owner of security group to revoke access from
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void revokeSecurityGroupIngress(String groupName, String secGroupName,
											String secGroupOwnerId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", groupName);
		params.put("SourceSecurityGroupOwnerId", secGroupOwnerId);
		params.put("SourceSecurityGroupName", secGroupName);
		GetMethod method = new GetMethod();
		try {
			RevokeSecurityGroupIngressResponse response =
					makeRequestInt(method, "RevokeSecurityGroupIngress", params, RevokeSecurityGroupIngressResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not revoke security ingress : "+groupName+". No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Revokes incoming permissions from a security group.
	 * 
	 * @param groupName name of group to modify
	 * @param ipProtocol protocol to revoke (tcp, udp, icmp)
	 * @param fromPort bottom of port range to revoke
	 * @param toPort top of port range to revoke
	 * @param cidrIp CIDR IP range to revoke (i.e. 0.0.0.0/0)
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void revokeSecurityGroupIngress(String groupName, String ipProtocol,
											int fromPort, int toPort,
											String cidrIp) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", groupName);
		params.put("IpProtocol", ipProtocol);
		params.put("FromPort", ""+fromPort);
		params.put("ToPort", ""+toPort);
		params.put("CidrIp", cidrIp);
		GetMethod method = new GetMethod();
		try {
			RevokeSecurityGroupIngressResponse response =
					makeRequestInt(method, "RevokeSecurityGroupIngress", params, RevokeSecurityGroupIngressResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not revoke security ingress : "+groupName+". No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}


	/**
	 * Creates a public/private keypair.
	 * 
	 * @param keyName Name of the keypair.
	 * @return A keypair description ({@link KeyPairInfo}).
	 * @throws EC2Exception wraps checked exceptions
	 * TODO: need to return request id
	 */
	public KeyPairInfo createKeyPair(String keyName) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("KeyName", keyName);
		GetMethod method = new GetMethod();
		try {
			CreateKeyPairResponse response =
					makeRequestInt(method, "CreateKeyPair", params, CreateKeyPairResponse.class);
			return new KeyPairInfo(response.getKeyName(),
									response.getKeyFingerprint(),
									response.getKeyMaterial());
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Lists public/private keypairs.
	 * 
	 * @param keyIds An array of keypairs.
	 * @return A list of keypair descriptions ({@link KeyPairInfo}).
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<KeyPairInfo> describeKeyPairs(String[] keyIds) throws EC2Exception {
		return describeKeyPairs(Arrays.asList(keyIds));
	}

	/**
	 * Lists public/private keypairs. NOTE: the KeyPairInfo.getMaterial() method will return null
	 * because this API call doesn't return the keypair material.
	 * 
	 * @param keyIds A list of keypairs.
	 * @return A list of keypair descriptions ({@link KeyPairInfo}).
	 * @throws EC2Exception wraps checked exceptions
	 * TODO: need to return request id
	 */
	public List<KeyPairInfo> describeKeyPairs(List<String> keyIds)
			throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<keyIds.size(); i++) {
			params.put("KeyName."+(i+1), keyIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			DescribeKeyPairsResponse response =
					makeRequestInt(method, "DescribeKeyPairs", params, DescribeKeyPairsResponse.class);
			List<KeyPairInfo> result = new ArrayList<KeyPairInfo>();
			DescribeKeyPairsResponseInfoType set = response.getKeySet();
			Iterator set_iter = set.getItems().iterator();
			while (set_iter.hasNext()) {
				DescribeKeyPairsResponseItemType item = (DescribeKeyPairsResponseItemType) set_iter.next();
				result.add(new KeyPairInfo(item.getKeyName(), item.getKeyFingerprint(), null));
			}
			return result;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Deletes a public/private keypair.
	 * 
	 * @param keyName Name of the keypair.
	 * @throws EC2Exception wraps checked exceptions
	 * TODO: need to return request id
	 */
	public void deleteKeyPair(String keyName) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("KeyName", keyName);
		GetMethod method = new GetMethod();
		try {
			DeleteKeyPairResponse response =
					makeRequestInt(method, "DeleteKeyPair", params, DeleteKeyPairResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not delete keypair : "+keyName+". No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Enumerates image list attribute operation types.
	 */
	public enum ImageListAttributeOperationType {
		add,
		remove
	}
	
	/**
	 * Modifies an attribute by the given items with the given operation. 
	 *
	 * @param imageId The ID of the AMI to modify the attributes for.
	 * @param attribute The name of the attribute to change.
	 * @param operationType The name of the operation to change. May be add or remove.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void modifyImageAttribute(String imageId, ImageListAttribute attribute,
								ImageListAttributeOperationType operationType) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageId", imageId);
		if (attribute.getType().equals(ImageAttribute.ImageAttributeType.launchPermission)) {
			params.put("Attribute", "launchPermission");
			switch (operationType) {
				case add: params.put("OperationType", "add"); break;
				case remove: params.put("OperationType", "remove"); break;
				default:
					throw new IllegalArgumentException("Unknown attribute operation.");
			}
		}
		else if (attribute.getType().equals(ImageAttribute.ImageAttributeType.productCodes)) {
			params.put("Attribute", "productCodes");
		}

		int gNum = 1;
		int iNum = 1;
		int pNum = 1;
		for(ImageListAttributeItem item : attribute.getImageListAttributeItems()) {
			switch (item.getType()) {
				case group: params.put("UserGroup."+gNum, item.getValue()); gNum++; break;
				case userId: params.put("UserId."+iNum, item.getValue()); iNum++; break;
				case productCode: params.put("ProductCode."+pNum, item.getValue()); pNum++; break;
				default:
					throw new IllegalArgumentException("Unknown item type.");
			}
		}
		GetMethod method = new GetMethod();
		try {
			ModifyImageAttributeResponse response =
					makeRequestInt(method, "ModifyImageAttribute", params, ModifyImageAttributeResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not reset image attribute. No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * Resets an attribute on an AMI.
	 *
	 * @param imageId The AMI to reset the attribute on.
	 * @param imageAttribute The attribute type to reset.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void resetImageAttribute(String imageId, ImageAttribute.ImageAttributeType imageAttribute) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageId", imageId);
		if (imageAttribute.equals(ImageAttribute.ImageAttributeType.launchPermission)) {
			params.put("Attribute", "launchPermission");
		}
		else if (imageAttribute.equals(ImageAttribute.ImageAttributeType.productCodes)) {
			throw new IllegalArgumentException("Cannot reset productCodes attribute");
		}
		GetMethod method = new GetMethod();
		try {
			ResetImageAttributeResponse response =
					makeRequestInt(method, "ResetImageAttribute", params, ResetImageAttributeResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not reset image attribute. No reason given.");
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * Describes an attribute of an AMI.
	 *
	 * @param imageId The AMI for which the attribute is described.
	 * @param imageAttribute The attribute type to describe.
	 * @return An object containing the imageId and a list of list attribute item types and values.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public DescribeImageAttributeResult describeImageAttribute(String imageId,
						ImageAttribute.ImageAttributeType imageAttribute) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageId", imageId);
		if (imageAttribute.equals(ImageAttribute.ImageAttributeType.launchPermission)) {
			params.put("Attribute", "launchPermission");
		}
		else if (imageAttribute.equals(ImageAttribute.ImageAttributeType.productCodes)) {
			params.put("Attribute", "productCodes");
		}
		GetMethod method = new GetMethod();
		try {
			DescribeImageAttributeResponse response =
					makeRequestInt(method, "DescribeImageAttribute", params, DescribeImageAttributeResponse.class);
			ImageListAttribute attribute = null;
			if (response.getLaunchPermission() != null) {
				LaunchPermissionListType list = response.getLaunchPermission();
				attribute = new LaunchPermissionAttribute();
				java.util.ListIterator i = list.getItems().listIterator();
				while (i.hasNext()) {
					LaunchPermissionItemType item = (LaunchPermissionItemType) i.next();
					if (item.getGroup() != null) {
						attribute.addImageListAttributeItem(ImageListAttribute.ImageListAttributeItemType.group,
													item.getGroup());
					} else if (item.getUserId() != null) {
						attribute.addImageListAttributeItem(ImageListAttribute.ImageListAttributeItemType.userId,
													item.getUserId());
					}
				}
			}
			else if (response.getProductCodes() != null) {
				ProductCodeListType list = response.getProductCodes();
				attribute = new ProductCodesAttribute();
				java.util.ListIterator i = list.getItems().listIterator();
				while (i.hasNext()) {
					ProductCodeItemType item = (ProductCodeItemType) i.next();
					if (item.getProductCode() != null) {
						attribute.addImageListAttributeItem(ImageListAttribute.ImageListAttributeItemType.productCode,
													item.getProductCode());
					}
				}
			}
			ArrayList<String> codes = new ArrayList<String>();
			ProductCodeListType set = response.getProductCodes();
			if (set != null) {
				for (ProductCodeItemType code : set.getItems()) {
					codes.add(code.getProductCode());
				}
			}
			NullableAttributeValueType val = response.getKernel();
			String kernel = (val != null)?val.getValue():"";
			val = response.getRamdisk();
			String ramdisk = (val != null)?val.getValue():"";
			ArrayList<BlockDeviceMapping> bdm = new ArrayList<BlockDeviceMapping>();
			BlockDeviceMappingType bdmSet = response.getBlockDeviceMapping();
			if (bdmSet != null) {
				for (BlockDeviceMappingItemType mapping : bdmSet.getItems()) {
					bdm.add(new BlockDeviceMapping(mapping.getVirtualName(), mapping.getDeviceName()));
				}
			}

			return new DescribeImageAttributeResult(response.getImageId(), attribute, codes, kernel, ramdisk, bdm);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Returns true if the productCode is associated with the instance.
	 *
	 * @param instanceId An instance's id ({@link com.xerox.amazonws.ec2.ReservationDescription.Instance#instanceId}.
	 * @param productCode the code for the project you registered with AWS
	 * @return null if no relationship exists, otherwise information about the owner
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ProductInstanceInfo confirmProductInstance(String instanceId, String productCode) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("InstanceId", instanceId);
		params.put("ProductCode", productCode);
		GetMethod method = new GetMethod();
		try {
			ConfirmProductInstanceResponse response =
					makeRequestInt(method, "ConfirmProductInstance", params, ConfirmProductInstanceResponse.class);
			if (response.isReturn()) {
				return new ProductInstanceInfo(instanceId, productCode, response.getOwnerId());
			}
			else return null;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Returns a list of availability zones and their status.
	 *
	 * @param zones a list of zones to limit the results, or null
	 * @return a list of zones and their availability
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<AvailabilityZone> describeAvailabilityZones(List<String> zones) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		if (zones != null && zones.size() > 0)  {
			for (int i=0 ; i<zones.size(); i++) {
				params.put("ZoneName."+(i+1), zones.get(i));
			}
		}
		GetMethod method = new GetMethod();
		try {
			DescribeAvailabilityZonesResponse response =
					makeRequestInt(method, "DescribeAvailabilityZones", params, DescribeAvailabilityZonesResponse.class);
			List<AvailabilityZone> ret = new ArrayList<AvailabilityZone>();
			AvailabilityZoneSetType set = response.getAvailabilityZoneInfo();
			Iterator set_iter = set.getItems().iterator();
			while (set_iter.hasNext()) {
				AvailabilityZoneItemType item = (AvailabilityZoneItemType) set_iter.next();
				ret.add(new AvailabilityZone(item.getZoneName(), item.getZoneState()));
			}
			return ret;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Returns a list of addresses associated with this account.
	 *
	 * @param addresses a list of zones to limit the results, or null
	 * @return a list of addresses and their associated instance
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<AddressInfo> describeAddresses(List<String> addresses) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		if (addresses != null && addresses.size() > 0)  {
			for (int i=0 ; i<addresses.size(); i++) {
				params.put("PublicIp."+(i+1), addresses.get(i));
			}
		}
		GetMethod method = new GetMethod();
		try {
			DescribeAddressesResponse response =
					makeRequestInt(method, "DescribeAddresses", params, DescribeAddressesResponse.class);
			List<AddressInfo> ret = new ArrayList<AddressInfo>();
			DescribeAddressesResponseInfoType set = response.getAddressesSet();
			Iterator set_iter = set.getItems().iterator();
			while (set_iter.hasNext()) {
				DescribeAddressesResponseItemType item = (DescribeAddressesResponseItemType) set_iter.next();
				ret.add(new AddressInfo(item.getPublicIp(), item.getInstanceId()));
			}
			return ret;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Allocates an address for this account.
	 *
	 * @return the new address allocated
	 * @throws EC2Exception wraps checked exceptions
	 */
	public String allocateAddress() throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		GetMethod method = new GetMethod();
		try {
			AllocateAddressResponse response =
					makeRequestInt(method, "AllocateAddress", params, AllocateAddressResponse.class);
			return response.getPublicIp();
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Associates an address with an instance.
	 *
	 * @param instanceId the instance
	 * @param publicIp the ip address to associate
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void associateAddress(String instanceId, String publicIp) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("InstanceId", instanceId);
		params.put("PublicIp", publicIp);
		GetMethod method = new GetMethod();
		try {
			AssociateAddressResponse response =
					makeRequestInt(method, "AssociateAddress", params, AssociateAddressResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not associate address with instance (no reason given).");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Disassociates an address with an instance.
	 *
	 * @param publicIp the ip address to disassociate
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void disassociateAddress(String publicIp) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("PublicIp", publicIp);
		GetMethod method = new GetMethod();
		try {
			DisassociateAddressResponse response =
					makeRequestInt(method, "DisassociateAddress", params, DisassociateAddressResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not disassociate address with instance (no reason given).");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Releases an address
	 *
	 * @param publicIp the ip address to release
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void releaseAddress(String publicIp) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("PublicIp", publicIp);
		GetMethod method = new GetMethod();
		try {
			ReleaseAddressResponse response =
					makeRequestInt(method, "ReleaseAddress", params, ReleaseAddressResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not release address (no reason given).");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Creates an EBS volume either by size, or from a snapshot. The zone must be the same as
	 * that of the instance you wish to attach it to.
	 *
	 * @param size the size of the volume in gigabytes
	 * @param snapshotId the snapshot from which to create the new volume
	 * @param zoneName the availability zone for the new volume
	 * @return information about the volume
	 * @throws EC2Exception wraps checked exceptions
	 */
	public VolumeInfo createVolume(String size, String snapshotId, String zoneName) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		if (size != null && !size.equals("")) {
			params.put("Size", size);
		}
		params.put("SnapshotId", (snapshotId==null)?"":snapshotId);
		params.put("AvailabilityZone", zoneName);
		GetMethod method = new GetMethod();
		try {
			CreateVolumeResponse response =
					makeRequestInt(method, "CreateVolume", params, CreateVolumeResponse.class);
			return new VolumeInfo(response.getVolumeId(), response.getSize(),
								response.getSnapshotId(), response.getAvailabilityZone(), response.getStatus(),
								response.getCreateTime().toGregorianCalendar());
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Deletes the EBS volume.
	 *
	 * @param volumeId the id of the volume to be deleted
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void deleteVolume(String volumeId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("VolumeId", volumeId);
		GetMethod method = new GetMethod();
		try {
			DeleteVolumeResponse response =
					makeRequestInt(method, "DeleteVolume", params, DeleteVolumeResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not release delete volume (no reason given).");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Gets a list of EBS volumes for this account.
	 * <p>
	 * If the array of volume IDs is empty then a list of all volumes owned
	 * by the caller will be returned. Otherwise the list will contain
	 * information for the requested volumes only.
	 * 
	 * @param volumeIds An array of volumes ({@link com.xerox.amazonws.ec2.VolumeInfo}.
	 * @return A list of {@link com.xerox.amazonws.ec2.VolumeInfo} volumes.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<VolumeInfo> describeVolumes(String[] volumeIds) throws EC2Exception {
		return this.describeVolumes(Arrays.asList(volumeIds));
	}

	/**
	 * Gets a list of EBS volumes for this account.
	 * <p>
	 * If the list of volume IDs is empty then a list of all volumes owned
	 * by the caller will be returned. Otherwise the list will contain
	 * information for the requested volumes only.
	 * 
	 * @param volumeIds A list of volumes ({@link com.xerox.amazonws.ec2.VolumeInfo}.
	 * @return A list of {@link com.xerox.amazonws.ec2.VolumeInfo} volumes.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<VolumeInfo> describeVolumes(List<String> volumeIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<volumeIds.size(); i++) {
			params.put("VolumeId."+(i+1), volumeIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			DescribeVolumesResponse response =
					makeRequestInt(method, "DescribeVolumes", params, DescribeVolumesResponse.class);
			List<VolumeInfo> result = new ArrayList<VolumeInfo>();
			DescribeVolumesSetResponseType res_set = response.getVolumeSet();
			Iterator reservations_iter = res_set.getItems().iterator();
			while (reservations_iter.hasNext()) {
				DescribeVolumesSetItemResponseType item = (DescribeVolumesSetItemResponseType) reservations_iter.next();
				VolumeInfo vol = new VolumeInfo(item.getVolumeId(), item.getSize(),
									item.getSnapshotId(), item.getAvailabilityZone(), item.getStatus(),
									item.getCreateTime().toGregorianCalendar());
				AttachmentSetResponseType set = item.getAttachmentSet();
				Iterator attachments_iter = set.getItems().iterator();
				while (attachments_iter.hasNext()) {
					AttachmentSetItemResponseType as_item = (AttachmentSetItemResponseType) attachments_iter
														.next();
					vol.addAttachmentInfo(as_item.getVolumeId(),
									as_item.getInstanceId(),
									as_item.getDevice(),
									as_item.getStatus(),
									as_item.getAttachTime().toGregorianCalendar());
				}
				result.add(vol);
			}
			return result;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Attaches an EBS volume to an instance.
	 *
	 * @param volumeId the id of the volume
	 * @param instanceId the id of the instance
	 * @param device the device name for the attached volume
	 * @return the information about this attachment
	 * @throws EC2Exception wraps checked exceptions
	 */
	public AttachmentInfo attachVolume(String volumeId, String instanceId, String device) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("VolumeId", volumeId);
		params.put("InstanceId", instanceId);
		params.put("Device", device);
		GetMethod method = new GetMethod();
		try {
			AttachVolumeResponse response =
					makeRequestInt(method, "AttachVolume", params, AttachVolumeResponse.class);
			return new AttachmentInfo(response.getVolumeId(), response.getInstanceId(),
								response.getDevice(), response.getStatus(),
								response.getAttachTime().toGregorianCalendar());
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Detaches an EBS volume from an instance.
	 *
	 * @param volumeId the id of the volume
	 * @param instanceId the id of the instance
	 * @param device the device name for the attached volume
	 * @param force if true, forces the detachment, only use if normal detachment fails
	 * @return the information about this attachment
	 * @throws EC2Exception wraps checked exceptions
	 */
	public AttachmentInfo detachVolume(String volumeId, String instanceId, String device, boolean force) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("VolumeId", volumeId);
		params.put("InstanceId", (instanceId==null)?"":instanceId);
		params.put("Device", (device==null)?"":device);
		params.put("Force", force?"true":"false");
		GetMethod method = new GetMethod();
		try {
			DetachVolumeResponse response =
					makeRequestInt(method, "DetachVolume", params, DetachVolumeResponse.class);
			return new AttachmentInfo(response.getVolumeId(), response.getInstanceId(),
								response.getDevice(), response.getStatus(),
								response.getAttachTime().toGregorianCalendar());
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Creates a snapshot of the EBS Volume.
	 *
	 * @param volumeId the id of the volume
	 * @return information about the snapshot
	 * @throws EC2Exception wraps checked exceptions
	 */
	public SnapshotInfo createSnapshot(String volumeId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("VolumeId", volumeId);
		GetMethod method = new GetMethod();
		try {
			CreateSnapshotResponse response =
					makeRequestInt(method, "CreateSnapshot", params, CreateSnapshotResponse.class);
			return new SnapshotInfo(response.getSnapshotId(), response.getVolumeId(),
								response.getStatus(),
								response.getStartTime().toGregorianCalendar(),
								response.getProgress());
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Deletes the snapshot.
	 *
	 * @param snapshotId the id of the snapshot
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void deleteSnapshot(String snapshotId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("SnapshotId", snapshotId);
		GetMethod method = new GetMethod();
		try {
			DeleteSnapshotResponse response =
					makeRequestInt(method, "DeleteSnapshot", params, DeleteSnapshotResponse.class);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not release delete snapshot (no reason given).");
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Gets a list of EBS snapshots for this account.
	 * <p>
	 * If the array of snapshot IDs is empty then a list of all snapshots owned
	 * by the caller will be returned. Otherwise the list will contain
	 * information for the requested snapshots only.
	 * 
	 * @param snapshotIds An array of snapshots ({@link com.xerox.amazonws.ec2.SnapshotInfo}.
	 * @return A list of {@link com.xerox.amazonws.ec2.VolumeInfo} volumes.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<SnapshotInfo> describeSnapshots(String[] snapshotIds) throws EC2Exception {
		return this.describeSnapshots(Arrays.asList(snapshotIds));
	}

	/**
	 * Gets a list of EBS snapshots for this account.
	 * <p>
	 * If the list of snapshot IDs is empty then a list of all snapshots owned
	 * by the caller will be returned. Otherwise the list will contain
	 * information for the requested snapshots only.
	 * 
	 * @param snapshotIds A list of snapshots ({@link com.xerox.amazonws.ec2.SnapshotInfo}.
	 * @return A list of {@link com.xerox.amazonws.ec2.VolumeInfo} volumes.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<SnapshotInfo> describeSnapshots(List<String> snapshotIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<snapshotIds.size(); i++) {
			params.put("SnapshotId."+(i+1), snapshotIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			DescribeSnapshotsResponse response =
					makeRequestInt(method, "DescribeSnapshots", params, DescribeSnapshotsResponse.class);
			List<SnapshotInfo> result = new ArrayList<SnapshotInfo>();
			DescribeSnapshotsSetResponseType res_set = response.getSnapshotSet();
			Iterator reservations_iter = res_set.getItems().iterator();
			while (reservations_iter.hasNext()) {
				DescribeSnapshotsSetItemResponseType item = (DescribeSnapshotsSetItemResponseType) reservations_iter.next();
				SnapshotInfo vol = new SnapshotInfo(item.getSnapshotId(), item.getVolumeId(),
									item.getStatus(),
									item.getStartTime().toGregorianCalendar(),
									item.getProgress());
				result.add(vol);
			}
			return result;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Returns a list of regions
	 *
	 * @param regions a list of regions to limit the results, or null
	 * @return a list of regions and endpoints
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<RegionInfo> describeRegions(List<String> regions) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		if (regions != null && regions.size() > 0)  {
			for (int i=0 ; i<regions.size(); i++) {
				params.put("Region."+(i+1), regions.get(i));
			}
		}
		GetMethod method = new GetMethod();
		try {
			DescribeRegionsResponse response =
					makeRequestInt(method, "DescribeRegions", params, DescribeRegionsResponse.class);
			List<RegionInfo> ret = new ArrayList<RegionInfo>();
			RegionSetType set = response.getRegionInfo();
			Iterator set_iter = set.getItems().iterator();
			while (set_iter.hasNext()) {
				RegionItemType item = (RegionItemType) set_iter.next();
				ret.add(new RegionInfo(item.getRegionName(), item.getRegionEndpoint()));
			}
			return ret;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Sets the region to use.
	 *
	 * @param region the region to use, from describeRegions()
	 */
	public void setRegion(RegionInfo region) {
		setServer(region.getUrl());
	}

	/**
	 * Sets the region Url to use.
	 *
	 * @param region the region Url to use from RegionInfo.getUrl()
	 */
	public void setRegionUrl(String regionUrl) {
		setServer(regionUrl);
	}

	/**
	 * Initiates bundling of an instance running Windows.
	 *
	 * @param instanceId the Id of the instance to bundle
	 * @param accessId the accessId of the owner of the S3 bucket
	 * @param bucketName the name of the S3 bucket in which the AMi will be stored
	 * @param prefix the prefix to append to the AMI
	 * @param policy an UploadPolicy object containing policy parameters
	 * @return information about the bundle task
	 * @throws EC2Exception wraps checked exceptions
	 */
	public BundleInstanceInfo bundleInstance(String instanceId, String accessId, String bucketName, String prefix, UploadPolicy policy) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("InstanceId", instanceId);
		params.put("Storage.S3.AWSAccessKeyId", accessId);
		params.put("Storage.S3.Bucket", bucketName);
		params.put("Storage.S3.Prefix", prefix);
		String jsonPolicy = policy.getPolicyString();
		params.put("Storage.S3.UploadPolicy", jsonPolicy);
		params.put("Storage.S3.UploadPolicySignature", encode(getSecretAccessKey(), jsonPolicy, false, "HmacSHA1"));
		GetMethod method = new GetMethod();
		try {
			BundleInstanceResponse response =
					makeRequestInt(method, "BundleInstance", params, BundleInstanceResponse.class);
			BundleInstanceTaskType task = response.getBundleInstanceTask();
			return new BundleInstanceInfo(response.getRequestId(), task.getInstanceId(), task.getBundleId(),
							task.getState(), task.getStartTime().toGregorianCalendar(),
							task.getUpdateTime().toGregorianCalendar(), task.getStorage(),
							task.getProgress(), task.getError());
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Cancel a bundling operation.
	 *
	 * @param bundleId the Id of the bundle task to cancel
	 * @return information about the cancelled task
	 * @throws EC2Exception wraps checked exceptions
	 */
	public BundleInstanceInfo cancelBundleInstance(String bundleId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("BundleId", bundleId);
		GetMethod method = new GetMethod();
		try {
			CancelBundleTaskResponse response =
					makeRequestInt(method, "CancelBundleTask", params, CancelBundleTaskResponse.class);
			BundleInstanceTaskType task = response.getBundleInstanceTask();
			return new BundleInstanceInfo(response.getRequestId(), task.getInstanceId(), task.getBundleId(),
							task.getState(), task.getStartTime().toGregorianCalendar(),
							task.getUpdateTime().toGregorianCalendar(), task.getStorage(),
							task.getProgress(), task.getError());
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Returns a list of current bundling tasks. An empty array causes all tasks to be returned.
	 *
	 * @param bundleIds the Ids of the bundle task to describe
	 * @return information about the cancelled task
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<BundleInstanceInfo> describeBundleTasks(String [] bundleIds) throws EC2Exception {
		return this.describeBundleTasks(Arrays.asList(bundleIds));
	}

	/**
	 * Returns a list of current bundling tasks. An empty list causes all tasks to be returned.
	 *
	 * @param bundleIds the Ids of the bundle task to describe
	 * @return information about the cancelled task
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<BundleInstanceInfo> describeBundleTasks(List<String> bundleIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<bundleIds.size(); i++) {
			params.put("BundleId."+(i+1), bundleIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			DescribeBundleTasksResponse response =
					makeRequestInt(method, "DescribeBundleTasks", params, DescribeBundleTasksResponse.class);
			List<BundleInstanceInfo> ret = new ArrayList<BundleInstanceInfo>();
			Iterator task_iter = response.getBundleInstanceTasksSet().getItems().iterator();
			while (task_iter.hasNext()) {
				BundleInstanceTaskType task = (BundleInstanceTaskType) task_iter.next();
				ret.add(new BundleInstanceInfo(response.getRequestId(), task.getInstanceId(), task.getBundleId(),
							task.getState(), task.getStartTime().toGregorianCalendar(),
							task.getUpdateTime().toGregorianCalendar(), task.getStorage(),
							task.getProgress(), task.getError()));
			}
			return ret;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Returns a list of Reserved Instance offerings that are available for purchase.
	 *
	 * @param instanceIds specific reserved instance offering ids to return
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ReservedInstances> describeReservedInstances(List<String> instanceIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		if (instanceIds != null) {
			for (int i=0 ; i<instanceIds.size(); i++) {
				params.put("ReservedInstanceId."+(i+1), instanceIds.get(i));
			}
		}
		GetMethod method = new GetMethod();
		try {
			DescribeReservedInstancesResponse response =
					makeRequestInt(method, "DescribeReservedInstances", params, DescribeReservedInstancesResponse.class);
			List<ReservedInstances> ret = new ArrayList<ReservedInstances>();
			Iterator task_iter = response.getReservedInstancesSet().getItems().iterator();
			while (task_iter.hasNext()) {
				DescribeReservedInstancesResponseSetItemType type =
						(DescribeReservedInstancesResponseSetItemType) task_iter.next();
				ret.add(new ReservedInstances(type.getReservedInstancesId(),
							InstanceType.getTypeFromString(type.getInstanceType()),
							type.getAvailabilityZone(),
							type.getDuration(), type.getFixedPrice(), type.getUsagePrice(),
							type.getProductDescription(),
							type.getInstanceCount().intValue(), type.getState()));
			}
			return ret;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * Returns a list of Reserved Instance offerings that are available for purchase.
	 *
	 * @param offeringIds specific reserved instance offering ids to return
	 * @param instanceType the type of instance offering to be returned
	 * @param availabilityZone the availability zone to get offerings for
	 * @param productDescription limit results to those with a matching product description
	 * @return a list of product descriptions
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ProductDescription> describeReservedInstancesOfferings(List<String> offeringIds,
								InstanceType instanceType, String availabilityZone,
								String productDescription) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		if (offeringIds != null) {
			for (int i=0 ; i<offeringIds.size(); i++) {
				params.put("ReservedInstancesOfferingId."+(i+1), offeringIds.get(i));
			}
		}
		if (instanceType != null) {
			params.put("InstanceType", instanceType.getTypeId());
		}
		if (availabilityZone != null) {
			params.put("AvailabilityZone", availabilityZone);
		}
		if (productDescription != null) {
			params.put("ProductDescription", productDescription);
		}
		GetMethod method = new GetMethod();
		try {
			DescribeReservedInstancesOfferingsResponse response =
					makeRequestInt(method, "DescribeReservedInstancesOfferings", params, DescribeReservedInstancesOfferingsResponse.class);
			List<ProductDescription> ret = new ArrayList<ProductDescription>();
			Iterator task_iter = response.getReservedInstancesOfferingsSet().getItems().iterator();
			while (task_iter.hasNext()) {
				DescribeReservedInstancesOfferingsResponseSetItemType type =
						(DescribeReservedInstancesOfferingsResponseSetItemType) task_iter.next();
				ret.add(new ProductDescription(type.getReservedInstancesOfferingId(),
							InstanceType.getTypeFromString(type.getInstanceType()),
							type.getAvailabilityZone(),
							type.getDuration(), type.getFixedPrice(), type.getUsagePrice(),
							type.getProductDescription()));
			}
			return ret;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * This method purchases a reserved instance offering.
	 *
	 * NOTE: Use With Caution!!! This can cost a lot of money!
	 *
	 * @param offeringId the id of the offering to purchase
	 * @param instanceCount the number of instances to reserve
	 * @return id of reserved instances
	 * @throws EC2Exception wraps checked exceptions
	 */
	public String purchaseReservedInstancesOffering(String offeringId, int instanceCount) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ReservedInstancesOfferingId", offeringId);
		params.put("InstanceCount", ""+instanceCount);
		GetMethod method = new GetMethod();
		try {
			PurchaseReservedInstancesOfferingResponse response =
					makeRequestInt(method, "PurchaseReservedInstancesOffering", params, PurchaseReservedInstancesOfferingResponse.class);
			return response.getReservedInstancesId();
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * This method enables monitoring for some instances
	 *
	 * @param instanceIds the id of the instances to enable monitoring for
	 * @return information about the monitoring state of those instances
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<MonitoredInstanceInfo> monitorInstances(List<String> instanceIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<instanceIds.size(); i++) {
			params.put("InstanceId."+(i+1), instanceIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			MonitorInstancesResponseType response =
					makeRequestInt(method, "MonitorInstances", params, MonitorInstancesResponseType.class);
			List<MonitoredInstanceInfo> ret = new ArrayList<MonitoredInstanceInfo>();
			for (MonitorInstancesResponseSetItemType item : response.getInstancesSet().getItems()) {
				ret.add(new MonitoredInstanceInfo(item.getInstanceId(),
								item.getMonitoring().getState()));
			}
			return ret;
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * This method disables monitoring for some instances
	 *
	 * @param instanceIds the id of the instances to disable monitoring for
	 * @return information about the monitoring state of those instances
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<MonitoredInstanceInfo> unmonitorInstances(List<String> instanceIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<instanceIds.size(); i++) {
			params.put("InstanceId."+(i+1), instanceIds.get(i));
		}
		GetMethod method = new GetMethod();
		try {
			MonitorInstancesResponseType response =
					makeRequestInt(method, "UnmonitorInstances", params, MonitorInstancesResponseType.class);
			List<MonitoredInstanceInfo> ret = new ArrayList<MonitoredInstanceInfo>();
			for (MonitorInstancesResponseSetItemType item : response.getInstancesSet().getItems()) {
				ret.add(new MonitoredInstanceInfo(item.getInstanceId(),
								item.getMonitoring().getState()));
			}
			return ret;
		} finally {
			method.releaseConnection();
		}
	}

	protected <T> T makeRequestInt(HttpMethodBase method, String action, Map<String, String> params, Class<T> respType)
		throws EC2Exception {
		try {
			return makeRequest(method, action, params, respType);
		} catch (AWSException ex) {
			throw new EC2Exception(ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}
}
