package com.tmobile.wsg.services.req.beans;

import com.tmobile.wsg.services.req.beans.generated.CustomProfile;

public class CustomerProfile extends CustomProfile{
	
	public CustomProfile getCustomProfile() {
		return customProfile;
	}

	public void setCustomProfile(CustomProfile customProfile) {
		this.customProfile = customProfile;
	}

	CustomProfile customProfile = new CustomProfile();
	
	
}
