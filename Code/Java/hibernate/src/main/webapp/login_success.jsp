<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>登录成功</title>
    </head>

    <body>
        <s:form action="login" namespace="/" method="post">
            <s:textfield name="name" label="name"></s:textfield>
            <s:password name="password" label="password"></s:password>
            <s:submit value="Login"></s:submit>
        </s:form>
    </body>
</html>
