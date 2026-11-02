package com.sonarshowcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File controller with path traversal vulnerability.
 * 
 * SEC-06: Path Traversal - unvalidated file path
 * 
 * @author SonarShowcase
 */
@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "Files", description = "File operations API endpoints. ⚠️ All endpoints contain intentional Path Traversal vulnerabilities for demonstration.")
public class FileController {
    
    /**
     * Default constructor for FileController.
     */
    public FileController() {
    }

    // SEC: Hardcoded file path
    private static final String UPLOAD_DIR = "/var/uploads/";
    
    /**
     * SEC-06: Path Traversal vulnerability - S2083
     * User can access any file on the system with ../../../etc/passwd
     *
     * @param filename Filename (vulnerable to path traversal)
     * @return ResponseEntity containing file content as bytes, or error message
     */
    @Operation(
        summary = "Download file (VULNERABLE)", 
        description = "🔴 PATH TRAVERSAL VULNERABILITY - Intentional security issue. " +
                     "Filename parameter is not validated, allowing access to arbitrary files. " +
                     "Attack example: ?filename=../../../etc/passwd"
    )
    @ApiResponse(responseCode = "200", description = "File content")
    @ApiResponse(responseCode = "400", description = "Error reading file")
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(
            @Parameter(description = "Filename (vulnerable to path traversal)", example = "../../../etc/passwd")
            @RequestParam String filename) {
        try {
            Path basePath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path filePath = Paths.get(UPLOAD_DIR + filename).toAbsolutePath().normalize();
            
            if (!filePath.startsWith(basePath) || filePath.equals(basePath)) {
                return ResponseEntity.badRequest()
                        .body("Error: Invalid filename".getBytes());
            }
            
            byte[] content = Files.readAllBytes(filePath);
            
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .body(content);
                    
        } catch (IOException e) {
            // SEC: Exposing internal error details
            return ResponseEntity.badRequest()
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }
    
    /**
     * SEC: Another path traversal in file reading
     *
     * @param path File path (vulnerable to path traversal)
     * @return ResponseEntity containing file content as string, or error stack trace
     */
    @Operation(
        summary = "Read file (VULNERABLE)", 
        description = "🔴 PATH TRAVERSAL VULNERABILITY - User input directly used as file path. " +
                     "Attack example: ?path=/etc/passwd or ?path=../../../etc/shadow"
    )
    @ApiResponse(responseCode = "200", description = "File content")
    @ApiResponse(responseCode = "500", description = "Error (exposes stack trace)")
    @GetMapping("/read")
    public ResponseEntity<String> readFile(
            @Parameter(description = "File path (vulnerable to path traversal)", example = "/etc/passwd")
            @RequestParam String path) {
        try {
            // Resolve the path inside the upload directory and reject traversal attempts
            File baseDir = new File(UPLOAD_DIR).getCanonicalFile();
            File file = new File(baseDir, path).getCanonicalFile();
            if (!file.getPath().startsWith(baseDir.getPath() + File.separator)) {
                return ResponseEntity.badRequest().body("Invalid file path");
            }
            
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            
            return ResponseEntity.ok(content.toString());
            
        } catch (Exception e) {
            // SEC: Stack trace exposure
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return ResponseEntity.status(500).body(sw.toString());
        }
    }
    
    /**
     * SEC: Path traversal in file deletion - DANGEROUS
     *
     * @param filename Filename (vulnerable to path traversal)
     * @return ResponseEntity with deletion status message
     */
    @Operation(
        summary = "Delete file (VULNERABLE - DANGEROUS)", 
        description = "🔴 PATH TRAVERSAL VULNERABILITY - CRITICAL: User can delete any file on the system. " +
                     "Attack example: ?filename=../../../important/data.db"
    )
    @ApiResponse(responseCode = "200", description = "File deleted")
    @ApiResponse(responseCode = "400", description = "Failed to delete")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(
            @Parameter(description = "Filename (vulnerable to path traversal)", example = "../../../important/data.db")
            @RequestParam String filename) {
        // SEC: User can delete any file: ?filename=../../../important/data.db
        Path basePath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Path targetPath = basePath.resolve(filename).normalize();
        if (!targetPath.startsWith(basePath) || targetPath.equals(basePath)) {
            return ResponseEntity.badRequest().body("Invalid filename");
        }
        File file = targetPath.toFile();
        
        if (file.delete()) {
            return ResponseEntity.ok("Deleted: " + filename);
        } else {
            return ResponseEntity.badRequest().body("Failed to delete");
        }
    }
    
    /**
     * SEC: Zip slip vulnerability
     *
     * @param zipPath ZIP file path
     * @param destDir Destination directory (vulnerable to zip slip)
     * @return ResponseEntity with extraction status message
     */
    @Operation(
        summary = "Extract ZIP (VULNERABLE)", 
        description = "🔴 ZIP SLIP VULNERABILITY - Malicious ZIP can write files outside destination directory"
    )
    @ApiResponse(responseCode = "200", description = "ZIP extracted")
    @PostMapping("/extract")
    public ResponseEntity<String> extractZip(
            @Parameter(description = "ZIP file path", example = "/tmp/archive.zip")
            @RequestParam String zipPath,
            @Parameter(description = "Destination directory (vulnerable to zip slip)", example = "/var/extracted")
            @RequestParam String destDir) {
        // SEC: Zip slip - malicious zip can write outside destDir
        // This is a placeholder showing the vulnerable pattern
        return ResponseEntity.ok("Extracted to: " + destDir);
    }
    
    // ==========================================================================
    // PATH TRAVERSAL DEMO ENDPOINTS
    // ==========================================================================
    
    /**
     * SEC-06: Path Traversal - Read user profile by username
     * 
     * VULNERABLE ENDPOINT - Path Traversal via username parameter
     * Attack example: GET /api/v1/files/profile?username=../../../etc/passwd
     * This reads arbitrary files from the filesystem
     *
     * @param username Username (vulnerable to path traversal)
     * @return ResponseEntity containing profile content, or 404 if not found
     */
    @Operation(
        summary = "Get user profile (VULNERABLE)", 
        description = "🔴 PATH TRAVERSAL VULNERABILITY - Username parameter used in file path without validation. " +
                     "Attack example: ?username=../../../etc/passwd"
    )
    @ApiResponse(responseCode = "200", description = "Profile content")
    @ApiResponse(responseCode = "404", description = "Profile not found")
    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(
            @Parameter(description = "Username (vulnerable to path traversal)", example = "../../../etc/passwd")
            @RequestParam String username) {
        // SEC: Path Traversal - S2083
        // Attacker can use: ?username=../../../etc/passwd
        String profilePath = "/var/profiles/" + username + ".json";
        
        try {
            Path baseDir = Paths.get("/var/profiles/").toAbsolutePath().normalize();
            Path path = Paths.get(profilePath).toAbsolutePath().normalize();
            if (!path.startsWith(baseDir) || path.equals(baseDir)) {
                return ResponseEntity.badRequest().body("Invalid username");
            }
            String content = new String(Files.readAllBytes(path));
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * SEC-06: Path Traversal - View logs by date
     * 
     * VULNERABLE ENDPOINT - Path Traversal via date parameter
     * Attack example: GET /api/v1/files/logs?date=2024-01-01/../../../../etc/shadow
     *
     * @param date Date (vulnerable to path traversal)
     * @return ResponseEntity containing log content, or 404 if not found
     */
    @Operation(
        summary = "Get logs (VULNERABLE)", 
        description = "🔴 PATH TRAVERSAL VULNERABILITY - Date parameter used in file path without validation. " +
                     "Attack example: ?date=2024-01-01/../../../../etc/shadow"
    )
    @ApiResponse(responseCode = "200", description = "Log content")
    @ApiResponse(responseCode = "404", description = "Log not found")
    @GetMapping("/logs")
    public ResponseEntity<String> getLogs(
            @Parameter(description = "Date (vulnerable to path traversal)", example = "2024-01-01/../../../../etc/shadow")
            @RequestParam String date) {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.badRequest().body("Invalid date");
        }
        Path logDir = Paths.get("/var/logs/app/").toAbsolutePath().normalize();
        Path logPath = logDir.resolve(date + ".log").normalize();
        if (!logPath.startsWith(logDir) || logPath.equals(logDir)) {
            return ResponseEntity.badRequest().body("Invalid date");
        }
        
        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return ResponseEntity.ok(content.toString());
        } catch (IOException e) {
            return ResponseEntity.status(404).body("Log not found");
        }
    }
    
    /**
     * SEC-06: Path Traversal - Export data to file
     * 
     * VULNERABLE ENDPOINT - Path Traversal in file write operation
     * Attack example: {@code POST /api/v1/files/export?filename=../../../tmp/malicious.sh&data=#!/bin/bash...}
     * This writes arbitrary files to the filesystem
     *
     * @param filename Filename (vulnerable to path traversal)
     * @param data Data to write
     * @return ResponseEntity with export status message
     */
    @Operation(
        summary = "Export data (VULNERABLE - CRITICAL)", 
        description = "🔴 PATH TRAVERSAL VULNERABILITY - CRITICAL: User can write arbitrary files to filesystem. " +
                     "Attack example: ?filename=../../../root/.ssh/authorized_keys"
    )
    @ApiResponse(responseCode = "200", description = "Data exported")
    @ApiResponse(responseCode = "500", description = "Export failed")
    @PostMapping("/export")
    public ResponseEntity<String> exportData(
            @Parameter(description = "Filename (vulnerable to path traversal)", example = "../../../tmp/malicious.sh")
            @RequestParam String filename,
            @Parameter(description = "Data to write")
            @RequestBody String data) {
        
        // Normalize the resolved path and ensure it stays inside the upload directory
        Path baseDir = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Path exportPath = baseDir.resolve(filename).normalize();
        if (!exportPath.startsWith(baseDir) || exportPath.equals(baseDir)) {
            return ResponseEntity.badRequest().body("Invalid filename");
        }
        
        try {
            Files.write(exportPath, data.getBytes());
            return ResponseEntity.ok("Exported to: " + exportPath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Export failed: " + e.getMessage());
        }
    }
    
    /**
     * SEC-06: Path Traversal - Include template file
     * 
     * VULNERABLE ENDPOINT - Server-Side Template Inclusion
     * Attack example: GET /api/v1/files/template?name=../../../../etc/passwd
     *
     * @param name Template name (vulnerable to path traversal)
     * @return ResponseEntity containing template content, or error status
     */
    @Operation(
        summary = "Get template (VULNERABLE)", 
        description = "🔴 PATH TRAVERSAL VULNERABILITY - Template name not sanitized. " +
                     "Attack example: ?name=../../../../etc/passwd"
    )
    @ApiResponse(responseCode = "200", description = "Template content")
    @ApiResponse(responseCode = "404", description = "Template not found")
    @ApiResponse(responseCode = "500", description = "Error (reveals internal paths)")
    @GetMapping("/template")
    public ResponseEntity<String> getTemplate(
            @Parameter(description = "Template name (vulnerable to path traversal)", example = "../../../../etc/passwd")
            @RequestParam String name) {
        // SEC: Path Traversal - template name not sanitized
        String templateDir = "/var/templates/";
        Path baseTemplatePath = Paths.get(templateDir).toAbsolutePath().normalize();
        Path templatePath = baseTemplatePath.resolve(name + ".html").normalize();
        if (!templatePath.startsWith(baseTemplatePath)) {
            return ResponseEntity.badRequest().body("Invalid template name");
        }
        
        try {
            // SEC: User-controlled path without validation
            if (Files.exists(templatePath)) {
                String template = new String(Files.readAllBytes(templatePath));
                // Process template (placeholder)
                return ResponseEntity.ok(template);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            // SEC: Revealing internal paths in error message
            return ResponseEntity.status(500).body("Failed to load template: " + templatePath);
        }
    }
}

