package com.biji.puppeteer.util;



import com.alibaba.druid.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public final class RequestUtil {
    /**
     * Convenience method to set a cookie
     *
     * @param response the current response
     * @param name     the name of the cookie
     * @param value    the value of the cookie
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        RequestUtil.setCookie(response, name, value, "");
    }

    /**
     * Convenience method to set a cookie
     *
     * @param response the current response
     * @param name     the name of the cookie
     * @param value    the value of the cookie
     * @param path     the path to set it on
     */
    public static void setCookie(HttpServletResponse response, String name,
                                 String value, String path) {
        setCookie(response, name, value, path, 30);
    }


    /**
     * @param response
     * @param name
     * @param value
     * @param path
     * @param maxAge   以小时为单位
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String domain, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setMaxAge(3600 * maxAge);

        if (!StringUtils.isEmpty(path))
            cookie.setPath(path);
        if (!StringUtils.isEmpty(domain))
            cookie.setDomain(domain);


        response.addCookie(cookie);
    }


    /**
     * @param response
     * @param name
     * @param value
     * @param path
     * @param maxAge   以小时为单位
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setMaxAge(3600 * maxAge);

        if (!StringUtils.isEmpty(path))
            cookie.setPath(path);

        response.addCookie(cookie);
    }

    /**
     * Convenience method to get a cookie by name
     *
     * @param request the current request
     * @param name    the name of the cookie to find
     * @return the cookie (if found), null if not found
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (final Cookie thisCookie : cookies) {
            if (thisCookie.getName().equals(name) && !"".equals(thisCookie.getValue())) {
                return thisCookie;
            }
        }

        return null;
    }

    /**
     * Convenience method for deleting a cookie by name
     *
     * @param response the current web response
     * @param cookie   the cookie to delete
     * @param path     the path on which the cookie was set (i.e. /appfuse)
     */
    public static void deleteCookie(HttpServletResponse response,
                                    Cookie cookie, String domain, String path) {
        if (cookie != null) {
            // Delete the cookie by setting its maximum age to zero
            cookie.setMaxAge(0);
            cookie.setPath(path);
            cookie.setDomain(domain);
            response.addCookie(cookie);
        }
    }
    /**
     * Convenience method for deleting a cookie by name
     *
     * @param response the current web response
     * @param cookie   the cookie to delete
     * @param path     the path on which the cookie was set (i.e. /appfuse)
     */
    public static void deleteCookie(HttpServletResponse response,
                                    Cookie cookie, String path) {
        if (cookie != null) {
            // Delete the cookie by setting its maximum age to zero
            cookie.setMaxAge(0);
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name, String path){
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length == 0){
            return;
        }
        for(Cookie cookie:cookies){
            if(name.equals(cookie.getName())){
                cookie.setMaxAge(0);
                cookie.setPath(path);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * Convenience method to get the application's URL based on request
     * variables.
     *
     * @param request the current request
     * @return URL to application
     */
    public static String getAppURL(HttpServletRequest request) {
        if (request == null) return "";

        StringBuffer url = new StringBuffer();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        String scheme = request.getScheme();
        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getContextPath());
        return url.toString();
    }

    /**
     * 获取文件对应的确byte数组
     *
     * @param f
     * @return
     */
    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        FileInputStream stream = null;
        ByteArrayOutputStream out = null;
        try {
            stream = new FileInputStream(f);
            out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1) {
                out.write(b, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取request的byte数组
     *
     * @param request
     * @return
     */
    public static byte[] getBytesFromRequest(HttpServletRequest request) {
        ByteArrayOutputStream outStream = null;
        ServletInputStream input = null;
        byte[] req = null;
        try {
            input = request.getInputStream();
            outStream = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = input.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            req = outStream.toByteArray();
        } catch (IOException e) {
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {

                }
            }
        }
        return req;
    }
}

