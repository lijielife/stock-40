package com.youku.java.copyright.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.util.Constant.RedisNameSpace;
import com.youku.java.copyright.util.Constant.Stock;
import com.youku.java.copyright.util.Constant.Time;
import com.youku.java.copyright.util.JedisUtil;
import com.youku.java.raptor.auth.NeedLogin;
import com.youku.java.raptor.exception.NeedAuthorizationException;

/**
 * 切面
 * @author chenlisong
 *
 */
@Aspect
@Component
public class AspecterStock {
	
	@Autowired
	private JedisUtil jedisUtil;
	
	@Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object doControllerAround(ProceedingJoinPoint pjp) throws Throwable {
        Object response = null;
        
        Object[] args = pjp.getArgs();
        Object[] argsModified = new Object[args.length];

        MethodSignature signature = (MethodSignature) pjp.getSignature(); 
        Method method = signature.getMethod();
        Class<?> clazz = method.getDeclaringClass();

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            String[] paths = requestMapping.value();
            String pathString = "";
            for (String path: paths) {
                pathString += path + ",";
            }
            if ("".equals(pathString)) {
                pathString = "unknowPath";
            } else {
                pathString = pathString.substring(0, pathString.length() - 1);
            }

            RequestMethod[] httpMethods = requestMapping.method();
            String methodString = "";
            for (RequestMethod httpMethod: httpMethods) {
                methodString += httpMethod.name() + ",";
            }

            if ("".equals(methodString)) {
                methodString = "unknowMethod";
            } else {
                methodString = methodString.substring(0, methodString.length() - 1);
            }
        }

        if (clazz.isAnnotationPresent(NeedLogin.class) || method.isAnnotationPresent(NeedLogin.class)) {
            ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = sra.getRequest();

            User loginInfo = null;
            String ticket = request.getHeader("ticket");

            String sourceIp = request.getHeader("Request-Ip");

            if (sourceIp == null || "".equals(sourceIp)) {
                String forwardedIpString = request.getHeader("X-Forwarded-For");
                if (forwardedIpString != null) {
                    String[] forwardedIps = forwardedIpString.split(",");
                    if (forwardedIps.length > 0 && !"".equals(forwardedIps[0].trim())) {
                        sourceIp = forwardedIps[0].trim();
                    }
                }
            }

            if (sourceIp == null || "".equals(sourceIp)) {
                sourceIp = request.getHeader("X-Real-IP");
            }
            if (sourceIp == null || "".equals(sourceIp)) {
                sourceIp = request.getRemoteAddr();
            }
            if (ticket != null && !"".equals(ticket)) {
                try {
                    loginInfo = JSON.parseObject(jedisUtil.get(RedisNameSpace.LOGIN + ticket), User.class);
                } catch(Exception e) {
                    throw new NeedAuthorizationException(Stock.LOGIN_URL);
                }
            }

            if (loginInfo == null) {
            	 throw new NeedAuthorizationException(Stock.LOGIN_URL);
            }else {
            	//刷新时间
            	jedisUtil.set(RedisNameSpace.LOGIN + ticket, JSON.toJSONString(loginInfo), Time.LOGIN_TIME);
            }

            int i = 0;
            for (Object o: args) {
                if (o instanceof User) {
                    argsModified[i++] = loginInfo;
                } else {
                    argsModified[i++] =  o;
                }
            }
        } else {
            argsModified = args;
        }
        response = pjp.proceed(argsModified);
        return response;
    }
}
