package com.app.cryptok.model;

import java.io.Serializable;

public class ReportReasonModel implements Serializable {

    private String report_id;
    private String report_reason;

    public ReportReasonModel() {
    }

    public ReportReasonModel(String report_id, String report_reason) {
        this.report_id = report_id;
        this.report_reason = report_reason;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getReport_reason() {
        return report_reason;
    }

    public void setReport_reason(String report_reason) {
        this.report_reason = report_reason;
    }
}
