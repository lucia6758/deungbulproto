package com.sbs.deungbulproto.container;

import com.sbs.deungbulproto.session.Session;

public class Container {
	
	public static Session session;
	
	static {
		
		session = new Session();
		
	}	

}
