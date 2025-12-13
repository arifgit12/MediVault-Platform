package com.app.medivault.entity;

public enum AnalysisStatus {

    PENDING,           // Initial state
    UPLOADED,          // Image uploaded successfully
    QUEUED,            // Waiting for OCR / analysis
    PROCESSING,        // OCR or AI analysis in progress
    COMPLETED,         // Analysis completed successfully
    RISK_DETECTED,     // Drug interaction / high risk found
    FAILED             // OCR or analysis failed
}
