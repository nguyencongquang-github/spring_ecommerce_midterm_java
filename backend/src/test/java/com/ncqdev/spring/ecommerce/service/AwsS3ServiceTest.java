//package com.ncqdev.spring.ecommerce.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectResponse;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class AwsS3ServiceTest {
//
//    @InjectMocks
//    private AwsS3Service awsS3Service;
//
//    @Mock
//    private S3Client s3Client;
//
//    @Mock
//    private MultipartFile photo;
//
//    private final String bucketName = "fashion-shop-management";
//    private final String imageUrl = "https://fashion-shop-management.s3.ap-southeast-1.amazonaws.com/sample.jpg";
//
//    @BeforeEach
//    void setUp() throws IOException {
//        // Cấu hình giả lập cho MultipartFile
//        when(photo.getOriginalFilename()).thenReturn("sample.jpg");
//        when(photo.getBytes()).thenReturn("image content".getBytes());
//    }
//
//    @Test
//    void testSaveImageToS3_Success() throws IOException {
//        // Giả lập phản hồi từ S3 khi tải ảnh lên
//        PutObjectResponse putObjectResponse = PutObjectResponse.builder().build();
//        when(s3Client.putObject((PutObjectRequest) any(PutObjectRequest.class), (RequestBody) any())).thenReturn(putObjectResponse);
//
//        // Gọi phương thức cần test
//        String result = awsS3Service.saveImageToS3(photo);
//
//        // Kiểm tra kết quả
//        assertEquals(imageUrl, result);
//        verify(s3Client, times(1)).putObject((PutObjectRequest) any(PutObjectRequest.class), (RequestBody) any());
//    }
//
//    @Test
//    void testSaveImageToS3_Failure() throws IOException {
//        // Giả lập lỗi khi tải ảnh lên
//        when(s3Client.putObject((PutObjectRequest) any(PutObjectRequest.class), (RequestBody) any())).thenThrow(new RuntimeException("S3 error"));
//
//        // Kiểm tra ngoại lệ
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> awsS3Service.saveImageToS3(photo));
//
//        // Kiểm tra thông điệp ngoại lệ
//        assertEquals("Error while saving image to S3", thrown.getMessage());
//    }
//
//    @Test
//    void testSaveImageToS3_NullFilename() throws IOException {
//        // Giả lập ảnh với tên file null
//        when(photo.getOriginalFilename()).thenReturn(null);
//
//        // Giả lập phản hồi từ S3 khi tải ảnh lên
//        PutObjectResponse putObjectResponse = PutObjectResponse.builder().build();
//        when(s3Client.putObject((PutObjectRequest) any(PutObjectRequest.class), (RequestBody) any())).thenReturn(putObjectResponse);
//
//        // Gọi phương thức cần test
//        String result = awsS3Service.saveImageToS3(photo);
//
//        // Kiểm tra kết quả
//        assertEquals(imageUrl, result);
//        verify(s3Client, times(1)).putObject((PutObjectRequest) any(PutObjectRequest.class), (RequestBody) any());
//    }
//}
