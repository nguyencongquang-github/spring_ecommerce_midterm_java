# README FOR SPRING ECOMMERCE PROJECT

Tổng quan
---------

**SpringCommerce** là một ứng dụng mua sắm trực tuyến đơn giản được xây dựng bằng **Java Spring Boot**. Mục tiêu của dự án là triển khai một Sản phẩm khả dụng tối thiểu (MVP) thể hiện các chức năng cơ bản của một nền tảng thương mại điện tử, bao gồm hiển thị danh sách sản phẩm, tìm kiếm và lọc, quản lý giỏ hàng, và đặt hàng.

Trong ứng dụng này:

*   Khách hàng có thể tìm kiếm và lọc sản phẩm theo nhiều tiêu chí như danh mục, tên, mô tả của sản phẩm.
*   Khách hàng có thể xem chi tiết sản phẩm và thêm vào giỏ hàng.
*   Quy trình thanh toán được triển khai, nhưng việc thanh toán thực hiện bằng tiền mặt khi giao hàng.
*   Dự án sử dụng **Spring Boot** cho backend, **ReactJs** cho frontend và **MySQL** để lưu trữ dữ liệu.
    

Các Tính Năng Chính
-------------------
1.  **Đăng nhập, đăng kí**: Người dùng có thể đăng nhập, đăng kí theo email và mật khẩu.
2.  **Tìm kiếm & Lọc Sản phẩm**: Người dùng có thể lọc sản phẩm dựa trên danh mục, tên, mô tả.
3.  **Giỏ Hàng**: Người dùng có thể thêm sản phẩm vào giỏ hàng và xem nội dung giỏ hàng.
4.  **Quản lý đơn hàng**: Admin có thể cập nhật trạng thái đơn hàng
5.  **Quản lý sản phẩm**: Admin có thể CRUD sản phẩm
6.  **Quản lý danh mục**: Admin có thể CRUD danh mục sản phẩm
    

Mục Lục
-------

