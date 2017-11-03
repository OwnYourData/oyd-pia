package eu.ownyourdata.pia.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by michael on 16.09.16.
 */
@Configuration
public class ServerDetection implements Filter {
    private AtomicReference<String> host = new AtomicReference<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        InetAddress address = InetAddress.getByName(request.getLocalAddr());
        if (!address.isAnyLocalAddress() && !address.isLoopbackAddress()) {
            if (request.getServerName().indexOf("datentresor.org") == -1 && request.getServerName().indexOf("data-vault.eu") == -1) {
                host.compareAndSet(null, request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort()); 
            } else {
                host.compareAndSet(null, "https://" + request.getServerName()); 
            }
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }

    public String getHost() {
        return host.get();
    }
}
