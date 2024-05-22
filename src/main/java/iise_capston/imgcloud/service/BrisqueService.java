package iise_capston.imgcloud.service;

import lombok.RequiredArgsConstructor;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_quality.QualityBRISQUE;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BrisqueService {
    private final String modelPath = "src/main/resources/brisque/brisque_model_live.yml";
    private final String rangePath = "src/main/resources/brisque/brisque_range_live.yml";

    private Logger logger = LoggerFactory.getLogger(BrisqueService.class);
    @Async
    @Transactional
    public CompletableFuture<Scalar> getBrisqueOne(MultipartFile picture) throws IOException {

        byte[]data = picture.getBytes();
        ByteBuffer dataByte = ByteBuffer.wrap(data);

        Mat pic = opencv_imgcodecs.imdecode(new Mat(new BytePointer(dataByte)),opencv_imgcodecs.IMWRITE_JPEG_SAMPLING_FACTOR);

        QualityBRISQUE brisque = QualityBRISQUE.create(modelPath,rangePath);
        Scalar brisqueScore = brisque.compute(pic);

        return CompletableFuture.completedFuture(brisqueScore);
    }

    public List<CompletableFuture<Scalar>> getBrisqueAll(List<MultipartFile> pictures) throws IOException{
        List<CompletableFuture<Scalar>> brisqueScores= new ArrayList<>();

        for(int i=0;i<pictures.size();i++){
            CompletableFuture<Scalar> score = getBrisqueOne(pictures.get(i));
            brisqueScores.add(score);
        }

        return brisqueScores;
    }
}
