package web.api;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class CORSFilter implements Filter {
    private static final String ALLOWED_ORIGINS = "http://localhost:8888";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        httpResponse.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGINS);
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(request, httpResponse);
    }

    @Override
    public void destroy() { }
}
