package com.capy.capyjara.oss.service.impl;

import com.capy.capyjara.minio.MinIOProperties;
import com.capy.capyjara.oss.entity.vo.PreSignedVO;
import com.capy.capyjara.oss.entity.vo.StatObjectVO;
import io.minio.MinioClient;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import okhttp3.Headers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MinIOOssServiceImplTest {

    @Mock
    private MinioClient mockMinioClient;
    @Mock
    private MinIOProperties mockMinIOProperties;

    @InjectMocks
    private MinIOOssServiceImpl minIOOssServiceImplUnderTest;

    @Test
    void testGetPreSignedURL() throws Exception {
        // Setup
        final PreSignedVO expectedResult = new PreSignedVO("preSignedUrl", "objectName", Method.GET, "filename",
                "/bucket/objectName");
        when(mockMinioClient.getPresignedObjectUrl(any())).thenReturn("preSignedUrl");
        when(mockMinIOProperties.isSecure()).thenReturn(false);
        when(mockMinIOProperties.getEndpoint()).thenReturn("result");
        when(mockMinIOProperties.getPort()).thenReturn(0);

        // Run the test
        final PreSignedVO result = minIOOssServiceImplUnderTest.getPreSignedURL(Method.GET, "bucket", "objectName", 1,
                "filename");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testStatObject() throws Exception {
        // Setup
        final StatObjectVO expectedResult = new StatObjectVO("fileId", "100KB", "filename");

        // Configure MinioClient.statObject(...).
        final StatObjectResponse statObjectResponse = new StatObjectResponse(Headers.of("x-amz-meta-filename", "filename", "Last-Modified", "Wed, 20 Feb 2013 11:41:23 GMT", "Content-Length", "102400"), "bucket",
                "region", "object");
        when(mockMinioClient.statObject(any()))
                .thenReturn(statObjectResponse);

        // Run the test
        final StatObjectVO result = minIOOssServiceImplUnderTest.statObject("bucket", "fileId");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }


}
