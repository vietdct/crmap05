package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebFilter(filterName = "authorFilter", urlPatterns = {"/user-add","/task-add"})
public class AuthorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getServletPath();
		Cookie[] cookies = req.getCookies();
		String roleUser = "";
		
		for(int i=0; i<cookies.length; i++) {
			String ckname = cookies[i].getName();
			String ckvalue = cookies[i].getValue();
			
			if(ckname.equals("role")) {
				roleUser = ckvalue;
				break;
			}
		}
		String contextPath = req.getContextPath();
		switch (roleUser){
		case "1": 
			if(path.equals("/user-add")) {
				chain.doFilter(request, response);
			}
		case "2":
			if(path.equals("/task-add")) {
				chain.doFilter(req, resp);
			}
		default:
			
		}
		
	}

}
