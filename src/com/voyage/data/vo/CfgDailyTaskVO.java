package com.voyage.data.vo;

public class CfgDailyTaskVO {
    private Integer cdtId;

    private Integer cdtType;

    private String cdtName;

    private String cdtDesc;

    private Integer cdtRestrain;

    private Integer cdtLive;

    public Integer getCdtId() {
        return cdtId;
    }

    public void setCdtId(Integer cdtId) {
        this.cdtId = cdtId;
    }

    public Integer getCdtType() {
        return cdtType;
    }

    public void setCdtType(Integer cdtType) {
        this.cdtType = cdtType;
    }

    public String getCdtName() {
        return cdtName;
    }

    public void setCdtName(String cdtName) {
        this.cdtName = cdtName;
    }

    public String getCdtDesc() {
        return cdtDesc;
    }

    public void setCdtDesc(String cdtDesc) {
        this.cdtDesc = cdtDesc;
    }

    public Integer getCdtRestrain() {
        return cdtRestrain;
    }

    public void setCdtRestrain(Integer cdtRestrain) {
        this.cdtRestrain = cdtRestrain;
    }

    public Integer getCdtLive() {
        return cdtLive;
    }

    public void setCdtLive(Integer cdtLive) {
        this.cdtLive = cdtLive;
    }
}