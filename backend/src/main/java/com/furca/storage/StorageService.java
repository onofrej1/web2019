package com.furca.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file, String path);

    Stream<Path> loadAll();
    
    Stream<Path> loadDir(String dir);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
