package com.finalexam.product.web;

import com.finalexam.product.dao.ProductDAO;
import com.finalexam.product.model.Product;
import com.finalexam.product.util.ProductValidator;
import com.finalexam.product.util.ProductValidator.ValidationResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String LIST_JSP = "/WEB-INF/views/product-list.jsp";
    private static final String FORM_JSP = "/WEB-INF/views/product-form.jsp";
    private static final String ERROR_JSP = "/WEB-INF/views/error.jsp";
    private final ProductDAO dao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        ensureCsrfToken(request.getSession());
        String action = request.getParameter("action");
        try {
            if (action == null || "list".equals(action)) {
                showList(request, response);
            } else if ("new".equals(action)) {
                showForm(request, response, new Product(), false);
            } else if ("edit".equals(action)) {
                Long id = parseId(request.getParameter("id"));
                if (id == null) {
                    response.sendRedirect(productsUrl(request) + "?status=not-found");
                    return;
                }
                Optional<Product> existing = dao.findById(id);
                if (!existing.isPresent()) {
                    response.sendRedirect(productsUrl(request) + "?status=not-found");
                    return;
                }
                showForm(request, response, existing.get(), true);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Duong dan khong ton tai");
            }
        } catch (SQLException e) {
            showDatabaseError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        if (!validCsrfToken(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bieu mau khong hop le. Vui long tai lai trang.");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                save(request, response, false);
            } else if ("update".equals(action)) {
                save(request, response, true);
            } else if ("delete".equals(action)) {
                Long id = parseId(request.getParameter("id"));
                if (id == null) {
                    response.sendRedirect(productsUrl(request) + "?status=not-found");
                    return;
                }
                boolean deleted = dao.delete(id);
                response.sendRedirect(productsUrl(request) + (deleted ? "?status=deleted" : "?status=not-found"));
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hanh dong khong hop le");
            }
        } catch (SQLException e) {
            showDatabaseError(request, response, e);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response, boolean editing)
            throws SQLException, ServletException, IOException {
        Long id = editing ? parseId(request.getParameter("id")) : 0L;
        if (id == null) {
            response.sendRedirect(productsUrl(request) + "?status=not-found");
            return;
        }
        if (editing && !dao.findById(id).isPresent()) {
            response.sendRedirect(productsUrl(request) + "?status=not-found");
            return;
        }

        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");
        ValidationResult result = ProductValidator.validate(name, price, quantity);
        if (!result.isValid()) {
            // Giữ nguyên dữ liệu nhập để người dùng sửa lỗi trên form.
            request.setAttribute("formName", name == null ? "" : name);
            request.setAttribute("formPrice", price == null ? "" : price);
            request.setAttribute("formQuantity", quantity == null ? "" : quantity);
            request.setAttribute("formErrors", result.getErrors());
            showForm(request, response, new Product(id, name, null, 0), editing);
            return;
        }

        Product product = result.getProduct();
        product.setId(id);
        if (editing) {
            boolean updated = dao.update(product);
            response.sendRedirect(productsUrl(request) + (updated ? "?status=updated" : "?status=not-found"));
        } else {
            dao.insert(product);
            response.sendRedirect(productsUrl(request) + "?status=created");
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        request.setAttribute("products", dao.findAll());
        request.setAttribute("status", request.getParameter("status"));
        request.getRequestDispatcher(LIST_JSP).forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response,
                          Product product, boolean editing) throws ServletException, IOException {
        request.setAttribute("product", product);
        request.setAttribute("editing", editing);
        if (request.getAttribute("formErrors") == null) {
            request.setAttribute("formErrors", Collections.emptyMap());
            request.setAttribute("formName", product.getName() == null ? "" : product.getName());
            request.setAttribute("formPrice", product.getPrice() == null ? "" : product.getPrice().toPlainString());
            request.setAttribute("formQuantity", editing ? String.valueOf(product.getQuantity()) : "");
        }
        request.getRequestDispatcher(FORM_JSP).forward(request, response);
    }

    private void showDatabaseError(HttpServletRequest request, HttpServletResponse response, SQLException exception)
            throws ServletException, IOException {
        getServletContext().log("Loi ket noi/truy van MySQL", exception);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.getRequestDispatcher(ERROR_JSP).forward(request, response);
    }

    private static Long parseId(String rawId) {
        try {
            long id = Long.parseLong(rawId);
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String productsUrl(HttpServletRequest request) {
        return request.getContextPath() + "/products";
    }

    private static void ensureCsrfToken(HttpSession session) {
        if (session.getAttribute("csrfToken") == null) {
            session.setAttribute("csrfToken", UUID.randomUUID().toString());
        }
    }

    private static boolean validCsrfToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String submitted = request.getParameter("csrfToken");
        return session != null && submitted != null && submitted.equals(session.getAttribute("csrfToken"));
    }
}