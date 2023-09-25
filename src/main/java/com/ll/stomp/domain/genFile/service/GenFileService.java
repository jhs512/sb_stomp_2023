package com.ll.stomp.domain.genFile.service;

import com.ll.stomp.domain.genFile.entity.GenFile;
import com.ll.stomp.domain.genFile.repository.GenFileRepository;
import com.ll.stomp.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenFileService {
    private final GenFileRepository genFileRepository;

    @Transactional
    public GenFile save(String relTypeCode, Long relId, String typeCode, String type2Code, int fileNo, MultipartFile multipartFile) {
        String originFileName = multipartFile.getOriginalFilename();
        String fileExt = Ut.file.getExt(originFileName);
        String fileExtTypeCode = Ut.file.getFileExtTypeCodeFromFileExt(fileExt);
        String fileExtType2Code = Ut.file.getFileExtType2CodeFromFileExt(fileExt);
        int fileSize = (int) multipartFile.getSize();
        String fileDir = getCurrentDirName(relTypeCode);

        GenFile genFile = GenFile.builder()
                .relTypeCode(relTypeCode)
                .relId(relId)
                .typeCode(typeCode)
                .type2Code(type2Code)
                .fileExtTypeCode(fileExtTypeCode)
                .fileExtType2Code(fileExtType2Code)
                .originFileName(originFileName)
                .fileSize(fileSize)
                .fileNo(fileNo)
                .fileExt(fileExt)
                .fileDir(fileDir)
                .build();

        genFileRepository.save(genFile);

        File file = new File(genFile.getFilePath());

        file.getParentFile().mkdirs();

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return genFile;
    }

    private String getCurrentDirName(String relTypeCode) {
        return relTypeCode + "/" + Ut.date.getCurrentDateFormatted("yyyy_MM_dd");
    }

    public Optional<GenFile> findGenFileBy(String relTypeCode, Long relId, String typeCode, String type2Code, int fileNo) {
        return genFileRepository.findByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeAndFileNo(relTypeCode, relId, typeCode, type2Code, fileNo);
    }
}
