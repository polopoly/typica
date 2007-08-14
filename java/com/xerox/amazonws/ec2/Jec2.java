//
// typica - A client library for Amazon Web Services
// Copyright (C) 2007 Xerox Corporation
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

import ch.inventec.Base64Coder;

import com.xerox.amazonws.typica.jaxb.AuthorizeSecurityGroupIngressResponse;
import com.xerox.amazonws.typica.jaxb.CreateKeyPairResponse;
import com.xerox.amazonws.typica.jaxb.ConfirmProductInstanceResponse;
import com.xerox.amazonws.typica.jaxb.CreateSecurityGroupResponse;
import com.xerox.amazonws.typica.jaxb.DeleteKeyPairResponse;
import com.xerox.amazonws.typica.jaxb.DeleteSecurityGroupResponse;
import com.xerox.amazonws.typica.jaxb.DeregisterImageResponse;
import com.xerox.amazonws.typica.jaxb.DescribeImageAttributeResponse;
import com.xerox.amazonws.typica.jaxb.DescribeImagesResponse;
import com.xerox.amazonws.typica.jaxb.DescribeImagesResponseInfoType;
import com.xerox.amazonws.typica.jaxb.DescribeImagesResponseItemType;
import com.xerox.amazonws.typica.jaxb.DescribeInstancesResponse;
import com.xerox.amazonws.typica.jaxb.DescribeKeyPairsResponse;
import com.xerox.amazonws.typica.jaxb.DescribeKeyPairsResponseInfoType;
import com.xerox.amazonws.typica.jaxb.DescribeKeyPairsResponseItemType;
import com.xerox.amazonws.typica.jaxb.DescribeSecurityGroupsResponse;
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
import com.xerox.amazonws.typica.jaxb.ObjectFactory;
import com.xerox.amazonws.typica.jaxb.ProductCodeListType;
import com.xerox.amazonws.typica.jaxb.ProductCodeItemType;
import com.xerox.amazonws.typica.jaxb.ProductCodesSetType;
import com.xerox.amazonws.typica.jaxb.ProductCodesSetItemType;
import com.xerox.amazonws.typica.jaxb.RebootInstancesResponse;
import com.xerox.amazonws.typica.jaxb.RegisterImageResponse;
import com.xerox.amazonws.typica.jaxb.RevokeSecurityGroupIngressResponse;
import com.xerox.amazonws.typica.jaxb.ReservationSetType;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xerox.amazonws.common.AWSQueryConnection;
import com.xerox.amazonws.common.JAXBuddy;

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
    public Jec2(String awsAccessId, String awsSecretAccessKey) {
        this(awsAccessId, awsSecretAccessKey, true);
    }

	/**
	 * Initializes the ec2 service with your AWS login information.
	 *
     * @param awsAccessId The your user key into AWS
     * @param awsSecretKey The secret string used to generate signatures for authentication.
     * @param isSecure True if the data should be encrypted on the wire on the way to or from EC2.
	 */
    public Jec2(String awsAccessId, String awsSecretAccessKey, boolean isSecure) {
        this(awsAccessId, awsSecretAccessKey, isSecure, "ec2.amazonaws.com");
    }

	/**
	 * Initializes the ec2 service with your AWS login information.
	 *
     * @param awsAccessId The your user key into AWS
     * @param awsSecretKey The secret string used to generate signatures for authentication.
     * @param isSecure True if the data should be encrypted on the wire on the way to or from EC2.
     * @param server Which host to connect to.  Usually, this will be s3.amazonaws.com
	 */
    public Jec2(String awsAccessId, String awsSecretAccessKey, boolean isSecure,
                             String server)
    {
        this(awsAccessId, awsSecretAccessKey, isSecure, server,
             isSecure ? 443 : 80);
    }

    /**
	 * Initializes the ec2 service with your AWS login information.
	 *
     * @param awsAccessId The your user key into AWS
     * @param awsSecretKey The secret string used to generate signatures for authentication.
     * @param isSecure True if the data should be encrypted on the wire on the way to or from EC2.
     * @param server Which host to connect to.  Usually, this will be s3.amazonaws.com
     * @param port Which port to use.
     */
    public Jec2(String awsAccessKeyId, String awsSecretAccessKey, boolean isSecure,
                             String server, int port)
    {
		super(awsAccessKeyId, awsSecretAccessKey, isSecure, server, port);
		ArrayList vals = new ArrayList();
		vals.add("2007-03-01");
		super.headers.put("Version", vals);
    }

	/**
	 * Register the given AMI.
	 * 
	 * @param imageLocation An AMI path within S3.
	 * @return A unique AMI ID that can be used to create and manage instances of this AMI.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public String registerImage(String imageLocation) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageLocation", imageLocation);
		try {
			InputStream iStr =
				makeRequest("GET", "RegisterImage", params).getInputStream();
			RegisterImageResponse response =
					JAXBuddy.deserializeXMLStream(RegisterImageResponse.class, iStr);
			return response.getImageId();
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "RegisterImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Deregister the given AMI.
	 * 
	 * @param imageId An AMI ID as returned by {@link #registerImage(String)}.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void deregisterImage(String imageId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageId", imageId);
		try {
			InputStream iStr =
				makeRequest("GET", "DeregisterImage", params).getInputStream();
			DeregisterImageResponse response =
					JAXBuddy.deserializeXMLStream(DeregisterImageResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not deregister image : "+imageId+". No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DeregisterImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
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
	 * @param imageIds A list of AMI IDs as returned by {@link #registerImage(String)}.
	 * @param owners A list of owners.
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
	 * @param users A list of users.
	 * @return A list of {@link ImageDescription} instances describing each AMI ID.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ImageDescription> describeImages(List<String> imageIds, List<String> owners, List<String> users) throws EC2Exception {
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
		return describeImages(params);
	}


	protected List<ImageDescription> describeImages(Map<String, String> params) throws EC2Exception {
		try {
			InputStream iStr =
				makeRequest("GET", "DescribeImages", params).getInputStream();
			DescribeImagesResponse response =
					JAXBuddy.deserializeXMLStream(DescribeImagesResponse.class, iStr);
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
				result.add(new ImageDescription(item.getImageId(), item
						.getImageLocation(), item.getImageOwnerId(), item
						.getImageState(), item.isIsPublic(), codes));
			}
			return result;
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
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
	 * 
	 * @param imageId An AMI ID as returned by {@link #registerImage(String)}.
	 * @param minCount The minimum number of instances to attempt to reserve.
	 * @param maxCount The maximum number of instances to attempt to reserve.
	 * @param groupSet A (possibly empty) set of security group definitions.
	 * @param userData User supplied data that will be made available to the instance(s)
	 * @return A {@link ReservationDescription} describing the instances that
	 *         have been reserved.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ReservationDescription runInstances(String imageId, int minCount,
			int maxCount, List<String> groupSet, String userData, String keyName)
				throws EC2Exception {
		return runInstances(imageId, minCount, maxCount, groupSet, userData, keyName, true);
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
	 * @return A {@link ReservationDescription} describing the instances that
	 *         have been reserved.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ReservationDescription runInstances(String imageId, int minCount,
			int maxCount, List<String> groupSet, String userData, String keyName, boolean publicAddr)
				throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ImageId", imageId);
		params.put("MinCount", ""+minCount);
		params.put("MaxCount", ""+maxCount);
		if (userData != null && !userData.trim().equals("")) {
			params.put("UserData", Base64Coder.encodeString(userData));
		}
		if (publicAddr) {
			params.put("AddressingType", "public");
		}
		else {
			params.put("AddressingType", "direct");
		}
		if (keyName != null && !keyName.trim().equals("")) {
			params.put("KeyName", keyName);
		}
		if (groupSet != null) {
			for (int i=0 ; i<groupSet.size(); i++) {
				params.put("SecurityGroup."+(i+1), groupSet.get(i));
			}
		}

		try {
			InputStream iStr =
				makeRequest("GET", "RunInstances", params).getInputStream();
			RunInstancesResponse response =
					JAXBuddy.deserializeXMLStream(RunInstancesResponse.class, iStr);
			ReservationDescription res = new ReservationDescription(response.getOwnerId(),
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
								rsp_item.getKeyName());
			}
			return res;
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "RunInstances", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Terminates a selection of running instances.
	 * 
	 * @param instanceIds An array of instances ({@link ReservationDescription.Instance#instanceId}.
	 * @return A list of {@link TerminatingInstanceDescription} instances.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<TerminatingInstanceDescription> terminateInstances(
			String[] instanceIds) throws EC2Exception {
		return this.terminateInstances(Arrays.asList(instanceIds));
	}

	/**
	 * Terminates a selection of running instances.
	 * 
	 * @param instanceIds A list of instances ({@link ReservationDescription.Instance#instanceId}.
	 * @return A list of {@link TerminatingInstanceDescription} instances.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<TerminatingInstanceDescription> terminateInstances(List<String> instanceIds)
			throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<instanceIds.size(); i++) {
			params.put("InstanceId."+(i+1), instanceIds.get(i));
		}
		try {
			InputStream iStr =
				makeRequest("GET", "TerminateInstances", params).getInputStream();
			TerminateInstancesResponse response =
					JAXBuddy.deserializeXMLStream(TerminateInstancesResponse.class, iStr);
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
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Gets a list of running instances.
	 * <p>
	 * If the array of instance IDs is empty then a list of all instances owned
	 * by the caller will be returned. Otherwise the list will contain
	 * information for the requested instances only.
	 * 
	 * @param instanceIds An array of instances ({@link ReservationDescription.Instance#instanceId}.
	 * @return A list of {@link ReservationDescription} instances.
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
	 * @param instanceIds A list of instances ({@link ReservationDescription.Instance#instanceId}.
	 * @return A list of {@link ReservationDescription} instances.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<ReservationDescription> describeInstances(List<String> instanceIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<instanceIds.size(); i++) {
			params.put("InstanceId."+(i+1), instanceIds.get(i));
		}
		try {
			InputStream iStr =
				makeRequest("GET", "DescribeInstances", params).getInputStream();
			DescribeInstancesResponse response =
					JAXBuddy.deserializeXMLStream(DescribeInstancesResponse.class, iStr);
			List<ReservationDescription> result = new ArrayList<ReservationDescription>();
			ReservationSetType res_set = response.getReservationSet();
			Iterator reservations_iter = res_set.getItems().iterator();
			while (reservations_iter.hasNext()) {
				RunInstancesResponse item = (RunInstancesResponse) reservations_iter.next();
				ReservationDescription res = new ReservationDescription(item
													.getOwnerId(), item.getReservationId());
				GroupSetType grp_set = item.getGroupSet();
				Iterator groups_iter = grp_set.getItems().iterator();
				while (groups_iter.hasNext()) {
					GroupItemType rsp_item = (GroupItemType) groups_iter.next();
					res.addGroup(rsp_item.getGroupId());
				}
				RunningInstancesSetType set = item.getInstancesSet();
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
									rsp_item.getKeyName());
				}
				result.add(res);
			}
			return result;
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Reboot a selection of running instances.
	 * 
	 * @param instanceIds A list of instances ({@link ReservationDescription.Instance#instanceId}.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void rebootInstances(String [] instanceIds) throws EC2Exception {
		this.rebootInstances(Arrays.asList(instanceIds));
	}

	/**
	 * Reboot a selection of running instances.
	 * 
	 * @param instanceIds A list of instances ({@link ReservationDescription.Instance#instanceId}.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void rebootInstances(List<String> instanceIds) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<instanceIds.size(); i++) {
			params.put("InstanceId."+(i+1), instanceIds.get(i));
		}
		try {
			InputStream iStr =
				makeRequest("GET", "RebootInstances", params).getInputStream();
			RebootInstancesResponse response =
					JAXBuddy.deserializeXMLStream(RebootInstancesResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not reboot instances. No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Get an instance's console output.
	 *
	 * @param instanceId An instance's id ({@link ReservationDescription.Instance#instanceId}.
	 * @return ({@link ConsoleOutput})
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ConsoleOutput getConsoleOutput(String instanceId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("InstanceId", instanceId);
		try {
			InputStream iStr =
				makeRequest("GET", "GetConsoleOutput", params).getInputStream();
			GetConsoleOutputResponse response =
					JAXBuddy.deserializeXMLStream(GetConsoleOutputResponse.class, iStr);
			return new ConsoleOutput(response.getInstanceId(),
				response.getTimestamp().toGregorianCalendar(),
				new String(Base64Coder.decodeString(response.getOutput().replaceAll("\n", ""))));
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Creates a security group.
	 * 
	 * @param name The name of the security group. 
	 * @param desc The description of the security group.
	 * @return <code>true</code> if the group was created, otherwise <code>false</code>
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void createSecurityGroup(String name, String desc) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", name);
		params.put("GroupDescription", desc);
		try {
			InputStream iStr =
				makeRequest("GET", "CreateSecurityGroup", params).getInputStream();
			CreateSecurityGroupResponse response =
					JAXBuddy.deserializeXMLStream(CreateSecurityGroupResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not create security group : "+name+". No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Deletes a security group. 
	 *
	 * @param name The name of the security group. 
	 * @return <code>true</code> if the group was deleted, otherwise <code>false</code>
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void deleteSecurityGroup(String name) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", name);
		try {
			InputStream iStr =
				makeRequest("GET", "DeleteSecurityGroup", params).getInputStream();
			DeleteSecurityGroupResponse response =
					JAXBuddy.deserializeXMLStream(DeleteSecurityGroupResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not delete security group : "+name+". No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
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
		try {
			InputStream iStr =
				makeRequest("GET", "DescribeSecurityGroups", params).getInputStream();
			DescribeSecurityGroupsResponse response =
					JAXBuddy.deserializeXMLStream(DescribeSecurityGroupsResponse.class, iStr);
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
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Adds incoming permissions to a security group.
	 * 
	 * @param groupName name of group to modify
	 * @param secGroupName name of security group to authorize access to
	 * @param secGroupOwnerId owner of security group to authorize access to
	 * @return A group description ({@link GroupDescription} containing the modified rules.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void authorizeSecurityGroupIngress(String groupName, String secGroupName,
											String secGroupOwnerId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", groupName);
		params.put("SourceSecurityGroupOwnerId", secGroupOwnerId);
		params.put("SourceSecurityGroupName", secGroupName);
		try {
			InputStream iStr =
				makeRequest("GET", "AuthorizeSecurityGroupIngress", params).getInputStream();
			AuthorizeSecurityGroupIngressResponse response =
				JAXBuddy.deserializeXMLStream(AuthorizeSecurityGroupIngressResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not authorize security ingress : "+groupName+". No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "AuthorizeSecurityGroupIngress", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
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
	 * @return A group description ({@link GroupDescription} containing the modified rules.
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
		try {
			InputStream iStr =
				makeRequest("GET", "AuthorizeSecurityGroupIngress", params).getInputStream();
			AuthorizeSecurityGroupIngressResponse response =
				JAXBuddy.deserializeXMLStream(AuthorizeSecurityGroupIngressResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not authorize security ingress : "+groupName+". No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "AuthorizeSecurityGroupIngress", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Revokes incoming permissions from a security group.
	 * 
	 * @param groupName name of group to modify
	 * @param secGroupName name of security group to revoke access from
	 * @param secGroupOwnerId owner of security group to revoke access from
	 * @return A group description ({@link GroupDescription} containing the modified rules.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void revokeSecurityGroupIngress(String groupName, String secGroupName,
											String secGroupOwnerId) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("GroupName", groupName);
		params.put("SourceSecurityGroupOwnerId", secGroupOwnerId);
		params.put("SourceSecurityGroupName", secGroupName);
		try {
			InputStream iStr =
				makeRequest("GET", "RevokeSecurityGroupIngress", params).getInputStream();
			RevokeSecurityGroupIngressResponse response =
				JAXBuddy.deserializeXMLStream(RevokeSecurityGroupIngressResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not revoke security ingress : "+groupName+". No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "RevokeSecurityGroupIngress", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
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
	 * @return A group description ({@link GroupDescription} containing the modified rules.
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
		try {
			InputStream iStr =
				makeRequest("GET", "RevokeSecurityGroupIngress", params).getInputStream();
			RevokeSecurityGroupIngressResponse response =
				JAXBuddy.deserializeXMLStream(RevokeSecurityGroupIngressResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not revoke security ingress : "+groupName+". No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "RevokeSecurityGroupIngress", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}


	/**
	 * Creates a public/private keypair.
	 * 
	 * @param keyName Name of the keypair.
	 * @return A keypair description ({@link KeyPairInfo}).
	 * @throws EC2Exception wraps checked exceptions
	 */
	public KeyPairInfo createKeyPair(String keyName) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("KeyName", keyName);
		try {
			InputStream iStr =
				makeRequest("GET", "CreateKeyPair", params).getInputStream();
			CreateKeyPairResponse response =
					JAXBuddy.deserializeXMLStream(CreateKeyPairResponse.class, iStr);
			return new KeyPairInfo(response.getKeyName(),
									response.getKeyFingerprint(),
									response.getKeyMaterial());
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
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
	 * Lists public/private keypairs.
	 * 
	 * @param keyIds A list of keypairs.
	 * @return A list of keypair descriptions ({@link KeyPairInfo}).
	 * @throws EC2Exception wraps checked exceptions
	 */
	public List<KeyPairInfo> describeKeyPairs(List<String> keyIds)
			throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0 ; i<keyIds.size(); i++) {
			params.put("KeyName."+(i+1), keyIds.get(i));
		}
		try {
			InputStream iStr =
				makeRequest("GET", "DescribeKeyPairs", params).getInputStream();
			DescribeKeyPairsResponse response =
					JAXBuddy.deserializeXMLStream(DescribeKeyPairsResponse.class, iStr);
			List<KeyPairInfo> result = new ArrayList<KeyPairInfo>();
			DescribeKeyPairsResponseInfoType set = response.getKeySet();
			Iterator set_iter = set.getItems().iterator();
			while (set_iter.hasNext()) {
				DescribeKeyPairsResponseItemType item = (DescribeKeyPairsResponseItemType) set_iter.next();
				result.add(new KeyPairInfo(item.getKeyName(), item.getKeyFingerprint(), null));
			}
			return result;
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Deletes a public/private keypair.
	 * 
	 * @param keyName Name of the keypair.
	 * @throws EC2Exception wraps checked exceptions
	 */
	public void deleteKeyPair(String keyName) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("KeyName", keyName);
		try {
			InputStream iStr =
				makeRequest("GET", "DeleteKeyPair", params).getInputStream();
			DeleteKeyPairResponse response =
					JAXBuddy.deserializeXMLStream(DeleteKeyPairResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not delete keypair : "+keyName+". No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Enumarates image list attribute operation types.
	 */
	public enum ImageListAttributeOperationType {
		add,
		remove
	}
	
	/**
	 * Modifies an attribute by the given items with the given operation. 
	 *
	 * @param imageID The ID of the AMI to modify the attributes for.
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
		}
		else if (attribute.getType().equals(ImageAttribute.ImageAttributeType.productCodes)) {
			params.put("Attribute", "productCodes");
		}

		switch (operationType) {
			case add: params.put("OperationType", "add"); break;
			case remove: params.put("OperationType", "remove"); break;
			default:
				throw new IllegalArgumentException("Unknown attribute operation.");
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

		try {
			InputStream iStr =
				makeRequest("GET", "ModifyImageAttribute", params).getInputStream();
			ModifyImageAttributeResponse response =
					JAXBuddy.deserializeXMLStream(ModifyImageAttributeResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not reset image attribute. No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
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
		try {
			InputStream iStr =
				makeRequest("GET", "ResetImageAttribute", params).getInputStream();
			ResetImageAttributeResponse response =
					JAXBuddy.deserializeXMLStream(ResetImageAttributeResponse.class, iStr);
			if (!response.isReturn()) {
				throw new EC2Exception("Could not reset image attribute. No reason given.");
			}
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
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
		try {
			InputStream iStr =
				makeRequest("GET", "DescribeImageAttribute", params).getInputStream();
			DescribeImageAttributeResponse rsp =
					JAXBuddy.deserializeXMLStream(DescribeImageAttributeResponse.class, iStr);
			ImageListAttribute attribute = null;
			if (rsp.getLaunchPermission() != null) {
				LaunchPermissionListType list = rsp.getLaunchPermission();
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
			else if (rsp.getProductCodes() != null) {
				ProductCodeListType list = rsp.getProductCodes();
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
			ProductCodeListType set = rsp.getProductCodes();
			if (set != null) {
				for (ProductCodeItemType code : set.getItems()) {
					codes.add(code.getProductCode());
				}
			}
			return new DescribeImageAttributeResult(rsp.getImageId(), attribute, codes);
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "DescribeImages", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}

	/**
	 * Returns true if the productCode is associated with the instance.
	 *
	 * @param instanceId An instance's id ({@link ReservationDescription.Instance#instanceId}.
	 * @returns null if no relationship exists, otherwise information about the owner
	 * @throws EC2Exception wraps checked exceptions
	 */
	public ProductInstanceInfo confirmProductInstance(String instanceId, String productCode) throws EC2Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("InstanceId", instanceId);
		params.put("ProductCode", productCode);
		try {
			InputStream iStr =
				makeRequest("GET", "ConfirmProductInstance", params).getInputStream();
			ConfirmProductInstanceResponse response =
					JAXBuddy.deserializeXMLStream(ConfirmProductInstanceResponse.class, iStr);
			if (response.isReturn()) {
				return new ProductInstanceInfo(instanceId, productCode, response.getOwnerId());
			}
			else return null;
		} catch (ArrayStoreException ex) {
			logger.error("ArrayStore problem, fetching response again to aid in debug.");
			try {
				logger.error(makeRequest("GET", "ConfirmProductInstance", params).getResponseMessage());
			} catch (Exception e) {
				logger.error("Had trouble re-fetching the request response.", e);
			}
			throw new EC2Exception("ArrayStore problem, maybe EC2 responded poorly?", ex);
		} catch (JAXBException ex) {
			throw new EC2Exception("Problem parsing returned message.", ex);
		} catch (MalformedURLException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new EC2Exception(ex.getMessage(), ex);
		}
	}
}
