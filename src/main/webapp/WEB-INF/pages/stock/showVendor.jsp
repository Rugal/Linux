<%-- 
    Document   : showVendor
    Created on : Sep 18, 2013, 1:21:17 AM
    Author     : rugal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Two Dimensions</title>
	</head>
	<body>
		<table border="1"  align="center">
			<caption>供货商信息</caption>
			<tr>
				<th>商户号</th>
				<td>${vendor.vid}</td>
			</tr>
			<tr>
				<th>名称</th>
				<td>${vendor.name}</td>
			</tr>
			<tr>
				<th>主营领域</th>
				<td>${vendor.field}</td>
			</tr>
			<tr>
				<th>联系方式</th>
				<td>${vendor.contact}</td>
			</tr>
			<tr>
				<th>网站</th>
				<td>${vendor.website}</td>
			</tr>
		</table>
		<p align="center">
			<a href="stock.html">返回</a>
		</p>
	</body>
</html>
