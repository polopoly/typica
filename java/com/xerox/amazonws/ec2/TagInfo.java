//
// typica - A client library for Amazon Web Services
// Copyright (C) 2007,2008 Xerox Corporation
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

/**
 * An instance of this class represents an EC2 Tag.
 */
public class TagInfo {
	private String resourceId;
	private String resourceType;
	private String tagKey;
	private String tagValue;

	public TagInfo(String resourceId, String resourceType, String tagKey, String tagValue) {
		this.resourceId = resourceId;
		this.resourceType = resourceType;
		this.tagKey = tagKey;
		this.tagValue = tagValue;
	}

	public String getResourceId() {
		return resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public String getTagKey() {
		return tagKey;
	}

	public String getTagValue() {
		return tagValue;
	}

	public String toString() {
		return "Tag[resource=" + this.resourceId + ", type="
				+ this.resourceType + ", key=" + this.tagKey
				+ ", value=" + this.tagValue + "]";
	}
}

