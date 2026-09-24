package com.sonarshowcase.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for creating activity log entries.
 * Contains only the fields that should be settable by the client.
 */
@Data
public class ActivityLogDto {

    private Long userId;
    private String action;
    private String details;
    private Date timestamp;
    private String ipAddress;
}
