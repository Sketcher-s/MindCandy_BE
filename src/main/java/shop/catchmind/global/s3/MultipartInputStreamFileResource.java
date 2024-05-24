package shop.catchmind.global.s3;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

public class MultipartInputStreamFileResource extends InputStreamResource {

    private final String fileName;

    public MultipartInputStreamFileResource(final InputStream inputStream, final String fileName) {
        super(inputStream);
        this.fileName = fileName;
    }

    @Override
    public String getFilename() {
        return this.fileName;
    }

    @Override
    public long contentLength() {
        return -1;
    }
}
