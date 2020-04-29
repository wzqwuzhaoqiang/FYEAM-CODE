package com.fuyaogroup.eam.common.Filter;

import java.io.IOException;






import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
 







import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuyaogroup.eam.common.Json.Audience;
import com.fuyaogroup.eam.common.Json.JsonResult;
import com.fuyaogroup.eam.util.JwtHelperUtil;
//@Order(1)
@WebFilter(urlPatterns = "/api/*", filterName = "httpBearerAuthFilter")
public class HttpBearerAuthFilter implements Filter{
	@Autowired
	private Audience audienceEntity;
 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	            filterConfig.getServletContext());
		System.out.print("Filter is ok");
		
	}
 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.print("Filter is continues");
		JsonResult resultMsg;
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String auth = httpRequest.getHeader("Authorization");
		if ((auth != null) && (auth.length() > 7))
		{
			String HeadStr = auth.substring(0, 6).toLowerCase();
			if (HeadStr.compareTo("bearer") == 0)
			{
				auth = auth.substring(7, auth.length()); 
	            if (JwtHelperUtil.parseJWT(auth, audienceEntity.getPassword()) != null)
	            {
	            	chain.doFilter(request, response);
	            	return;
	            }
			}
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");  
		httpResponse.setContentType("application/json; charset=utf-8"); 
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 
		ObjectMapper mapper = new ObjectMapper();
		
		resultMsg = new JsonResult("500", "Token认证失败！", null);
		httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));
		return;
	}
 
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}

