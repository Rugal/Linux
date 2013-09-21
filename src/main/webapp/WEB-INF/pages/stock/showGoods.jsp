<%-- 
    Document   : showGoods
    Created on : Sep 18, 2013, 1:21:06 AM
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
			<caption>商品信息</caption>
			<tr>
				<th width="100" align="right">商品号</th>
				<td width="300" align="left">${goods.gid}</td>
			</tr>
			<tr>
				<th width="100" align="right">名称</th>
				<td width="300" align="left">${goods.name}</td>
			</tr>
			<tr>
				<th width="100" align="right">单位</th>
				<td width="300" align="left">${goods.unit}</td>
			</tr>
			<tr>
				<th width="100" align="right">存货量</th>
				<td width="300" align="left">${goods.quantity}</td>
			</tr>
			<tr>
				<th width="100" align="right">定价</th>
				<td width="300" align="left">${goods.sellPrice}</td>
			</tr>
			<tr>
				<th width="100" align="right">供货商</th>
				<td width="300" align="left">
					<a href="showVendor.do?vid=${goods.vid.vid}">${goods.vid.name}</a>
				</td>
			</tr>
			<tr>
				<th width="100" align="right">杂项</th>
				<td width="300" align="left">
					<font color="white">${goods.stockPrice}</font>
				</td>
			</tr>
		</table>
		<p align="center">
			<a href="stock.html">返回</a>
		</p>
	</body>
</html>
