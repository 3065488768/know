package com.example.know.enumeration;

public enum TransactionType {

    REWARD('1', "打赏"),
    OFFERREWARD('2', "悬赏");
    private char typeId;
    private String typeName;

    TransactionType(char typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public char getTypeId() {
        return typeId;
    }

    public void setTypeId(char typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
