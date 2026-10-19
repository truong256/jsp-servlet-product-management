<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="productsUrl" value="/products" />
<c:url var="cssUrl" value="/assets/css/style.css" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><c:choose><c:when test="${editing}">Sửa sản phẩm</c:when><c:otherwise>Thêm sản phẩm</c:otherwise></c:choose> | Quản lý sản phẩm</title>
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
    <main class="content form-content">
        <a href="${productsUrl}" class="back-link">← Quay lại danh sách</a>
        <div class="panel form-panel">
            <h1><c:choose><c:when test="${editing}">Chỉnh sửa sản phẩm #<c:out value="${product.id}" /></c:when><c:otherwise>Thêm sản phẩm mới</c:otherwise></c:choose></h1>
            <p class="muted">Các trường đánh dấu * là bắt buộc.</p>
            <form action="${productsUrl}" method="post" class="product-form">
                <input type="hidden" name="action" value="${editing ? 'update' : 'create'}" />
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
                <c:if test="${editing}"><input type="hidden" name="id" value="${product.id}" /></c:if>
                <div class="form-field">
                    <label for="name">Tên sản phẩm <span class="required">*</span></label>
                    <input type="text" id="name" name="name" required maxlength="150"
                           value="<c:out value='${formName}' />" placeholder="Ví dụ: Bàn phím cơ" />
                    <c:if test="${not empty formErrors.name}"><p class="field-error"><c:out value="${formErrors.name}" /></p></c:if>
                </div>
                <div class="form-grid">
                    <div class="form-field">
                        <label for="price">Giá (VNĐ) <span class="required">*</span></label>
                        <input type="number" id="price" name="price" required min="0" max="9999999999.99" step="0.01"
                               value="<c:out value='${formPrice}' />" placeholder="Ví dụ: 250000" />
                        <c:if test="${not empty formErrors.price}"><p class="field-error"><c:out value="${formErrors.price}" /></p></c:if>
                    </div>
                    <div class="form-field">
                        <label for="quantity">Số lượng <span class="required">*</span></label>
                        <input type="number" id="quantity" name="quantity" required min="0" max="1000000000" step="1"
                               value="<c:out value='${formQuantity}' />" placeholder="Ví dụ: 10" />
                        <c:if test="${not empty formErrors.quantity}"><p class="field-error"><c:out value="${formErrors.quantity}" /></p></c:if>
                    </div>
                </div>
                <div class="form-actions">
                    <a href="${productsUrl}" class="btn btn-outline">Hủy</a>
                    <button type="submit" class="btn btn-primary"><c:choose><c:when test="${editing}">Lưu thay đổi</c:when><c:otherwise>Thêm sản phẩm</c:otherwise></c:choose></button>
                </div>
            </form>
        </div>
    </main>
    <footer>Ứng dụng JSP · Java Servlet · JDBC · MySQL</footer>
</div>
</body>
</html>