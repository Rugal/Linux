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
		<title>Two Dimension</title>
	</head>
	<body>
		<table align="center" border="1">
			<caption>存货信息</caption>
			<tr>
				<th width="200">名称</th>
				<th width="40">数量</th>
				<th width="40">单位</th>
				<th width="80">进价</th>
				<th width="80">定价</th>
				<th width="80">供货商号</th>
			</tr>
			<c:forEach var="it" items="${page.list}"  >
				<tr>
					<td>
						<!--Here need to work into hyper link-->
						<a href="showGoods.do?gid=${it.gid}">${it.name}</a>
					</td>
					<td>
						${it.quantity}
					</td>
					<td>
						${it.unit}
					</td>
					<td>
						${it.stockPrice}
					</td>
					<td>
						${it.sellPrice}
					</td>
					<td align="center">
						<!--Here need to work into hyper link-->
						${it.vid.vid}
					</td>
				</tr>
			</c:forEach>
		</table>
		<form action="" method="post">
			<!--TODO here needs to upgrade to a check box style search box-->
			<input type="checkbox"/>
		</form>
		<p align="center">
			<c:if test="${page.pageNo>1}">
				<a href="list.do?pageNo=${page.pageNo-1}">上一页</a>
			</c:if>
			<c:if test="${page.pageNo<page.totalPage}">
				<a href="list.do?pageNo=${page.pageNo+1}">下一页</a>
			</c:if>
			<a href="stock.html">返回</a>
		</p>
	</body>
</html>
