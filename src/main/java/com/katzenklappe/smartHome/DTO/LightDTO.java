package com.katzenklappe.smartHome.DTO;

public class LightDTO {

    public String reqBody = "{\n" +
            "    \"type\": \"SetState\",\n" +
            "    \"namespace\": \"core.RWE\",\n" +
            "    \"target\": \"/capability/cbc79784424d442e9372c1e24f910ec1\",\n" +
            "    \"params\": {\n" +
            "        \"onState\": {\n" +
            "            \"type\": \"Constant\",\n" +
            "            \"value\": false\n" +
            "        }\n" +
            "    }\n" +
            "}";

    public LightDTO(String reqBody){
        super();
        this.reqBody = reqBody;
    }
}
