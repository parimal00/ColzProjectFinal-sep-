package com.something.arfurnitureapp;

public class ReportModel {

    String reported_by;
    String report_text;

    public  ReportModel(){}

    public  ReportModel(String report_text, String reported_by){
        this.report_text=report_text;
        this.reported_by=reported_by;
    }
    public String getReported_by() {
        return reported_by;
    }

    public void setReported_by(String reported_by) {
        this.reported_by = reported_by;
    }

    public String getReport_text() {
        return report_text;
    }

    public void setReport_text(String report_text) {
        this.report_text = report_text;
    }

}
