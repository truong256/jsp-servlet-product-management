<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="productsUrl" value="/products" />
<c:url var="cssUrl" value="/assets/css/style.css" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Lỗi hệ thống | Quản lý sản phẩm</title>
    <link rel="stylesheet" href="${cssUrl}" />
</head>
<body>
<div class="shell">
    <header class="topbar"><a class="brand" href="${productsUrl}">Quản lý sản phẩm</a></header>
    <main class="content form-content">
        <div class="panel error-panel">
            <h1>Không thể tải dữ liệu</h1>
            <p>Ứng dụng đang gặp lỗi khi kết nối hoặc truy vấn MySQL. Hãy kiểm tra MySQL đã chạy, đã import file SQL và thông tin trong <code>db.properties</code>.</p>
            <p>Chi tiết kỹ thuật được ghi trong log của Tomcat.</p>
            <a href="${productsUrl}" class="btn btn-primary">Thử lại</a>
        </div>
    </main>
</div>
</body>
</html>