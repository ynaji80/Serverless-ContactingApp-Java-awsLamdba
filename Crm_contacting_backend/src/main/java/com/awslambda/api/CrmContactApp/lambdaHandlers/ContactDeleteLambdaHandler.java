package com.awslambda.api.CrmContactApp.lambdaHandlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.awslambda.api.CrmContactApp.model.Contact;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class ContactDeleteLambdaHandler implements RequestStreamHandler {
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


        try {
            JsonObject requestObject = (JsonObject) JsonParser.parseReader(reader);
            // where request is with pathParameters
            if (requestObject.get("pathParameters") != null){
                JsonObject params = (JsonObject) requestObject.get("pathParameters");
                if (params.get("id") != null){
                    int id = Integer.parseInt(params.get("id").toString());
                    dynamoDB.getTable(DYNAMO_TABLE).deleteItem("id", id);
                }

            }
            responseBody.addProperty("message", "Item deleted");
            responseBody.addProperty("statusCode", 200);
            responseBody.addProperty("body", responseBody.toString());
        } catch (Exception e){
            logger.log("Error : " + e.getMessage());
            responseBody.addProperty("statusCode", 400);
        }
        writer.write(responseObject.toString());
        writer.close();
        reader.close();

    }
}
