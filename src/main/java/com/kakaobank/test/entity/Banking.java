package com.kakaobank.test.entity;

public class Banking {
    private final String BANKINGSCHEMA =
            "  {" +
            "    \"namespace\": \"com.kakaobank.test.entity\"," +
            "    \"type\": \"record\"," +
            "    \"name\": \"Banking\"," +
            "    \"fields\": [" +
            "        {\"name\": \"transactionDt\", \"type\": \"string\"}," +
            "        {\"name\": \"logId\", \"type\": \"string\"}," +
            "        {\"name\": \"logDt\",  \"type\": \"string\"}," +
            "        {\"name\": \"resultCode\",  \"type\": \"string\"}," +
            "        {\"name\": \"userId\",  \"type\": \"string\"}," +
            "        {\"name\": \"transactionContent\",  \"type\": \"string\"}" +
            "    ]" +
            "  }";


    private String transactionDt;
    private String logId;
    private String logDt;
    private String resultCode;
    private String userId;
    private String transactionContent;

    public String getBANKINGSCHEMA() {
        return BANKINGSCHEMA;
    }

    public String getResultCode() { return resultCode; }

    public void setResultCode(String resultCode) { this.resultCode = resultCode; }

    public String getTransactionDt() {
        return transactionDt;
    }

    public void setTransactionDt(String transactionDt) {
        this.transactionDt = transactionDt;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogDt() {
        return logDt;
    }

    public void setLogDt(String logDt) { this.logDt = logDt; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) { this.userId = userId; }

    public String getTransactionContent() { return transactionContent; }

    public void setTransactionContent(String transactionContent) { this.transactionContent = transactionContent; }
}
