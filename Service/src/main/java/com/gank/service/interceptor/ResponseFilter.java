package com.gank.service.interceptor;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gank.service.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回信息拦截打印
 *
 * @author shijunxing
 */
public class ResponseFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);
    private String userInfo;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        WrapperResponse wrapperResponse = new WrapperResponse((HttpServletResponse) response);
        filterChain.doFilter(request, wrapperResponse);
        byte[] content = wrapperResponse.getContent();
        if (logger.isDebugEnabled() && content != null && content.length > 0) {
            System.out.println("Request start : -----------------------------start----------------------------------");
            System.out.println();
            logger.debug("Request header: {}", getRequestHeaders((HttpServletRequest) request));
            logger.debug("Request Content: {}", getRequestInfo((HttpServletRequest) request, true));
            logger.debug("Response Content: {}", StringUtil.formatJson(new String(content)));

            System.out.println("Response Content : -----------------------------Content----------------------------------");
            System.out.println();
            System.out.println("Response Content:\n"+ StringUtil.formatJson(new String(content)));
            System.out.println();
            System.out.println("Response Content : -----------------------------Content----------------------------------");
            System.out.println();
            System.out.println("Request finish : -----------------------------finish----------------------------------");
        }
        ServletOutputStream out = response.getOutputStream();
        out.write(content);
        out.flush();
    }

    @Override
    public void init(FilterConfig paramFilterConfig) throws ServletException {
    }


    private String getRequestInfo(HttpServletRequest request, boolean requestDetails) {
        StringBuffer sb = new StringBuffer();
        sb.append(request.getMethod()).append(" ");
        sb.append(request.getRequestURI());

        if (requestDetails) {
            Enumeration<String> e = request.getParameterNames();
            sb.append("{");
            int i = 0;
            System.out.println("Request RequestInfo : -----------------------------RequestInfo----------------------------------");
            System.out.println();
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String val = request.getParameter(name);

                if (val != null && !val.isEmpty()) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(name).append(": ").append(val);
                    System.out.println("          "+name + " : " + val);
                    i++;
                }
            }
            System.out.println();
            System.out.println("Request RequestInfo : -----------------------------RequestInfo----------------------------------");
            sb.append("}");
        }

        return sb.toString();
    }

    private String getRequestHeaders(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");

        Enumeration<String> headers = request.getHeaderNames();
        int i = 0;
        System.out.println("Request   URL " +request.getMethod()+ " : " + request.getRequestURI());
        System.out.println();
        System.out.println("Request Headers : -----------------------------Headers----------------------------------");
        System.out.println();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();

            if (i > 0) {
                sb.append(", ");
            }
            sb.append(header + ": " + request.getHeader(header));
            System.out.println("          "+header + " : " + request.getHeader(header));
            if ("user-agent".equals(header)) {
                userInfo = request.getHeader(header).split("@")[1];
                System.out.println("          userInfo : " + userInfo);
            }
            i++;
        }
        System.out.println();
        System.out.println("Request Headers : -----------------------------Headers----------------------------------");
        sb.append("}");
        return sb.toString();
    }
}
