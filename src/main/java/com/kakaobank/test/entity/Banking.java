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
            "        {\"name\": \"logdt\",  \"type\": \"string\"}," +
            "        {\"name\": \"userId\",  \"type\": \"string\"}," +
            "        {\"name\": \"TransactionContent\",  \"type\": \"string\"}" +
            "    ]" +
            "  }";


    private String transactionDt;
    private String logId;
    private String logdt;
    private String userId;
    private String TransactionContent;

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

    public String getLogdt() {
        return logdt;
    }

    public void setLogdt(String logdt) {
        this.logdt = logdt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransactionContent() {
        return TransactionContent;
    }

    public void setTransactionContent(String transactionContent) {
        TransactionContent = transactionContent;
    }

}
