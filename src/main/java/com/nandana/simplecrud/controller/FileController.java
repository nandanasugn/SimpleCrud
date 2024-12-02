package com.nandana.simplecrud.controller;

import com.nandana.simplecrud.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Controller", description = "Endpoints for file management and retrieval")
public class FileController {
    private final FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/{subfolder}/{fileName}")
    @Operation(
            summary = "Fetch file",
            description = "Retrieves a file from the specified subfolder by file name. Accessible to users with ADMIN or CUSTOMER roles.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "subfolder", description = "Subfolder name where the file is stored", required = true),
                    @Parameter(name = "fileName", description = "Name of the file to fetch", required = true)
            },
            tags = {"File Controller"}
    )
    public ResponseEntity<Resource> serveFile(@PathVariable String subfolder, @PathVariable String fileName) {
        try {
            Resource resource = fileStorageService.getFile(fileName, subfolder);
            String contentType = Files.probeContentType(Paths.get(resource.getURI()));
            contentType = (contentType != null) ? contentType : "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
