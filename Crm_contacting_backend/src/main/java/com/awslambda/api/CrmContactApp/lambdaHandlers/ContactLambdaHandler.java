package com.awslambda.api.CrmContactApp.lambdaHandlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.awslambda.api.CrmContactApp.model.Contact;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class ContactLambdaHandler implements RequestStreamHandler {
    private String DYNAMO_TABLE = "Contacts";

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        // Logging handler properties
        logger.log("Context : " + context);
        logger.log("FunctionName : " + context.getFunctionName());
        logger.log("Context test : " + new Gson().toJson(context));
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedReader reader = new BufferedReader( new InputStreamReader(inputStream));
        JsonObject responseObject = new JsonObject();
        JsonObject responseBody = new JsonObject();
        //DynamoDB
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        int id;
        Item resItem = null;
        try {
            JsonObject requestObject = (JsonObject) JsonParser.parseReader(reader);
            // where request is with pathParameters
            if (requestObject.get("pathParameters") != null){
                JsonObject params = (JsonObject) requestObject.get("pathParameters");
                if (params.get("id") != null ){
                    id = Integer.parseInt(params.get("id").getAsString());
                    resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("id",id);
                }
            }
            // where request is with queryStringParameters
            else if (requestObject.get("queryStringParameters") != null){
                JsonObject params = (JsonObject) requestObject.get("queryStringParameters");
                if (params.get("id") != null ){
                    id = Integer.parseInt(params.get("id").getAsString());
                    resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("id",id);
                }
            }
            if(resItem != null) {
                Contact contact = new Contact(resItem.toJSON());
                responseBody.add("contact", new Gson().toJsonTree(contact));
                responseBody.addProperty("statusCode", 200);
            }
            else {
                responseBody.addProperty("message", "No Contacts Found");
                responseBody.addProperty("statusCode", 404);
            }
            responseObject.addProperty("body", responseBody.toString());

        } catch (Exception e){
            logger.log("Error : " + e.getMessage());
        }
        writer.write(responseObject.toString());
        writer.close();
        reader.close();




    }
}
