package com.seemee.userservice.repository.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import com.seemee.userservice.config.MongoDBConnection;
import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.dto.AuthenticateUser;
import com.seemee.userservice.model.User;
import com.seemee.userservice.repository.UserRepository;
import org.bson.Document;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import java.util.logging.Logger;

@Repository
public class UserRepositoryImpl implements UserRepository {
    Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());

    @Override
    public User getUserProfile(String email) {
        logger.info("Email:- " + email);
        User user = new User();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {

            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Query for vendors with the given street name
            MongoCursor<Document> cursor = collection.find(
                    new Document("email", email)).iterator();

            for (MongoCursor<Document> it = cursor; it.hasNext();) {
                Document doc = it.next();
                user = objectMapper.readValue(doc.toJson(), User.class);
            }
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
        }

        return user;
    }

    @Override
    public void createUserProfile(User user) {
        // Configure ObjectMapper to exclude null values
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try (MongoClient client = MongoDBConnection.connect()) {

            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Convert user to JSON string (excluding null values)
            String userJson = objectMapper.writeValueAsString(user);

            // Parse to Document
            Document userDoc = Document.parse(userJson);

            // Remove the _id field if it exists and is null, let MongoDB generate it
            if (userDoc.containsKey("_id") && userDoc.get("_id") == null) {
                userDoc.remove("_id");
            }

            // Insert the document
            collection.insertOne(userDoc);

            logger.info("User profile created successfully for email: " + user.getEmail());

        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            throw new RuntimeException("Failed to create user profile: " + e.getMessage());
        }
    }

    @Override
    public User authenticateUser(AuthenticateUser authenticateUser) {
        String email = authenticateUser.getEmail();
        String password = authenticateUser.getPassword();
        logger.info("Email:- " + email);
        User user = new User();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {

            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Query for vendors with the given street name
            MongoCursor<Document> cursor = collection.find(
                    new Document("email", email)).iterator();

            for (MongoCursor<Document> it = cursor; it.hasNext();) {
                Document doc = it.next();
                user = objectMapper.readValue(doc.toJson(), User.class);
            }
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
        }
        return user;

    }

    public String updatePassword(AuthenticateUser authenticateUser) {
        String email = authenticateUser.getEmail();
        String password = authenticateUser.getPassword();
        logger.info("Email:- " + email);
        User user = new User();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {

            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Query for vendors with the given street name
            MongoCursor<Document> cursor = collection.find(
                    new Document("email", email)).iterator();

            for (MongoCursor<Document> it = cursor; it.hasNext();) {
                Document doc = it.next();
                user = objectMapper.readValue(doc.toJson(), User.class);
            }
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
        }

        if (user.getEmail() != null && user.getEmail().equals(email)) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            user.setPassword(hashedPassword);
            try (MongoClient client = MongoDBConnection.connect()) {

                // Get the database
                MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

                // Get the collection
                MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

                // Update the document
                collection.updateOne(new Document("email", email),
                        new Document("$set", new Document("password", hashedPassword)));

            } catch (Exception e) {
                logger.info("Exception: " + e.getMessage());
            }
            return "Success";
        } else {
            return "Failure";
        }
    }

    public String updateAddress(User user, String index) {
        String email = user.getEmail();
        logger.info("Email:- " + email + ", Index:- " + index);

        try (MongoClient client = MongoDBConnection.connect()) {

            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Find the address to update
            if (user.getAddress() != null && !user.getAddress().isEmpty()) {

                // Get the address to update (first address in the list)
                var addressToUpdate = user.getAddress().get(0);

                // Build the update document with only non-null fields
                Document updateDoc = new Document();

                // Only update non-null and non-empty fields
                if (addressToUpdate.getIndex() != null) {
                    updateDoc.append("address.$.index", addressToUpdate.getIndex());
                }
                if (isValidString(addressToUpdate.getHouseNo())) {
                    updateDoc.append("address.$.houseNo", addressToUpdate.getHouseNo());
                }
                if (isValidString(addressToUpdate.getApartmentName())) {
                    updateDoc.append("address.$.apartmentName", addressToUpdate.getApartmentName());
                }
                if (isValidString(addressToUpdate.getLandmark())) {
                    updateDoc.append("address.$.landmark", addressToUpdate.getLandmark());
                }
                if (isValidString(addressToUpdate.getStreet())) {
                    updateDoc.append("address.$.street", addressToUpdate.getStreet());
                }
                if (isValidString(addressToUpdate.getCity())) {
                    updateDoc.append("address.$.city", addressToUpdate.getCity());
                }
                if (isValidString(addressToUpdate.getState())) {
                    updateDoc.append("address.$.state", addressToUpdate.getState());
                }
                if (isValidString(addressToUpdate.getZipCode())) {
                    updateDoc.append("address.$.zipCode", addressToUpdate.getZipCode());
                }
                if (isValidString(addressToUpdate.getCountry())) {
                    updateDoc.append("address.$.country", addressToUpdate.getCountry());
                }
                if (isValidString(addressToUpdate.getAddressType())) {
                    updateDoc.append("address.$.addressType", addressToUpdate.getAddressType());
                }

                // Only update if there are fields to update
                if (!updateDoc.isEmpty()) {
                    // Filter to find the specific address by index in the array
                    Document filter = new Document("email", email)
                            .append("address.index", Integer.parseInt(index));

                    Document update = new Document("$set", updateDoc);

                    var result = collection.updateOne(filter, update);

                    if (result.getModifiedCount() > 0) {
                        logger.info("Address updated successfully for email: " + email + ", index: " + index);
                        return "Success";
                    } else {
                        logger.info("No address found to update for email: " + email + ", index: " + index);
                        return "Address not found";
                    }
                } else {
                    logger.info("No non-null fields to update for email: " + email);
                    return "No fields to update";
                }
            } else {
                logger.info("No address provided for update");
                return "No address provided";
            }

        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            return "Failure: " + e.getMessage();
        }
    }

    // Helper method to check if string is valid (not null and not empty after trim)
    private boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

}