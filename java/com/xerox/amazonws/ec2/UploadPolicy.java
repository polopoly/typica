//
// typica - A client library for Amazon Web Services
// Copyright (C) 2008,2009 Xerox Corporation
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;

public class UploadPolicy {
	private int minutesToExpiration;
	private String bucket;
	private String acl;

	public UploadPolicy(int minutesToExpiration, String bucket, String acl) {
		this.minutesToExpiration = minutesToExpiration;
		this.bucket = bucket;
		this.acl = acl;
	}

	UploadPolicy(String jsonPolicyString) {
		// TODO: parse json string.. do we really want another dependency??
	}

	public int getMinutesToExpiration() {
		return minutesToExpiration;
	}

	public String getBucket() {
		return bucket;
	}

	public String getAcl() {
		return acl;
	}

	public String getPolicyString() {
		StringBuilder json = new StringBuilder("{\n");
		json.append("\"expiration\": \"");
        final String DateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat format = new SimpleDateFormat( DateFormat, Locale.US );
        format.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
		json.append(format.format(new Date(System.currentTimeMillis()+(minutesToExpiration*60000L))));
		json.append("\",\n");
		json.append("\"conditions\": [\n");

		json.append("{\"bucket\": \"");
		json.append(bucket);
		json.append("\"},");

		json.append("{\"acl\": \"");
		json.append(acl);
		json.append("\"},");

		json.append("]\n}");
		return new String(Base64.encodeBase64(json.toString().getBytes()));
	}

	public String toString() {
		return "UploadPolicy[minutesToExpiration="+minutesToExpiration+", bucket="
								+this.bucket+", acl="+this.acl+"]";
	}
}
