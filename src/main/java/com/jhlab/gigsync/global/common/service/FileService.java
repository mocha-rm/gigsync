package com.jhlab.gigsync.global.common.service;

import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.board.entity.BoardFile;
import com.jhlab.gigsync.global.exception.CustomException;
import com.jhlab.gigsync.global.exception.type.FileErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public List<BoardFile> uploadFiles(List<MultipartFile> files, Board board) {
        List<BoardFile> result = new ArrayList<>();

        for (MultipartFile file : files) {
            String extension = getExtension(file.getOriginalFilename());

            if (isImage(file)) {
                if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
                    throw new CustomException(FileErrorCode.UNSUPPORTED_IMAGE_FORMAT);
                }
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String url = uploadToS3(file, fileName);

            result.add(BoardFile.builder()
                    .fileName(fileName)
                    .fileUrl(url)
                    .board(board)
                    .build()
            );
        }

        return result;
    }

    private String uploadToS3(MultipartFile file, String fileName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
        } catch (IOException e) {
            throw new CustomException(FileErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image");
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new CustomException(FileErrorCode.FILE_EXTENSION_MISSING);
        }

        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
