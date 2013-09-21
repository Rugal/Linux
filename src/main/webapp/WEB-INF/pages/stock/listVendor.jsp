<%-- 
    Document   : listStockLog
    Created on : Sep 17, 2013, 10:43:48 PM
    Author     : rugal
--%>

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
			<caption>供货商信息列表</caption>
			<tr>
				<th width="150">名称</th>
				<th width="200">领域</th>
				<th width="110">联系方式</th>
				<th width="230">网址</th>
			</tr>
			<c:forEach var="it" items="${page.list}"  >
				<tr>
					<td>
						<a href="showVendor.do?vid=${it.vid}">${it.name}</a>
					</td>
					<td>
						${it.field}
					</td>
					<td>
						${it.contact}
					</td>
					<td>
						${it.website}
					</td>
				</tr>
			</c:forEach>
		</table>
		<p align="center">
			<c:if test="${page.pageNo>1}">
				<a href="listVendor.do?pageNo=${page.pageNo-1}">上一页</a>
			</c:if>
			<c:if test="${page.pageNo<page.totalPage}">
				<a href="listVendor.do?pageNo=${page.pageNo+1}">下一页</a>
			</c:if>
			<a href="stock.html">返回</a>

		</p>
	</body>
</html>
