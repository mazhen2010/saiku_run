package org.saiku.web.rest.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;

public class ServletUtil {
	
	
	private static final String PREFIX_PARAMETER = "param";
	
	public static Map<String, String> getParameters(HttpServletRequest req) {
		return getParameters(req, PREFIX_PARAMETER);
	}
	
	private static Map<String, String> getParameters(HttpServletRequest req, String prefix) {

		Map<String, String> queryParams = new HashMap<>();
		if (req != null) {
			// ... and the query parameters
			// We identify any pathParams starting with "param" as query parameters 

			// FIXME we should probably be able to have array params as well
			Enumeration<String> enumeration = req.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String param = (String) enumeration.nextElement();
				String value = req.getParameter(param);
				if (StringUtils.isNotBlank(prefix)) {
					if(param.toLowerCase().startsWith(prefix))
					{
						param = param.substring(prefix.length());
						queryParams.put(param, value);
					}
				} else {
					queryParams.put(param, value);
				}
			}
		}
		return queryParams;
	}
	
	private static Map<String, String> getParameters(MultivaluedMap<String, String> formParams) {
		return getParameters(formParams, PREFIX_PARAMETER);
	}
	
	private static Map<String, String> getParameters(MultivaluedMap<String, String> formParams, String prefix) {
		Map<String, String> queryParams = new HashMap<>();
		if (formParams != null) {
			for (String key : formParams.keySet()) {
				String param = key;
				String value = formParams.getFirst(key);
				
				if (StringUtils.isNotBlank(prefix)) {
					if(param.toLowerCase().startsWith(prefix))
					{
						param = param.substring(prefix.length());
						queryParams.put(param, value);
					}
				} else {
					queryParams.put(param, value);
				}
			}
		}
		return queryParams;
		
	}

	private static String replaceParameters(String query, Map<String, String> parameters) {
		if (parameters != null) {
			for (String parameter : parameters.keySet()) {
				String value = parameters.get(parameter);
				query = query.replaceAll("(?i)\\$\\{" + parameter + "\\}", value);
			}
		}
		return query;
	}
	
	public static String replaceParameters(HttpServletRequest req, String query) {
		Map<String, String> queryParams = getParameters(req);
	  return replaceParameters(query, queryParams);
	}

	public static String replaceParameters(MultivaluedMap<String, String> formParams, String query) {
		Map<String, String> queryParams = getParameters(formParams);
	  return replaceParameters(query, queryParams);
	}
}
