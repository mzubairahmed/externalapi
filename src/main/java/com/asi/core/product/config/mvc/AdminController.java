package com.asi.core.product.config.mvc;


//@Controller
public class AdminController {

/*	private ConsumerTokenServices tokenServices;
	
	private TokenStore tokenStore;

	private AsiUserApprovalHandler userApprovalHandler;

	@RequestMapping("/oauth/cache_approvals")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void startCaching() throws Exception {
		userApprovalHandler.setUseApprovalStore(true);
	}

	@RequestMapping("/oauth/uncache_approvals")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void stopCaching() throws Exception {
		userApprovalHandler.setUseApprovalStore(false);
	}

	@RequestMapping("/oauth/users/{user}/tokens")
	@ResponseBody
	public Collection<OAuth2AccessToken> listTokensForUser(@PathVariable String user, Principal principal)
			throws Exception {
		checkResourceOwner(user, principal);
		return enhance(tokenStore.findTokensByUserName(user));
	}

	@RequestMapping(value = "/oauth/users/{user}/tokens/{token}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> revokeToken(@PathVariable String user, @PathVariable String token, Principal principal)
			throws Exception {
		checkResourceOwner(user, principal);
		if (tokenServices.revokeToken(token)) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);			
		}
	}

	@RequestMapping("/oauth/clients/{client}/tokens")
	@ResponseBody
	public Collection<OAuth2AccessToken> listTokensForClient(@PathVariable String client) throws Exception {
		return enhance(tokenStore.findTokensByClientId(client));
	}

	private Collection<OAuth2AccessToken> enhance(Collection<OAuth2AccessToken> tokens) {
		Collection<OAuth2AccessToken> result = new ArrayList<OAuth2AccessToken>();
		for (OAuth2AccessToken prototype : tokens) {
			DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(prototype);
			String clientId = tokenStore.readAuthentication(token).getOAuth2Request().getClientId();
			if (clientId != null) {
				Map<String, Object> map = new HashMap<String, Object>(token.getAdditionalInformation());
				map.put("client_id", clientId);
				token.setAdditionalInformation(map);
				result.add(token);
			}
		}
		return result;
	}

	private void checkResourceOwner(String user, Principal principal) {
		if (principal instanceof OAuth2Authentication) {
			OAuth2Authentication authentication = (OAuth2Authentication) principal;
			if (!authentication.isClientOnly() && !user.equals(principal.getName())) {
				throw new AccessDeniedException(String.format("User '%s' cannot obtain tokens for user '%s'",
						principal.getName(), user));
			}
		}
	}

	*//**
	 * @param userApprovalHandler the userApprovalHandler to set
	 *//*
	public void setUserApprovalHandler(AsiUserApprovalHandler userApprovalHandler) {
		this.userApprovalHandler = userApprovalHandler;
	}

	*//**
	 * @param tokenServices the consumerTokenServices to set
	 *//*
	public void setTokenServices(ConsumerTokenServices tokenServices) {
		this.tokenServices = tokenServices;
	}
	
	*//**
	 * @param tokenStore the tokenStore to set
	 *//*
	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}
*/

}