1.  [Các Nguyên Tắc, Mẫu Thiết Kế](#principles-and-patterns)

2.  [Kiến Trúc Thiết Kế ](#architectural-design)
    
3.  [Công nghệ Sử dụng](#technologies-used)
    
4.  [Cấu trúc Dự án](#project-structure)
    
5.  [Sơ đồ ERD](#erd-diagram)
    
6.  [Các Endpoint API](#api-endpoints)
    
7.  [Kiểm thử đơn vị](#unit-tests)
    
8.  [Cài đặt và Thiết lập](#installation-and-setup)
    
9.   [Video Demo](#demo-video)
    
    

1\. Các Nguyên Tắc, Mẫu Thiết Kế 
------------------------------------------------------------------------------

### **Nguyên Tắc SOLID**
Nguyên tắc SOLID là cơ sở để tạo ra các hệ thống phần mềm dễ duy trì, linh hoạt và có thể mở rộng. Trong dự án này, chúng tôi áp dụng các nguyên tắc SOLID để đảm bảo rằng ứng dụng vừa mô-đun, dễ mở rộng trong tương lai. Dưới đây là cách áp dụng các nguyên tắc này:
*   **S - Nguyên Tắc Trách Nhiệm Đơn (SRP)**: Mỗi lớp trong ứng dụng có một trách nhiệm duy nhất. Ví dụ, lớp ProductService xử lý logic nghiệp vụ liên quan đến sản phẩm, trong khi lớp OrderService quản lý các hoạt động liên quan đến đơn hàng.
*   **O - Nguyên Tắc Mở/Đóng (OCP)**: Hệ thống được thiết kế để mở rộng nhưng không thay đổi mã nguồn hiện tại. Ví dụ, chúng ta có thể thêm các bộ lọc mới cho sản phẩm mà không cần thay đổi mã nguồn của lớp sản phẩm, chỉ cần thêm các lớp mới thực hiện các giao diện chung.
*   **L - Nguyên Tắc Thay Thế Liskov (LSP)**: Các lớp con có thể thay thế lớp cha mà không làm sai lệch tính đúng đắn của chương trình. Điều này rất quan trọng khi xử lý các thực thể như Product hoặc Order, nơi các lớp con có thể được sử dụng một cách liền mạch.
*   **I - Nguyên Tắc Phân Tách Giao Diện (ISP)**: Thay vì có các giao diện lớn và phức tạp, chúng tôi chia chúng thành các giao diện nhỏ, đặc thù cho từng đối tượng. Ví dụ, các giao diện riêng biệt cho ProductService và UserService đảm bảo rằng mỗi dịch vụ chỉ xử lý trách nhiệm của riêng mình.  
*   **D - Nguyên Tắc Đảo Ngược Phụ Thuộc (DIP)**: Các mô-đun cấp cao phụ thuộc vào các trừu tượng, không phụ thuộc vào các lớp cụ thể. OrderService phụ thuộc vào một giao diện, thay vì một lớp triển khai cụ thể, điều này giúp dễ dàng thay thế lớp cơ sở dữ liệu hoặc kho dữ liệu. 

### **Mẫu Thiết Kế**
Ứng dụng tuân theo một số mẫu thiết kế chính:
*   **Factory Pattern**: Được sử dụng để tạo các đối tượng như Product, User hoặc Order một cách động.
*   **Singleton Pattern**: Đảm bảo rằng các thành phần dịch vụ trong ứng dụng như UserService chỉ được khởi tạo một lần trong suốt vòng đời của ứng dụng.
*   **Repository Pattern**: Trừu tượng hóa lớp truy xuất dữ liệu và cung cấp một giao diện đơn giản để thực hiện các truy vấn và thao tác trên dữ liệu trong cơ sở dữ liệu.
    
### **Tiêm Phụ Thuộc (Dependency Injection)**
Dependency Injection (DI) là một kỹ thuật thiết kế phần mềm trong lập trình hướng đối tượng, dùng để giảm thiểu sự phụ thuộc giữa các lớp (class) với nhau, giúp code dễ hiểu, dễ bảo trì và nâng cấp hơn.
Về cơ bản, DI hoạt động bằng cách thay vì lớp tự tạo ra các đối tượng phụ thuộc (dependency) của mình, nó sẽ nhận các đối tượng đó được "tiêm" vào từ bên ngoài. Điều này cho phép:
*  **Tách biệt mối quan tâm**: Lớp chỉ tập trung vào logic riêng của mình, không cần lo việc tạo ra các dependency, khiến code rõ ràng và dễ quản lý hơn.
*  **Liên kết lỏng lẻo**: Lớp phụ thuộc ít vào cách thức tạo ra các dependency, dễ dàng thay thế các dependency khác nhau trong các môi trường khác nhau, ví dụ: testing, production…
* **Tính linh hoạt cao**: Kiểm soát tốt hơn cách tạo và quản lý các dependency, thuận tiện cho việc mở rộng và tùy chỉnh hệ thống.

Trong dự án này, tôi chủ yếu sử dụng `Constructor Injection` là một cách phổ biến và được ưu chuộng nhất. Các phụ thuộc được truyền vào đối tượng thông qua hàm tạo (constructor) của lớp. Điều này giúp đảm bảo tính rõ ràng và chặt chẽ trong việc thiết lập các mối quan hệ giữa các lớp.
Nhờ sử dụng Dependency Injection (DI), các phụ thuộc giữa các lớp trong dự án trở nên lỏng lẻo, giúp mã nguồn dễ bảo trì, mở rộng và kiểm tra. DI giúp tách biệt các lớp, dễ dàng thay thế phụ thuộc mà không làm ảnh hưởng đến phần còn lại của ứng dụng.
Tuy nhiên, DI cũng có những hạn chế. Việc triển khai và cấu hình DI có thể phức tạp, đặc biệt với các ứng dụng lớn, gây khó khăn trong việc phát triển nhanh chóng và quản lý các phụ thuộc, nhất là đối với người mới làm quen với DI.

### **Inversion of Control (IoC) - Đảo Ngược Kiểm Soát**

Inversion of Control (IoC) là một nguyên tắc thiết kế phần mềm, trong đó dòng chảy điều khiển của ứng dụng bị đảo ngược. Thay vì ứng dụng kiểm soát luồng điều khiển, IoC sử dụng các framework như Spring để quản lý vòng đời đối tượng, tiêm dịch vụ và cấu hình. Điều này giúp tách biệt logic nghiệp vụ khỏi các chi tiết cấu hình, khiến mã nguồn trở nên mô-đun hơn, giảm sự phụ thuộc giữa các lớp và dễ dàng bảo trì.

Trong dự án này, việc áp dụng IoC thông qua Spring Boot giúp giảm tải cho lập trình viên trong việc quản lý vòng đời của các đối tượng và dịch vụ. Nhờ đó, mã nguồn trở nên linh hoạt, dễ mở rộng và dễ dàng thay đổi khi cần thiết.

2\. Kiến Trúc Thiết Kế 
-------------------------------------

### **Kiến trúc RESTful API**
RESTful API (Representational State Transfer) là một kiến trúc phần mềm mà tôi đã sử dụng trong dự án này để xây dựng các dịch vụ web. RESTful API cho phép các ứng dụng giao tiếp với nhau qua giao thức HTTP, sử dụng các phương thức HTTP tiêu chuẩn như GET, POST, PUT, DELETE để thao tác với các tài nguyên.

![markdown](https://topdev.vn/blog/wp-content/uploads/2019/04/restful-api.jpg)

#### **Cấu trúc và Nguyên tắc của RESTful API**
Khi tôi xây dựng RESTful API, tôi tuân thủ các nguyên tắc cơ bản như sau:
1.  **Tài nguyên (Resources)**: Tài nguyên trong RESTful API có thể là bất kỳ đối tượng nào trong hệ thống mà tôi muốn truy cập, ví dụ như người dùng, sản phẩm, đơn hàng. Mỗi tài nguyên sẽ có một URL riêng biệt để truy xuất.
    *   */users* để truy xuất danh sách người dùng. 
    *   */auth/login* để đăng nhập vào hệ thống.  
2.  **Phương thức HTTP**: RESTful API sử dụng các phương thức HTTP để thao tác với tài nguyên:
    *   **GET**: Lấy thông tin từ server.
    *   **POST**: Tạo mới một tài nguyên.
    *   **PUT**: Cập nhật tài nguyên đã tồn tại.
    *   **DELETE**: Xóa tài nguyên.
3.  **Stateless**: Mỗi yêu cầu từ client phải chứa đầy đủ thông tin để server xử lý mà không cần phụ thuộc vào trạng thái của yêu cầu trước đó.
4.  **Cacheable**: Dữ liệu trả về từ server có thể được lưu vào bộ nhớ cache để cải thiện hiệu suất.
5.  **Uniform Interface**: RESTful API cung cấp một giao diện nhất quán và dễ hiểu, giúp việc xác định tài nguyên và các thao tác trên tài nguyên trở nên rõ ràng.

#### **Cách thức hoạt động của RESTful API trong Dự án này**
Trong dự án của tôi, front-end được xây dựng bằng ReactJS, còn back-end sử dụng Spring Boot. Cả hai giao tiếp với nhau thông qua RESTful API. Cụ thể, khi người dùng gửi yêu cầu từ front-end (ví dụ: nhấn nút để xem sản phẩm), yêu cầu này sẽ được gửi dưới dạng HTTP request tới back-end, nơi Spring Boot xử lý và trả về kết quả.

![markdown](https://topdev.vn/blog/wp-content/uploads/2019/04/restful-rest-diagram-api.jpg)

1.  **Client** (ReactJS) gửi yêu cầu tới server thông qua một API endpoint, ví dụ: GET /products.
2.  **Server** (Spring Boot) nhận yêu cầu, xử lý (chẳng hạn như truy vấn cơ sở dữ liệu) và trả về kết quả, ví dụ: danh sách sản phẩm dưới dạng JSON.
3.  **Client** nhận dữ liệu và hiển thị nó cho người dùng.

#### **Lợi ích của việc sử dụng RESTful API trong Dự án này**
Sử dụng RESTful API trong dự án này giúp tôi đạt được nhiều lợi ích quan trọng:

1.  **Tách biệt Front-end và Back-end**: Việc sử dụng RESTful API giúp tôi tách biệt rõ ràng giữa giao diện người dùng (front-end) và logic xử lý (back-end). Điều này không chỉ giúp hai phần làm việc độc lập mà còn tạo điều kiện cho việc phát triển song song, đồng thời dễ dàng thay thế một phần mà không ảnh hưởng đến phần còn lại.
2.  **Mở rộng và bảo trì dễ dàng**: RESTful API giúp tôi dễ dàng mở rộng hệ thống khi cần thêm tính năng mới mà không làm gián đoạn các phần khác của ứng dụng. Bên cạnh đó, việc bảo trì các phần riêng biệt cũng trở nên dễ dàng hơn.
3.  **Tính linh hoạt và tương thích cao**: API cho phép hệ thống của tôi giao tiếp dễ dàng với các dịch vụ bên ngoài, chẳng hạn như thanh toán trực tuyến, gửi email, hoặc tích hợp với các hệ thống khác. Bằng cách này, tôi có thể mở rộng chức năng mà không phải thay đổi cấu trúc cốt lõi của ứng dụng.
4.  **Khả năng tái sử dụng và kiểm tra**: API có thể được sử dụng lại ở nhiều nơi và dễ dàng kiểm tra. Việc sử dụng RESTful API giúp tôi xây dựng các chức năng mà không phải lo lắng về cách thức hoạt động của các phần khác trong ứng dụng.

3\. Công nghệ sử dụng
-------------------------------------
### Front-end: ReactJS 
ReactJS là thư viện JavaScript được sử dụng để phát triển giao diện người dùng (UI) cho ứng dụng web. Trong dự án này, tôi đã chọn ReactJS vì các lý do sau:

*  **Tạo giao diện động và tương tác**: ReactJS cho phép tôi xây dựng các UI động, nơi các thay đổi được phản ánh ngay lập tức mà không cần phải tải lại trang. Điều này rất quan trọng khi tôi cần một ứng dụng có tính tương tác cao như quản lý đơn hàng, sản phẩm, và người dùng.
*  **Component-based Architecture**: React cho phép tôi chia giao diện thành các component nhỏ và tái sử dụng, giúp giảm thiểu việc lặp lại mã nguồn. Điều này giúp cải thiện tính bảo trì của ứng dụng khi có thay đổi hoặc mở rộng tính năng.
*  **Quản lý trạng thái với Redux**: Để quản lý trạng thái toàn cục của ứng dụng, tôi sử dụng Redux, giúp đồng bộ hóa dữ liệu giữa các component và dễ dàng thao tác với dữ liệu trong toàn bộ ứng dụng.
*  **Dễ dàng tích hợp với API RESTful**: ReactJS rất dễ dàng để tích hợp với các API RESTful mà tôi xây dựng ở phía back-end bằng Spring Boot. Điều này cho phép ứng dụng của tôi thực hiện các yêu cầu HTTP (GET, POST, PUT, DELETE) với back-end và hiển thị dữ liệu động cho người dùng.

### Backend: Spring Boot
Spring Boot là framework Java được sử dụng để phát triển các dịch vụ back-end cho ứng dụng của tôi. Dưới đây là một số lý do tôi chọn Spring Boot:

*  **Cấu hình tự động**: Spring Boot cung cấp các tính năng cấu hình tự động, giúp tôi giảm bớt công sức trong việc cấu hình ứng dụng. Các tính năng như quản lý kết nối cơ sở dữ liệu, cấu hình bảo mật, và cấu hình máy chủ web đã được thiết lập sẵn, giúp tôi tập trung vào việc phát triển tính năng của ứng dụng.
*  **Tạo RESTful API dễ dàng**: Spring Boot hỗ trợ xây dựng các API RESTful mạnh mẽ, rất phù hợp với yêu cầu của dự án này. Tôi sử dụng các annotation như @RestController, @GetMapping, @PostMapping, v.v. để triển khai các endpoint cho các chức năng như tạo, sửa, xóa và lấy thông tin sản phẩm, đơn hàng và người dùng.
*  **Bảo mật và quản lý người dùng**: Với Spring Security, tôi dễ dàng thêm các tính năng bảo mật như xác thực và phân quyền người dùng vào ứng dụng. Điều này đảm bảo rằng chỉ những người dùng có quyền mới có thể truy cập vào các tài nguyên hoặc thực hiện các hành động nhất định.
*  **Tích hợp với MySQL**: Spring Boot tích hợp dễ dàng với MySQL thông qua Spring Data JPA, giúp tôi thao tác với cơ sở dữ liệu một cách hiệu quả mà không cần phải viết nhiều SQL thủ công. Các entity của tôi được tự động ánh xạ với các bảng trong MySQL, giúp việc lưu trữ và truy vấn dữ liệu trở nên dễ dàng và thuận tiện.

### Database: MySQL
MySQL là hệ quản trị cơ sở dữ liệu quan hệ (RDBMS) được sử dụng để lưu trữ dữ liệu trong dự án này. Các lợi ích chính khi sử dụng MySQL bao gồm:

*  **Hiệu suất cao và dễ dàng mở rộng**: MySQL cung cấp khả năng xử lý các truy vấn nhanh chóng và hỗ trợ các chỉ mục để tối ưu hóa hiệu suất, rất phù hợp cho các ứng dụng yêu cầu xử lý dữ liệu lớn, như quản lý sản phẩm, đơn hàng và người dùng trong dự án này.
*  **Tính tương thích với Spring Boot**: MySQL tích hợp rất tốt với Spring Boot thông qua Spring Data JPA, giúp tôi thao tác dữ liệu một cách nhanh chóng mà không cần phải viết quá nhiều mã SQL phức tạp.
*  **Quản lý dữ liệu dễ dàng**: Với MySQL, tôi có thể dễ dàng thực hiện các thao tác CRUD (Create, Read, Update, Delete) đối với dữ liệu người dùng, sản phẩm, đơn hàng thông qua các repository trong Spring Data JPA.

### Cloud: AWS (Amazon Web Services)
Trong dự án này, tôi áp dụng AWS vào việc lưu trữ hình ảnh sản phẩm thông qua AWS S3. AWS S3 cung cấp dịch vụ lưu trữ đám mây đáng tin cậy để lưu trữ các tệp như hình ảnh sản phẩm, tài liệu hướng dẫn và các tệp khác. Điều này giúp tôi quản lý các tệp đính kèm trong ứng dụng một cách dễ dàng và hiệu quả. Trong thời gian tới tôi sẽ cố gắng hoàn thiện sản phẩm và triển khai lên máy chủ EC2 của AWS. 

### Security: Spring Security

Spring Security là một trong những công nghệ quan trọng được tôi áp dụng để bảo vệ ứng dụng. Dưới đây là cách Spring Security được sử dụng trong dự án này:

*   **Xác thực người dùng (Authentication)**:Spring Security cung cấp một cơ chế mạnh mẽ để xác thực người dùng thông qua việc tích hợp với cơ sở dữ liệu hoặc các dịch vụ xác thực bên ngoài. Trong dự án này, tôi sử dụng cơ chế xác thực dựa trên tên đăng nhập và mật khẩu được lưu trữ trong cơ sở dữ liệu MySQL.
    
*   **Phân quyền (Authorization)**:Với Spring Security, tôi có thể dễ dàng kiểm soát quyền truy cập vào các tài nguyên hoặc chức năng cụ thể của ứng dụng. Các vai trò như _admin_ và _user_ được định nghĩa để xác định ai có quyền thực hiện các hành động như quản lý sản phẩm, đặt hàng hoặc xem thông tin.
    
*   **JWT (JSON Web Token)**:Tôi sử dụng JWT để quản lý phiên đăng nhập người dùng. Sau khi xác thực thành công, một token JWT được tạo ra và gửi lại cho người dùng. Token này được sử dụng để xác thực các yêu cầu tiếp theo, giúp loại bỏ sự phụ thuộc vào lưu trữ trạng thái phiên (session) trên máy chủ.
    
*   **Bảo vệ endpoint API**:Spring Security cho phép tôi định cấu hình các endpoint API chỉ được truy cập bởi những người dùng có quyền hợp lệ. Ví dụ:
    
    *   Các endpoint liên quan đến quản lý sản phẩm chỉ được truy cập bởi _admin_.
        
    *   Các endpoint liên quan đến đặt hàng hoặc xem giỏ hàng chỉ dành cho người dùng đã đăng nhập.
        
*   **Mã hóa mật khẩu**:Tôi sử dụng BCryptPasswordEncoder để mã hóa mật khẩu người dùng trước khi lưu vào cơ sở dữ liệu, đảm bảo rằng dữ liệu nhạy cảm luôn được bảo vệ.
    
*   **Cấu hình dễ dàng**:Spring Security hỗ trợ cấu hình linh hoạt thông qua file SecurityConfig. Tôi sử dụng các annotation như @EnableWebSecurity và triển khai các phương thức như configure(HttpSecurity http) để kiểm soát luồng xác thực và phân quyền.


4\. Cấu trúc dự án
-------------------------------------
## Backend
### **Controller**
Controller là nơi xử lý các yêu cầu HTTP từ phía client. Trong Spring Boot, các lớp controller sử dụng annotation @RestController hoặc @Controller để đánh dấu và xử lý các endpoint của API. Controller nhận các yêu cầu từ phía người dùng, gọi các dịch vụ liên quan và trả về kết quả (dữ liệu hoặc thông báo).
**Chức năng:**
*   Xử lý các yêu cầu GET, POST, PUT, DELETE.
*   Gửi yêu cầu đến các service tương ứng.
*   Đảm bảo trả về các dữ liệu dưới định dạng JSON hoặc các phản hồi thích hợp.    

### **Service**
Service chứa logic nghiệp vụ của ứng dụng. Các lớp service thực hiện các phép toán, xử lý các yêu cầu và thực hiện các hành động cần thiết trong hệ thống. Các lớp này thường được đánh dấu với annotation @Service.
**Chức năng:**
*   Chứa logic xử lý nghiệp vụ.    
*   Tương tác với repository để truy vấn hoặc lưu trữ dữ liệu.    
*   Cung cấp các phương thức để controller gọi và trả về kết quả cho người dùng.
    
### **Repository**
Repository là lớp để tương tác với cơ sở dữ liệu. Trong Spring Boot, tôi sử dụng JpaRepository hoặc CrudRepository từ Spring Data JPA để dễ dàng truy vấn và thao tác với dữ liệu. Repository giúp thực hiện các thao tác như tìm kiếm, lưu trữ và cập nhật dữ liệu.
**Chức năng:**
*   Tương tác trực tiếp với cơ sở dữ liệu. 
*   Cung cấp các phương thức mặc định để truy vấn, lưu và xóa dữ liệu.    
*   Được Spring Data JPA tự động triển khai.
    
### **Entity**
Entity là các lớp mô phỏng các bảng trong cơ sở dữ liệu. Mỗi entity đại diện cho một bảng dữ liệu trong hệ thống, và các trường trong entity tương ứng với các cột trong bảng. Các lớp entity được đánh dấu với annotation @Entity.
**Chức năng:**
*   Định nghĩa các đối tượng dữ liệu trong hệ thống.    
*   Ánh xạ các trường trong entity vào các cột trong cơ sở dữ liệu.   
*   Xử lý các mối quan hệ giữa các bảng (1-1, 1-n, n-n).   

### **Enums**
Enums chứa các giá trị cố định trong ứng dụng. Chúng giúp định nghĩa các trạng thái hoặc loại dữ liệu có giới hạn, ví dụ như các trạng thái đơn hàng, mức độ quyền hạn người dùng, hoặc các mã lỗi.
**Chức năng:**
*   Cung cấp các giá trị cố định cho các trường hợp đặc biệt.   
*   Tăng cường tính bảo mật và dễ dàng mở rộng khi có thêm các trạng thái hoặc giá trị mới.
    
### **Exception**
Exception bao gồm các lớp xử lý ngoại lệ (errors). Tôi đã tạo các lớp exception tùy chỉnh để xử lý các lỗi người dùng hoặc hệ thống một cách chi tiết và dễ quản lý. Các lớp này thường được đánh dấu với annotation @ResponseStatus để trả về mã trạng thái HTTP thích hợp.
**Chức năng:**
*   Xử lý các lỗi xảy ra trong quá trình xử lý yêu cầu.    
*   Cung cấp thông tin chi tiết về lỗi cho người dùng.   
*   Đảm bảo ứng dụng không bị dừng đột ngột mà luôn trả về phản hồi hợp lý.   

### **Mapper**
Mapper là các lớp dùng để chuyển đổi giữa các đối tượng khác nhau, ví dụ như từ DTO sang entity và ngược lại. Tôi đã sử dụng thư viện MapStruct hoặc các phương thức thủ công để thực hiện việc ánh xạ này.
**Chức năng:**
*   Chuyển đổi giữa các lớp DTO và entity. 
*   Giảm bớt sự lặp lại khi làm việc với các đối tượng tương tự. 

### **Specification**
Specification được sử dụng để xây dựng các truy vấn động trong Spring Data JPA. Nó cho phép tạo ra các truy vấn linh hoạt và phức tạp hơn mà không cần phải viết trực tiếp các câu lệnh SQL.
**Chức năng:**
*   Xây dựng các truy vấn động cho các yêu cầu tìm kiếm phức tạp.    
*   Tăng cường khả năng tái sử dụng mã và mở rộng ứng dụng.  

### **Security**
Security chịu trách nhiệm bảo mật ứng dụng. Trong phần này, tôi đã sử dụng Spring Security để xử lý các yêu cầu về xác thực (authentication) và phân quyền (authorization). Security đảm bảo rằng chỉ người dùng có quyền mới có thể truy cập vào các tài nguyên của hệ thống.
**Chức năng:**
*   Xác thực người dùng (login/logout).   
*   Phân quyền truy cập vào các endpoint API.   
*   Bảo vệ các endpoint khỏi các mối đe dọa từ bên ngoài.   

### **DTO (Data Transfer Object)**
DTO là các lớp dùng để truyền tải dữ liệu giữa các lớp hoặc giữa server và client. DTO giúp tối ưu hóa dữ liệu trả về và gửi đi, chỉ truyền các trường cần thiết thay vì toàn bộ entity.
**Chức năng:**
*   Giảm thiểu dữ liệu không cần thiết được truyền tải.    
*   Cung cấp một lớp trung gian giữa các tầng trong ứng dụng.

## **Frontend**
Dự án frontend của tôi được xây dựng với ReactJS, giúp tạo ra giao diện người dùng động và dễ tương tác. Dưới đây là cấu trúc chính của dự án frontend và mô tả các phần quan trọng:

### **Component**
Components là các phần tử cơ bản của giao diện người dùng trong React. Mỗi component đại diện cho một phần giao diện độc lập và có thể tái sử dụng, giúp tăng tính mô-đun và bảo trì cho ứng dụng.

**Chức năng:**

*   **Chia nhỏ giao diện:** Mỗi component đại diện cho một phần nhỏ của giao diện như form đăng nhập, danh sách sản phẩm, hoặc biểu đồ thống kê.
*   **Quản lý trạng thái:** Các component có thể quản lý trạng thái của chúng thông qua useState hoặc thông qua Redux (nếu sử dụng).  
*   **Tái sử dụng:** Một component có thể được sử dụng nhiều lần trong ứng dụng ở các nơi khác nhau.
    
Các loại component thường có trong dự án:
*   **Functional Components:** Sử dụng function để định nghĩa component. 
*   **Class Components:** Được sử dụng khi cần quản lý lifecycle methods (dù giờ đã ít sử dụng).  
*   **Stateful Components:** Những component cần lưu trữ và quản lý trạng thái.  
*   **Stateless Components:** Chỉ nhận props và render UI mà không cần quản lý trạng thái.

### **Service**
Services trong React thường dùng để tổ chức các logic xử lý dữ liệu hoặc giao tiếp với backend (API). Chúng không trực tiếp render giao diện, mà chỉ thực hiện các tác vụ như gọi API, xử lý dữ liệu, và trả kết quả về cho các component.
**Chức năng:**

*   **Giao tiếp với backend:** Dùng axios hoặc fetch để gọi các API từ backend (Spring Boot trong dự án của tôi). 
*   **Quản lý logic nghiệp vụ:** Chứa các hàm để xử lý các logic như đăng nhập, lấy dữ liệu sản phẩm, thanh toán đơn hàng, v.v.  
*   **Tách biệt logic khỏi component:** Đảm bảo các component chỉ tập trung vào việc hiển thị UI, còn logic nghiệp vụ được xử lý trong service.
  
Ví dụ: Một service có thể chứa hàm để gọi API lấy danh sách sản phẩm từ backend, rồi trả kết quả về cho component cần hiển thị.

### **Style**
Phần này chứa các file và thư mục liên quan đến kiểu dáng của ứng dụng, bao gồm cả các file CSS hoặc SCSS (nếu sử dụng). Các file style giúp định nghĩa giao diện, màu sắc, font chữ, và bố cục của ứng dụng.
**Chức năng:**

*   **Thiết kế giao diện:** Cung cấp các quy tắc CSS để giao diện người dùng trông bắt mắt và dễ sử dụng. 
*   **Responsive:** Đảm bảo giao diện thích ứng tốt trên các loại màn hình khác nhau như desktop, tablet, và mobile. 
*   **Tái sử dụng:** Các style có thể được tái sử dụng trong nhiều component, giúp duy trì tính nhất quán trong thiết kế.
    
Một số cách tôi thường áp dụng trong việc quản lý style:

*   **CSS Modules:** Tạo các file CSS riêng cho từng component, giúp tránh sự xung đột tên lớp.
*   **Styled-components:** Sử dụng thư viện này để viết CSS trực tiếp trong các component bằng cách sử dụng JavaScript.


5\. Entity Diagram
-------------------------------------

### **Sơ đồ quan hệ ERD**
![erd](https://github.com/user-attachments/assets/84b26864-f011-4d2d-9a1e-513fcae90ab1)


### **Mô tả các bảng và quan hệ trong sơ đồ ERD**
1.  **Bảng users**:
    
    *   Lưu trữ thông tin người dùng như tên, email, mật khẩu, số điện thoại, vai trò, và thời gian tạo tài khoản.      
    *   id là khóa chính (PK).      
    *   email là trường duy nhất.
        
2.  **Bảng addresses**:
    
    *   Lưu trữ địa chỉ của người dùng.     
    *   user\_id là khóa ngoại (FK) tham chiếu đến bảng users.      
    *   id là khóa chính (PK).
        
3.  **Bảng categories**:
    
    *   Lưu trữ các danh mục sản phẩm.        
    *   id là khóa chính (PK).        
    *   name là trường duy nhất.
        
4.  **Bảng products**:
    
    *   Lưu trữ thông tin sản phẩm như tên, mô tả, URL ảnh, giá và danh mục sản phẩm.       
    *   category\_id là khóa ngoại (FK) tham chiếu đến bảng categories.       
    *   id là khóa chính (PK).
        
5.  **Bảng orders**:
    
    *   Lưu trữ thông tin đơn hàng, bao gồm tổng giá trị đơn hàng và thời gian tạo đơn.       
    *   id là khóa chính (PK).
        
6.  **Bảng order_items**:
    
    *   Lưu trữ các mục trong mỗi đơn hàng.     
    *   user\_id là khóa ngoại (FK) tham chiếu đến bảng users.     
    *   product\_id là khóa ngoại (FK) tham chiếu đến bảng products.     
    *   order\_id là khóa ngoại (FK) tham chiếu đến bảng orders.     
    *   id là khóa chính (PK).
        

### **Các quan hệ giữa các bảng:**

*   **Bảng users ↔ Bảng addresses**:   
    *   Một người dùng có thể có nhiều địa chỉ (quan hệ 1-n).        
    *   Mối quan hệ được thể hiện qua trường user\_id trong bảng addresses, tham chiếu đến id trong bảng users.
        
*   **Bảng categories ↔ Bảng products**:   
    *   Một danh mục có thể chứa nhiều sản phẩm (quan hệ 1-n).       
    *   Mối quan hệ được thể hiện qua trường category\_id trong bảng products, tham chiếu đến id trong bảng categories.
        
*   **Bảng users ↔ Bảng order_items**:  
    *   Một người dùng có thể có nhiều mục trong đơn hàng (quan hệ 1-n).      
    *   Mối quan hệ được thể hiện qua trường user\_id trong bảng order\_items, tham chiếu đến id trong bảng users.
        
*   **Bảng products ↔ Bảng order_items**:    
    *   Một sản phẩm có thể xuất hiện trong nhiều mục của đơn hàng (quan hệ 1-n).       
    *   Mối quan hệ được thể hiện qua trường product\_id trong bảng order\_items, tham chiếu đến id trong bảng products.
        
*   **Bảng orders ↔ Bảng order_items**:    
    *   Một đơn hàng có thể có nhiều mục (quantities) (quan hệ 1-n).     
    *   Mối quan hệ được thể hiện qua trường order\_id trong bảng order\_items, tham chiếu đến id trong bảng orders.


  
6\. API Enpoints
-------------------------------------
## Auth
### **Register** 
```bash
curl --location 'http://localhost:8080/auth/register' \\
\--header 'Content-Type: application/json' \\
\--data-raw '{
    "email": "nguyencongquang2k4@gmail.com",
    "name": "Nguyen Cong Quang",
    "phoneNumber": "0389171890",
    "password": "123456"
}'
```
### **Login** 

```bash
curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "admin@gmail.com",
    "password": "admin"
}'
```

## User

### Get all users (**Only Admin**)
```bash
curl --location 'http://localhost:8080/user/get-all' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ'
```

### Get user details 
```bash
curl --location 'http://localhost:8080/user/my-info' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ'
```

## Product

### Get all products
```bash
curl --location 'http://localhost:8080/product/get-all' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJxdWFuZ2l0ZXI3NEBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI1NTcsImV4cCI6MTc0ODMyNDU1N30.Oc0XAkTHyq576gNhN5OtRN-_J0NizeL6JkGOlQ3q0Oim_qIlsmnLFfFUbxcBTvFv'
```

### Get product details
```bash
curl --location 'http://localhost:8080/product/get-by-product-id/3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJxdWFuZ2l0ZXI3NEBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI1NTcsImV4cCI6MTc0ODMyNDU1N30.Oc0XAkTHyq576gNhN5OtRN-_J0NizeL6JkGOlQ3q0Oim_qIlsmnLFfFUbxcBTvFv'
```

### Get products by category id
```bash
curl --location 'http://localhost:8080/product/get-by-category-id/2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJxdWFuZ2l0ZXI3NEBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI1NTcsImV4cCI6MTc0ODMyNDU1N30.Oc0XAkTHyq576gNhN5OtRN-_J0NizeL6JkGOlQ3q0Oim_qIlsmnLFfFUbxcBTvFv'
```

### Create product (**Only Admin**)
```bash
curl --location 'http://localhost:8080/product/create' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ' \
--form 'categoryId="4"' \
--form 'image=@"/E:/University/Year-3/Enterprise Systems Development Concepts/fashion-shop-management/assets/p_img8.png"' \
--form 'name="Ao sweater nam"' \
--form 'description="Ao phu hop ca mua dong, lan mua he"' \
--form 'price="5556"'
```

### Search product
```bash
curl --location 'http://localhost:8080/product/search?searchValue=Ao%20thun'
```

### Update product (**Only Admin**)
```bash
curl --location --request PUT 'http://localhost:8080/product/update' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ' \
--data-urlencode 'productId=2' \
--data-urlencode 'name=Quan nu'
```

### Delete product (**Only Admin**)
```bash
curl --location --request DELETE 'http://localhost:8080/product/delete/3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ'
```

## Category

### Get category
```bash
curl --location 'http://localhost:8080/category/get-category-by-id/2'
```

### Get all categories
```bash
curl --location 'http://localhost:8080/category/get-all' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ'
```

### Create category (**Only Admin**)
```bash
curl --location 'http://localhost:8080/category/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ' \
--data '{
    "name": "Others"
}'
```

### Update category (**Only Admin**)
```bash
curl --location --request PUT 'http://localhost:8080/category/update/234' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ' \
--data '{
    "name": "Fashion"
}'
```

### Delete category (**Only Admin**)
```bash
curl --location --request DELETE 'http://localhost:8080/category/delete/5' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ'
```

##Order
### Get all orders
```bash
curl --location 'http://localhost:8080/order/filter' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ'
```

### Get order item by id
```bash
curl --location 'http://localhost:8080/order/filter?itemId=2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ'
```

### Get order item by status (**Only Admin**)
```bash
curl --location 'http://localhost:8080/order/filter?status=pending' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ'
```

### Create order
```bash
curl --location 'http://localhost:8080/order/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI3MzEsImV4cCI6MTc0ODMyNDczMX0.i2-U561z0nN-7g1Gba3eMYyUJ1lrt-cKHhfYuocXWWzB9tCY31_zlqXQ1cS3FrxZ' \
--data '{
    "items": [
        {
            "productId": 1,
            "quantity": 5
        },
        {
             "productId": 11,
            "quantity": 1
        }
    ]
}'
```

### Update order item status (**Only Admin**)
```bash
curl --location --request PUT 'http://localhost:8080/order/update-item-status/2?status=CONFIRMED' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJxdWFuZ2l0ZXI3NEBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI1NTcsImV4cCI6MTc0ODMyNDU1N30.Oc0XAkTHyq576gNhN5OtRN-_J0NizeL6JkGOlQ3q0Oim_qIlsmnLFfFUbxcBTvFv'
```

## Address
## Save and update address
```bash
curl --location 'http://localhost:8080/address/save' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJxdWFuZ2l0ZXI3NEBnbWFpbC5jb20iLCJpYXQiOjE3MzI3NzI1NTcsImV4cCI6MTc0ODMyNDU1N30.Oc0XAkTHyq576gNhN5OtRN-_J0NizeL6JkGOlQ3q0Oim_qIlsmnLFfFUbxcBTvFv'\
--data '{
    "street": "Nguyen Huu Tho",
    "city": "Ho Chi Minh",
    "state": "Tan Phong",
    "zipCode": 7000,
    "country": "VietNam"
}'
```


7\. Kiểm thử đơn vị (Unit test)
-------------------------------------
Unit Test là phương pháp kiểm thử các thành phần nhỏ nhất của ứng dụng, thường là các hàm hoặc phương thức, nhằm đảm bảo rằng chúng hoạt động đúng như mong đợi. Trong dự án, Unit Test đóng vai trò quan trọng trong việc đảm bảo tính đúng đắn, giúp phát hiện và sửa lỗi ngay từ giai đoạn đầu. Đồng thời, Unit Test còn tăng cường độ tin cậy khi đảm bảo các thành phần hoạt động ổn định trong quá trình tích hợp và hỗ trợ bảo trì, cho phép kiểm tra nhanh các ảnh hưởng khi thay đổi hoặc mở rộng ứng dụng.  

Tôi đã sử dụng **JUnit** và **Mockito** để kiểm tra các tầng Service, cùng với **MockMvc** để kiểm tra các endpoint trong Controller. Các bài test tại tầng Service được thực hiện khá đầy đủ với các trường hợp thành công và thất bại, giúp đảm bảo logic xử lý chính xác. Tuy nhiên, khi kiểm tra tầng Controller, tôi gặp khó khăn trong việc test các endpoint bảo mật do ứng dụng sử dụng Spring Security. Điều này đòi hỏi phải cấu hình Security Context và giả lập người dùng đã xác thực, khiến việc kiểm thử các endpoint yêu cầu xác thực trở nên phức tạp hơn.  

Mặc dù đã đạt độ bao phủ test khoảng 70%, với tầng Service được kiểm tra kỹ lưỡng, nhưng tôi vẫn cần cải thiện khả năng kiểm tra các endpoint bảo mật của tầng Controller để đảm bảo chất lượng toàn diện cho ứng dụng.

![dobaophucuatest](https://github.com/user-attachments/assets/1c021dfb-207e-45bf-98e7-818470094f70)



8\. Cài đặt và thiết lập
-------------------------------------

### **1\. Thiết lập Backend (Spring Boot)**

#### Bước 1: Cài đặt Java và Maven
Đảm bảo rằng Java Development Kit (JDK) và Maven đã được cài đặt trên máy tính của bạn.

*   **JDK:** Spring Boot yêu cầu JDK 8 hoặc phiên bản mới hơn. Tải và cài đặt JDK cho hệ điều hành của bạn.
    *   **Tải JDK:** [AdoptOpenJDK](https://adoptopenjdk.net/)
*   **Maven:** Spring Boot cũng yêu cầu Maven để xây dựng và quản lý các phụ thuộc.    
    *   **Tải Maven:** [Apache Maven](https://maven.apache.org/download.cgi)
        
#### Bước 2: Clone Dự Án Spring Boot
Clone dự án Spring Boot từ GitHub hoặc hệ thống quản lý phiên bản khác.
```bash
git clone https://github.com/nguyencongquang-github/spring_ecommerce_midterm_java
```

#### Bước 3: Thiết lập Cơ sở Dữ liệu (MySQL)
Đảm bảo bạn đã cài đặt MySQL và thiết lập cơ sở dữ liệu cho ứng dụng Spring Boot.
1.  **Cài đặt MySQL:** Nếu bạn chưa có MySQL, làm theo hướng dẫn tại [MySQL Downloads](https://dev.mysql.com/downloads/).
2.  **Tạo Cơ sở Dữ liệu:** Trong MySQL, tạo cơ sở dữ liệu mà ứng dụng Spring Boot sẽ sử dụng.

```bash
CREATE DATABASE your_db;
```
#### Bước 4: Cấu hình Application Properties
Cấu hình kết nối cơ sở dữ liệu trong tệp application.properties hoặc application.yml nằm trong src/main/resources. Chỉnh sửa lại tên database và username, password của bạn và Xóa **111* ở *aws.s3.accessKey* và *aws.s3.secretKey* để chạy được.

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#JWT
security.jwt.secret-key =09AsC/ucc50zVPxpaxTXGK5NC90jU1XtzJv62R0ROxtMXQ7ANBsv4TMzkUDopYdR
security.jwt.expiration-time=3600000

# AWS S3 configuration
aws.s3.accessKey=AKIA2ZIOM6RCSLKMPCSY111
aws.s3.secretKey=Kr89ZFzg/JaT4j5UopbA3egLE1vjxODVdkxHN7lc111

# Admin account
admin.email=admin@gmail.com
admin.password=admin123
```

#### Bước 5: Xây dựng và Chạy Backend
1.  Mở terminal trong thư mục gốc của dự án.
2.  Sử dụng Maven để xây dựng và chạy ứng dụng.
    
```bash
./mvnw clean install
./mvnw spring-boot:run
```
3.  Hoặc bạn có thể chạy ứng dụng từ IDE của mình (IntelliJ IDEA hoặc Eclipse).
4.  Ứng dụng backend sẽ chạy trên http://localhost:8080 theo mặc định.

### **2\. Thiết lập Frontend (React.js)**

#### Bước 1: Cài đặt Node.js và npm
Bạn cần **Node.js** và **npm** (Node Package Manager) để quản lý các phụ thuộc frontend và chạy máy chủ phát triển.
*   **Tải Node.js:** [Node.js Downloads](https://nodejs.org/)
    
Kiểm tra cài đặt:

```bash
node -v
npm -v
```

#### Bước 2: Clone Dự Án React
Bước này đã làm trên thiết lập backend nên bỏ qua bước này.

#### Bước 3: Cài đặt Các Phụ Thuộc
Đi đến thư mục frontend và cài đặt tất cả các phụ thuộc cần thiết.

```bash
cd frontend
npm install
``` 

#### Bước 4: Cấu hình API Endpoints

Đảm bảo rằng ứng dụng React được cấu hình để giao tiếp với backend. Thông thường, điều này được thực hiện trong thư mục src/api hoặc src/services, nơi bạn định nghĩa URL backend cho các cuộc gọi API.
Đảm bảo rằng biến BASE_URL đúng địa chỉ đến server frontend. Bạn có thể kiểm tra trong service/ApiService.js
```bash
BASE_URL = "http://localhost:8080";
```

#### Bước 5: Chạy Máy Chủ Phát Triển React

Khởi động máy chủ phát triển React:

```bash
npm start
``` 
Điều này sẽ khởi động ứng dụng React tại http://localhost:3000 theo mặc định.

#### Bước 6: Kiểm tra Frontend
Sau khi ứng dụng React đã chạy, mở trình duyệt và truy cập vào **http://localhost:3000** để xem ứng dụng.

9\. Video demo
-------------------------------------
Ấn vào link bên dưới để xem video demo sản phẩm.
{@embed: https://youtu.be/Ghg-0nROPXo}

