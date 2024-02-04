package com.example.mailserver.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mailserver.dao.entity.Role;
import com.example.mailserver.dao.entity.RoleRoute;
import com.example.mailserver.dao.entity.Route;
import com.example.mailserver.dao.entity.User;
import com.example.mailserver.dao.mapper.RoleMapper;
import com.example.mailserver.dao.mapper.RoleRouteMapper;
import com.example.mailserver.dao.mapper.RouteMapper;
import com.example.mailserver.dao.mapper.UserMapper;
import com.example.mailserver.utils.Result;
import com.example.mailserver.utils.ResultEnum;
import com.example.mailserver.utils.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.buf.MessageBytes;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthHandler implements HandlerInterceptor {
    UserMapper userMapper;
    RoleMapper roleMapper;
    RouteMapper routeMapper;
    RoleRouteMapper roleRouteMapper;
    Token token;

    public AuthHandler(UserMapper userMapper, RoleMapper roleMapper, RouteMapper routeMapper,
                       RoleRouteMapper roleRouteMapper, Token token) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.routeMapper = routeMapper;
        this.roleRouteMapper = roleRouteMapper;
        this.token = token;
    }

    private String getAuthorizationFromHeader(HttpServletRequest request) {
        final String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("mail ")) {
            return null;
        }

        final String[] tokenArray = token.split(" ");
        if (tokenArray.length < 2) {
            return null;
        }
        return tokenArray[1];
    }

    private void parseTokenError(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        printWriter.append(objectMapper.writeValueAsString(Result.error(ResultEnum.PARSE_TOKEN_ERROR)));
        printWriter.flush();
    }

    //根据Field获得对应的Class
    private Class getClassByName(Class classObject, String name) {
        Map<Class, List<Field>> fieldMap = new HashMap<>();
        Class returnClass = null;
        Class tempClass = classObject;
        while (tempClass != null) {
            fieldMap.put(tempClass, Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        for (Map.Entry<Class, List<Field>> entry : fieldMap.entrySet()) {
            for (Field f : entry.getValue()) {
                if (f.getName().equals(name)) {
                    returnClass = entry.getKey();
                    break;
                }
            }
        }
        return returnClass;
    }

    //递归遍历父类寻找coyoteRequest Field
    private Object findCoyoteRequest(Object request) throws Exception {
        Class a = getClassByName(request.getClass(), "request");
        Field request1 = a.getDeclaredField("request");
        request1.setAccessible(true);
        Object b = request1.get(request);
        if (getClassByName(b.getClass(), "coyoteRequest") == null) {
            return findCoyoteRequest(b);
        } else {
            return b;
        }
    }

    private void authError(HttpServletResponse response) throws IOException {
        response.setStatus(403);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        printWriter.append(objectMapper.writeValueAsString(Result.error(ResultEnum.AUTH_ERROR)));
        printWriter.flush();
    }

    private boolean judgeUserAuth(Route route, String path, String method) {
        System.out.println(path);
        System.out.println(method);
        return path.matches("^" + route.getPath() + "$")
                && method.equalsIgnoreCase(route.getMethod());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final String tokenString = getAuthorizationFromHeader(request);
        if (tokenString == null) {
//            parseTokenError(response);
            return true;
        }
        try {
            final int id = token.verify(tokenString);
            // 查询出用户
            User user = userMapper.selectById(id);
            // 获取用户角色
            Role userRole = roleMapper.selectById(user.getRoleId());
            user.setRole(userRole);
            // 该角色下所有的路由映射关系
            Map<String, Object> map = new HashMap<>();
            map.put("role_id", userRole.getId());
            final List<RoleRoute> roleRouteList = roleRouteMapper.selectByMap(map);
            // 获取该角色下所有的路由 id
            List<Integer> routeID = new ArrayList<>();
            for (RoleRoute roleRoute : roleRouteList) {
                routeID.add(roleRoute.getRouteId());
            }
            // 获取该角色下的所有路由
            QueryWrapper<Route> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", routeID);
            List<Route> routeList = routeMapper.selectList(queryWrapper);
            final String method = request.getMethod();
            Object a = findCoyoteRequest(request);
            Field coyoteRequest = a.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object b = coyoteRequest.get(a);
            Field uriMB = b.getClass().getDeclaredField("uriMB");
            uriMB.setAccessible(true);
            MessageBytes c = (MessageBytes) uriMB.get(b);
            final String path = "/error".equals(request.getRequestURI()) ? c.getString() : request.getRequestURI();
            boolean flag = false;
            for (Route route : routeList) {
                System.out.println(route);
                if (judgeUserAuth(route, path, method)) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                authError(response);
                return false;
            }
            request.getSession().setAttribute("currentUser", user);
            return true;
        } catch (Exception e) {
            authError(response);
            return false;
        }
    }
}
