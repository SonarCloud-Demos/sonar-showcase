package com.sonarshowcase.util;

import java.io.*;
import java.sql.*;
import java.util.Date;

/**
 * Report generator with resource leaks.
 * 
 * REL-04: Resource leaks - unclosed streams and connections
 * 
 * @author SonarShowcase
 */
public class ReportGenerator {
    
    /**
     * Default constructor for ReportGenerator.
     */
    public ReportGenerator() {
    }

    // SEC: Hardcoded credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sonarshowcase";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "admin123";

    /**
     * REL-04: Multiple resource leaks
     *
     * @param reportType Type of report to generate
     * @return Generated report as a string
     * @throws Exception If report generation fails
     */
    public String generateReport(String reportType) throws Exception {
        // REL: Connection never closed
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        
        // REL: Statement never closed
        Statement stmt = conn.createStatement();
        
        // REL: ResultSet never closed
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        
        StringBuilder report = new StringBuilder();
        report.append("Report Type: ").append(reportType).append("\n");
        report.append("Generated: ").append(new Date()).append("\n\n");
        
        while (rs.next()) {
            report.append(rs.getString("username")).append("\n");
        }
        
        // REL: None of conn, stmt, rs are closed
        // Should use try-with-resources
        
        return report.toString();
    }
    
    /**
     * REL: File stream never closed
     *
     * @param content Report content to save
     * @param filePath Path to save the report file
     */
    public void saveReportToFile(String content, String filePath) {
        try {
            // REL: FileWriter never closed
            FileWriter fw = new FileWriter(filePath);
            fw.write(content);
            fw.flush();
            // REL: Missing fw.close()
        } catch (IOException e) {
            // REL: Exception swallowed
        }
    }
    
    /**
     * REL: Input stream never closed
     *
     * @param path Path to template file
     * @return Template content as a string
     */
    public String readTemplate(String path) {
        try {
            // REL: FileInputStream never closed
            FileInputStream fis = new FileInputStream(path);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            // REL: Missing fis.close()
            return new String(data);
        } catch (IOException e) {
            return "";
        }
    }
    
    /**
     * REL: BufferedReader never closed
     *
     * @param filePath Path to file to count lines in
     * @return Number of lines in the file
     */
    public int countLines(String filePath) {
        int count = 0;
        try {
            // REL: BufferedReader never closed
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while (reader.readLine() != null) {
                count++;
            }
            // REL: Missing reader.close()
        } catch (IOException e) {
            // REL: Swallowed
        }
        return count;
    }
    
    /**
     * REL: PrintWriter closed in wrong place
     *
     * @param data Data array to export
     * @param path Path to export file
     */
    public void exportData(String[] data, String path) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(path));
            for (String line : data) {
                writer.println(line);
            }
        } catch (IOException e) {
            // REL: If exception occurs, writer is never closed
            System.out.println("Error: " + e);
        }
        // REL: writer could be null here, and if exception was thrown, not closed
        if (writer != null) {
            writer.close();
        }
    }
    
    /**
     * REL: Nested streams - only outer closed
     *
     * @param content Report content to write
     * @param path Path to write compressed report
     */
    public void writeCompressedReport(String content, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);
            
            dos.writeUTF(content);
            
            // REL: Only closing outer stream - inner streams may leak if exception
            dos.close();
            // fos and bos not explicitly closed
        } catch (IOException e) {
            // REL: Swallowed, resources leaked
        }
    }
    
    /**
     * REL: PreparedStatement leak
     *
     * @param reportId Report ID to update
     * @param status New status for the report
     */
    public void updateReportStatus(long reportId, String status) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            
            // REL: PreparedStatement never closed
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE reports SET status = ? WHERE id = ?"
            );
            ps.setString(1, status);
            ps.setLong(2, reportId);
            ps.executeUpdate();
            
            // REL: Neither ps nor conn closed
        } catch (SQLException e) {
            // REL: Swallowed
        }
    }
}

