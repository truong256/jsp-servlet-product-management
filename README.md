# BÀI KIỂM TRA CUỐI MODULE – QUẢN LÝ SẢN PHẨM

**Mã số sinh viên:** `dtc245200120`

Dự án Java Web dùng **JSP + Servlet + JDBC + MySQL**, đóng gói Maven WAR chạy trên **Apache Tomcat 9**.

## 1. Các chức năng đã làm

| Chức năng | Mô tả |
| --- | --- |
| Xem danh sách | Bảng có ID, tên sản phẩm, giá, số lượng, thao tác Sửa/Xóa |
| Thêm sản phẩm | Form nhập tên, giá, số lượng; kiểm tra hợp lệ và lưu MySQL |
| Sửa sản phẩm | Form tải dữ liệu hiện tại; sửa và cập nhật MySQL |
| Xóa sản phẩm | Bấm Xóa, trình duyệt hiện hộp thoại xác nhận, sau đó xóa bằng POST |
| Kết nối database | JDBC với PreparedStatement, MySQL Connector/J, có file tạo bảng và dữ liệu mẫu |

**Bổ sung:** Giao diện responsive, thông báo thành công, bảo vệ form bằng CSRF token, kiểm tra dữ liệu cả server lẫn HTML, thoát SQL injection bằng `PreparedStatement`, tự động đóng JDBC resources.

## 2. Yêu cầu môi trường

- Java **11 trở lên** (chạy tốt với Java 17 hoặc Java 21).
- **Apache Tomcat 9.x** (`javax.servlet`; không dùng trực tiếp trên Tomcat 10/11).
- **MySQL 8.0.16 trở lên**.
- **Maven 3.8+**.
- Có thể dùng IntelliJ IDEA hoặc Eclipse để mở thư mục chứa `pom.xml`.

## 3. Tạo cơ sở dữ liệu

**Cách 1 – MySQL Workbench:** Mở file `sql/product_management.sql`, chọn biểu tượng Execute.

**Cách 2 – Command Prompt/Terminal:** Chạy ở thư mục dự án:

```bash
mysql -u root -p < sql/product_management.sql
```

SQL sẽ tạo `product_management` và bảng `products` có cột `id`, `name`, `price`, `quantity`, sau đó thêm 5 sản phẩm mẫu nếu bảng đang trống. Script chạy lại không xóa dữ liệu hiện có.

## 4. Cấu hình kết nối

Mở `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/product_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8
db.username=root
db.password=
```

- Điền mật khẩu tài khoản MySQL sau `db.password=` nếu máy có mật khẩu.
- `localhost:3306` là mặc định; đổi nếu MySQL chạy trên host/cổng khác.
- Để tránh ghi mật khẩu trong code, có thể đặt các biến môi trường **DB_URL**, **DB_USER**, **DB_PASSWORD** (được ưu tiên hơn file `.properties`).
- Không đưa mật khẩu thật lên GitHub.

## 5. Build và chạy với Tomcat 9

Tại thư mục có `pom.xml`, chạy:

```bash
mvn clean test
mvn clean package
```

Sau build, có file `target/product-management.war`.

1. Copy `target/product-management.war` vào thư mục `webapps` của Apache Tomcat 9.
2. Khởi động Tomcat (`bin/startup.bat` trên Windows hoặc `bin/startup.sh` trên Linux).
3. Mở trình duyệt:

   **http://localhost:8080/product-management/**

> Nếu cổng Tomcat không phải 8080, thay địa chỉ tương ứng. Trước khi mở trang cần khởi động MySQL và chạy script SQL.

**Chạy trong IDE:** Import project dưới dạng Maven project, cấu hình Tomcat 9 và deploy artifact WAR/exploded, rồi truy cập theo context path do IDE cấu hình.

## 6. Kiểm thử thủ công

1. **Danh sách:** Mở trang, nhìn thấy 5 sản phẩm mẫu và đủ bốn cột ID, tên, giá, số lượng.
2. **Thêm:** Chọn *Thêm sản phẩm*, nhập `Webcam`, giá `499000`, số lượng `10`, lưu và kiểm tra có hàng mới.
3. **Sửa:** Chọn *Sửa* tại dòng `Webcam`, đổi giá `450000`, lưu và kiểm tra giá cập nhật.
4. **Xóa có xác nhận:** Bấm *Xóa*, chọn **Cancel** thì dòng vẫn còn; bấm lại và chọn **OK** thì dòng biến mất.
5. **Validation:** Thử tên trống, giá âm, số lượng âm, giá có hơn 2 chữ số thập phân, số lượng là số thập phân: form không được lưu.
6. **Không tìm thấy:** Truy cập `/products?action=edit&id=999999999` sẽ trở về danh sách và thông báo không tìm thấy.
7. **Lưu bền vững:** Khởi động lại Tomcat, dữ liệu đã thay đổi vẫn được lưu trong MySQL.

## 7. Cấu trúc thư mục

```text
dtc245200120_FinalExam/ (thư mục dự án sau giải nén)
├── pom.xml
├── README.md
├── sql/
│   └── product_management.sql
└── src/
    ├── main/
    │   ├── java/com/finalexam/product/
    │   │   ├── config/Database.java
    │   │   ├── dao/ProductDAO.java
    │   │   ├── model/Product.java
    │   │   ├── util/ProductValidator.java
    │   │   └── web/ProductServlet.java
    │   ├── resources/db.properties
    │   └── webapp/
    │       ├── index.jsp
    │       ├── assets/css/style.css
    │       └── WEB-INF/views/
    │           ├── product-list.jsp
    │           ├── product-form.jsp
    │           └── error.jsp
    └── test/java/com/finalexam/product/util/ProductValidatorTest.java
```

## 8. Sơ đồ xử lý

- Truy cập `/products` → `ProductServlet` → `ProductDAO.findAll()` → JSP danh sách.
- GET `/products?action=new` → JSP biểu mẫu thêm.
- POST `/products` với `action=create` → validate → `ProductDAO.insert()` → redirect về danh sách.
- GET `/products?action=edit&id=...` → `ProductDAO.findById()` → JSP form có sẵn dữ liệu.
- POST `action=update` → validate → `ProductDAO.update()` → redirect về danh sách.
- POST `action=delete` → kiểm tra CSRF → `ProductDAO.delete()` → redirect về danh sách.

## 9. Lỗi thường gặp

- **HTTP 404:** Kiểm tra đã copy `.war` vào Tomcat 9 và URL đúng `/product-management/`.
- **Lỗi 500/kết nối dữ liệu:** Kiểm tra MySQL đang chạy, file SQL đã thực thi, `db.username`/`db.password` chính xác.
- **Port 8080 đã bị dùng:** Sửa port trong `conf/server.xml` của Tomcat.
- **Dùng Tomcat 10 trở lên:** Dự án này sử dụng `javax.servlet` tương thích Tomcat 9. Tomcat 10 yêu cầu chuyển sang `jakarta.servlet` và JSTL mới.
- **Bấm lưu form báo 403:** Mở lại form từ giao diện, không gửi POST thiếu CSRF token.

## 10. Thông tin nộp bài

Nộp file **`dtc245200120_FinalExam.zip`** chứa toàn bộ source + SQL + README; không cần gửi thư mục `target`.

**Nguồn mã:** Mã nguồn bài này được viết riêng cho yêu cầu kiểm tra, không sao chép từ kho dự án bên ngoài. Phần dependency được khai báo chính thức bằng Maven trong `pom.xml`.