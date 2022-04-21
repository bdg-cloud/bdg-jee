function findValue (key){
	var result = null;
    if(reportContext.getHttpServletRequest()!=null) {
		result = reportContext.getHttpServletRequest().getSession().getAttribute(key);
	} else {
		result = reportContext.getAppContext().get(key);
	}
	return result;
}
	
reportContext.setPersistentGlobalVariable("findValue", findValue);