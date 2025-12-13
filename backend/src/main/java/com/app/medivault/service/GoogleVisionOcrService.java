package com.app.medivault.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleVisionOcrService {

    public String extractText(byte[] imageBytes) {

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {

            ByteString imgBytes = ByteString.copyFrom(imageBytes);
            Image image = Image.newBuilder().setContent(imgBytes).build();

            Feature feature = Feature.newBuilder()
                    .setType(Feature.Type.DOCUMENT_TEXT_DETECTION)
                    .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(image)
                    .build();

            BatchAnnotateImagesResponse response =
                    client.batchAnnotateImages(List.of(request));

            return response.getResponses(0)
                    .getFullTextAnnotation()
                    .getText();

        } catch (Exception e) {
            throw new RuntimeException("OCR failed", e);
        }
    }
}

