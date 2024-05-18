package shop.catchmind.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import shop.catchmind.global.config.AmazonConfig;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Provider {

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(final String keyName, final MultipartFile file) {
        String originName = file.getOriginalFilename(); // 파일 이름
        String ext = originName != null ? originName.substring(originName.lastIndexOf(".")) : null; // 확장자

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType()); // 다운로드가 아닌 브라우저로 조회를 하기 위함

        try{
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName + ext, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("파일을 업로드하는데 오류가 발생했습니다. : {}", (Object) e.getStackTrace());
        }
        return amazonS3.getUrl(amazonConfig.getBucket(), keyName + ext).toString();
    }

    // 이미지 Url로 Key 추출
    public String extractImageKeyFromUrl(final String imageUrl) {
        String bucket = amazonConfig.getBucket();
        String prefix = "https://" + bucket + ".s3." + amazonConfig.getRegion() + ".amazonaws.com/";
        return imageUrl.substring(prefix.length());
    }

    // 저장된 이미지 제거
    public void deleteImage(final String key) {
        amazonS3.deleteObject(amazonConfig.getBucket(), key);
    }

    // 이미지 파일 경로로 저장
    public String generateFilesKeyName() {
        return amazonConfig.getFilePath() + '/' + UUID.randomUUID();
    }
}
