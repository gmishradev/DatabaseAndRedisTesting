package db.mongo;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

/**

Create (Insert):

We create a Document object representing the data (similar to a row in a relational database) and insert it into the MongoDB collection using insertOne().
Read (Find):

To retrieve documents, we use the find() method, which returns all documents in the collection. In this case, we iterate through all documents and print them out.
Update:

To update a document, we use the updateOne() method with Filters to match the document and Updates to specify what needs to be updated.
Delete:

The deleteOne() method deletes the first document matching the filter provided.

 **/

public class MongoDBExample {
    public static void main(String[] args) {
        // Connect to the MongoDB server (default host is localhost and default port is 27017)
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

        // Select the database and collection
        MongoDatabase database = mongoClient.getDatabase("testdb");
        MongoCollection<Document> collection = database.getCollection("users");

        // Create (Insert) operation
        Document user1 = new Document("name", "John")
                            .append("age", 30)
                            .append("email", "john@example.com");
        collection.insertOne(user1);
        System.out.println("User Inserted: " + user1);

        // Read operation (Find all documents)
        FindIterable<Document> users = collection.find();
        for (Document user : users) {
            System.out.println("User: " + user.toJson());
        }

        // Update operation
        collection.updateOne(Filters.eq("name", "John"),
                             Updates.set("age", 31));
        System.out.println("Updated John age to 31");

        // Delete operation
      //  collection.deleteOne(Filters.eq("name", "John"));
      //  System.out.println("Deleted John from the collection");

        // Close MongoDB connection
        mongoClient.close();
    }
}
