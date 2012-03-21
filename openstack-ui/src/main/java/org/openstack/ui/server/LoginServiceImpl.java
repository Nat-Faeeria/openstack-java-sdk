package org.openstack.ui.server;

import org.openstack.api.identity.IdentityResource;
import org.openstack.client.jersey2.OpenStackClient;
import org.openstack.client.jersey2.OpenStackClientFactory;
import org.openstack.model.identity.KeyStoneAccess;
import org.openstack.model.identity.KeyStoneAuthentication;
import org.openstack.model.identity.KeyStoneTenant;
import org.openstack.model.identity.KeyStoneTenantList;

public class LoginServiceImpl implements LoginService {

	@Override
	public KeyStoneAccess login(String identityURL, String username, String password) {
		KeyStoneAuthentication authentication = new KeyStoneAuthentication().withPasswordCredentials(username, password);
		
		OpenStackClient openstack = OpenStackClientFactory.authenticate(identityURL, username, password);
		//no services when login without tenant
		IdentityResource identity = openstack.target(identityURL, IdentityResource.class);
		
		KeyStoneAccess access = identity.tokens().authenticate(authentication);
		
		KeyStoneTenantList tenants = identity.tenants().get();
		
		KeyStoneTenant tenant = tenants.getList().get(0);
		
		authentication.setTenantId(tenant.getId());
		
		access = identity.tokens().authenticate(authentication);
		
		return access;
	}

}
