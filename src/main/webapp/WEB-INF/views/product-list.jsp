<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />
<c:url var="productsUrl" value="/products" />
<c:url var="cssUrl" value="/assets/css/style.css" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Danh sách sản phẩm | Quản lý sản phẩm</title>
    <link rel="stylesheet" href="${cssUrl}" />
</head>
<body>
<div class="shell">
    <header class="topbar">
        <div>
            <a class="brand" href="${productsUrl}">Quản lý sản phẩm</a>
            <p class="subtitle">Bài kiểm tra cuối module JSP &amp; Servlet</p>
        </div>
        <div class="student-tag">MSSV: dtc245200120</div>
    </header>

    <main class="content">
        <div class="heading-row">
            <div>
                <h1>Danh sách sản phẩm</h1>
                <p class="muted">Theo dõi và cập nhật sản phẩm của cửa hàng.</p>
            </div>
            <a class="btn btn-primary" href="${productsUrl}?action=new">+ Thêm sản phẩm</a>
        </div>

        <c:choose>
            <c:when test="${status == 'created'}"><div class="alert alert-success" role="status">Đã thêm sản phẩm thành công.</div></c:when>
            <c:when test="${status == 'updated'}"><div class="alert alert-success" role="status">Đã cập nhật sản phẩm thành công.</div></c:when>
            <c:when test="${status == 'deleted'}"><div class="alert alert-success" role="status">Đã xóa sản phẩm thành công.</div></c:when>
            <c:when test="${status == 'not-found'}"><div class="alert alert-warning" role="alert">Không tìm thấy sản phẩm được yêu cầu.</div></c:when>
        </c:choose>

        <div class="panel">
            <div class="panel-head">
                <div>
                    <h2>Tất cả sản phẩm</h2>
                    <p class="muted small">Tổng cộng <strong><c:out value="${products.size()}" /></strong> sản phẩm</p>
                </div>
            </div>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Tên sản phẩm</th>
                        <th scope="col" class="right">Giá (VNĐ)</th>
                        <th scope="col" class="right">Số lượng</th>
                        <th scope="col" class="right">Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty products}">
                            <tr><td colspan="5" class="empty">Chưa có sản phẩm nào. Hãy chọn “Thêm sản phẩm” để bắt đầu.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td class="muted">#<c:out value="${p.id}" /></td>
                                    <td class="product-name"><c:out value="${p.name}" /></td>
                                    <td class="right price"><fmt:formatNumber value="${p.price}" minFractionDigits="0" maxFractionDigits="2" /></td>
                                    <td class="right"><fmt:formatNumber value="${p.quantity}" maxFractionDigits="0" /></td>
                                    <td class="right actions">
                                        <a class="btn btn-small btn-outline" href="${productsUrl}?action=edit&amp;id=${p.id}">Sửa</a>
                                        <form class="inline-form" action="${productsUrl}" method="post"
                                              onsubmit="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?');">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="id" value="${p.id}" />
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
                                            <button type="submit" class="btn btn-small btn-danger">Xóa</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
    <footer>Ứng dụng JSP · Java Servlet · JDBC · MySQL</footer>
</div>
</body>
</html>