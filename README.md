# Giải thích:

## 1. ALARM:
### 1.1. requestCode trong PendingIntent có tác dụng gì?
#### 1.1.1. Ý nghĩa:
`requestCode` là một tham số kiểu Int trong PendingIntent.  
Nó giúp phân biệt các PendingIntent khác nhau khi bạn có nhiều intent cùng loại (ở đây là broadcast intent gửi đến ReminderReceiver).  
#### 1.1.2. Tác dụng:
Xác định duy nhất PendingIntent: Hệ thống dùng bộ ba `(Intent, requestCode, flags)` để quyết định liệu một PendingIntent đã tồn tại hay chưa.   
Nếu bạn tạo hai PendingIntent với cùng Intent (giống action, extras, v.v.) và cùng requestCode, chúng sẽ được coi là một, trừ khi bạn dùng các cờ như FLAG_UPDATE_CURRENT.  
#### 1.1.3. Ứng dụng cụ thể trong code của bạn:
Trong scheduleReminders(), mỗi reminder có requestCode riêng (từ 1 đến 15), giúp AlarmManager đặt nhiều alarm khác nhau mà không ghi đè lên nhau.  
Trong showTestNotification(), bạn dùng requestCode = 998 để đảm bảo alarm thử nghiệm không xung đột với các alarm trong lịch trình.
#### 1.1.4. Ví dụ:
Nếu bạn dùng cùng requestCode = 1 cho hai alarm khác nhau (ví dụ: 7h và 8h), alarm thứ hai có thể thay thế alarm đầu tiên, dẫn đến chỉ một trong hai được thực thi.
#### 1.1.5. Lưu ý:
Chọn requestCode sao cho không trùng lặp giữa các PendingIntent trong ứng dụng nếu bạn muốn chúng hoạt động độc lập.

---

### 1.2. PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE có tác dụng gì?
#### Tổng quan:
PendingIntent.getBroadcast có tham số flags để kiểm soát cách PendingIntent được tạo và xử lý.   
Bạn đang dùng FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE, hãy phân tích từng cờ:
##### 1.2.1. PendingIntent.FLAG_UPDATE_CURRENT:
###### Ý nghĩa: 
Nếu PendingIntent với cùng (Intent, requestCode) đã tồn tại, nó sẽ được cập nhật với dữ liệu mới từ Intent hiện tại (ví dụ: extras mới) thay vì tạo mới.
###### Tác dụng trong code:
Trong showTestNotification hoặc scheduleReminders, nếu bạn gọi hàm nhiều lần với cùng requestCode, PendingIntent cũ sẽ được cập nhật với extras mới (như task = "Button Test!") mà không tạo mới.  

Điều này hữu ích nếu bạn muốn thay đổi nội dung thông báo mà không cần hủy và tạo lại alarm.  
Ví dụ: Nếu không có cờ này, một PendingIntent cũ sẽ giữ nguyên dữ liệu cũ, dẫn đến thông báo không phản ánh thay đổi.
##### 1.2.2. PendingIntent.FLAG_IMMUTABLE:
###### Ý nghĩa:
Đánh dấu PendingIntent là bất biến, nghĩa là không thể bị chỉnh sửa bởi ứng dụng khác sau khi được tạo.   
Đây là yêu cầu bắt buộc trên Android 12+ (API 31) khi Intent chứa dữ liệu nhạy cảm.
###### Tác dụng trong code:
Đảm bảo an toàn, tránh các ứng dụng khác can thiệp vào PendingIntent của bạn.  
Nếu không có cờ này trên Android 12+, bạn có thể gặp lỗi runtime (IllegalArgumentException).  
Toán tử or:
- FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE kết hợp cả hai cờ bằng phép bitwise OR:
- FLAG_UPDATE_CURRENT = 0x04000000 (hex)
- FLAG_IMMUTABLE = 0x04000000 (hex)

Kết quả: Một số nguyên kết hợp cả hai thuộc tính.
#### 1.2.3. Khi nào dùng gì?
- Chỉ dùng FLAG_IMMUTABLE: Nếu bạn muốn PendingIntent bất biến và không cần cập nhật dữ liệu.
- Dùng cả hai: Như hiện tại, khi bạn vừa muốn cập nhật dữ liệu (nếu cần) vừa đảm bảo an toàn.
#### 1.2.4. Lưu ý:
Trên Android 12+, luôn thêm FLAG_IMMUTABLE hoặc FLAG_MUTABLE để tránh lỗi.    
Nếu không cần thay đổi PendingIntent từ bên ngoài, FLAG_IMMUTABLE là lựa chọn mặc định tốt.