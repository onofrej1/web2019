package com.furca.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.furca.storage.StorageFileNotFoundException;
import com.furca.storage.StorageService;

@Controller
public class FileController {

    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @GetMapping("/calc")
    public ResponseEntity<String> calculate(Model model) throws IOException {
    	LevenshteinDistance dist = new LevenshteinDistance();
    	Integer num = dist.apply("erik", "erikxxb");
    	
    	return ResponseEntity.ok(num.toString());
    }

    @GetMapping("/files")
    public ResponseEntity<List> listUploadedFiles(Model model) throws IOException {
    	Path root = Paths.get("images");
        /*List files = storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList());*/
    	
    	List files = storageService.loadDir("images").map(path -> {
    		BasicFileAttributes attr = null;
			try {
				attr = Files.readAttributes(path, BasicFileAttributes.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		Map fileInfo = new HashMap();
    		fileInfo.put("link", "http://localhost:8082/images/"+path.toFile().getName());
    		fileInfo.put("path", path.toFile().getName());
    		//fileInfo.put("name", path.getFileName());
    		fileInfo.put("size", attr.size());
    		fileInfo.put("isDirectory", attr.isDirectory());
    		fileInfo.put("created", attr.creationTime().toString());
    		fileInfo.put("lastAccessTime", attr.lastAccessTime().toString());
    		return fileInfo;
    	}).collect(Collectors.toList());

        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
    		@RequestParam("path") String path) {
    	
    	System.out.println(path);
    	
        storageService.store(file, path);

        return ResponseEntity.ok("You successfully uploaded " + file.getOriginalFilename() + "!");
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
