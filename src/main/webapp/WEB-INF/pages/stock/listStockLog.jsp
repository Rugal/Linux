<%-- 
    Document   : listStockLog
    Created on : Sep 17, 2013, 10:43:48 PM
    Author     : rugal
--%>

<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Two Dimensions</title>
	</head>
	<body>
		<table align="center" border="1">
			<caption>进货日志</caption>
			<tr>
				<th width="150">名称</th>
				<th width="40">数量</th>
				<th width="70">经手人</th>
				<th width="120">供应商</th>
				<th width="100"><a href="listStockLog.do?desc=${desc}&pageNo=${page.pageNo}">时间</a></th>
			</tr>
			<c:forEach var="it" items="${page.list}"  >
				<tr>
					<td>
						${it.gid.name}
					</td>
					<td>
						${it.quantity}
					</td>
					<td>
						${it.oid.name}
					</td>
					<td>
						${it.vid.name}
					</td>
					<td>
						<fmt:formatDate value="${it.formalLogTime}" pattern="yyyy-MM-dd"/> 
					</td>
				</tr>
			</c:forEach>
		</table>
		<p align="center">
			<c:if test="${page.pageNo>1}">
				<a href="listStockLog.do?desc=${not desc}&pageNo=${page.pageNo-1}">上一页</a>
			</c:if>
			<c:if test="${page.pageNo<page.totalPage}">
				<a href="listStockLog.do?desc=${not desc}&pageNo=${page.pageNo+1}">下一页</a>
			</c:if>
			<a href="stock.html">返回</a>

		</p>
	</body>
</html>
