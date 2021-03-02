package com.unipi.p17172p17168p17164.efruit.Utils;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DBHelper {

    public static Query getShopWorkingTimes(FirebaseFirestore db, String shopId) {
        Query q = db.collection("shops").document(shopId)
                    .collection("working_times");

        // Snapshot listener for realtime database updates
        q.addSnapshotListener((snapshots, e) -> {
            if (e != null)
                return;

            assert snapshots != null;
            for (DocumentChange dc : snapshots.getDocumentChanges())
                switch (dc.getType()) {
                    case ADDED:
                        Log.d("TAG", "New Shop Item: " + dc.getDocument().getData());
                        break;
                    case MODIFIED:
                        Log.d("TAG", "Modified Shop Item: " + dc.getDocument().getData());
                        break;
                    case REMOVED:
                        Log.d("TAG", "Removed Shop Item: " + dc.getDocument().getData());
                        break; }
        });

        return q;
    }

    public static Query getShopGeohash(FirebaseFirestore db, String shopId) {
        Query queryShopDetails = db.collection("carts")
                                   .whereEqualTo("shopId", shopId);

        queryShopDetails.addSnapshotListener((snapshots, e) -> {
            if (e != null)
                return;

            assert snapshots != null;
            for (DocumentChange dc : snapshots.getDocumentChanges())
                switch (dc.getType()) {
                    case ADDED:
                        Log.d("TAG", "New Shop Item: " + dc.getDocument().getData());
                        break;
                    case MODIFIED:
                        Log.d("TAG", "Modified Shop Item: " + dc.getDocument().getData());
                        break;
                    case REMOVED:
                        Log.d("TAG", "Removed Shop Item: " + dc.getDocument().getData());
                        break; }
        });

        return queryShopDetails;
    }

    // Sets the selected current product amount in cart
    public static Task<Void> setSelectedItemAmount(FirebaseFirestore db, String userId, String productId, int amount) {
        return db.collection("carts")
          .document(userId)
          .collection("products")
          .document(productId)
          .update("amount", amount);
    }

    public static void setCartGrandTotal(FirebaseFirestore db, String userId, double grandTotal) {
        Query cardDetails = db.collection("carts")
                              .whereEqualTo("userId", userId);

        cardDetails.get().addOnCompleteListener(v -> {
            if (v.isSuccessful()) {
                Map< String, Object > cart = new HashMap< >();
                cart.put("grand_total", grandTotal);

                db.collection("carts")
                  .document(userId)
                  .update(cart);
            }
        });
    }

    public static Task<Void> setCartItem(FirebaseFirestore db, String userId, String shopId, String productId, double price, int amount) {
       Query cardDetails = db.collection("carts")
                             .whereEqualTo("userId", userId);

       cardDetails.get().addOnCompleteListener(v -> {
            if (v.isSuccessful()) {
                if (v.getResult().isEmpty()) {
                    Map< String, Object > mapCartDetails = new HashMap< >();
                    mapCartDetails.put("shopId", shopId);
                    mapCartDetails.put("userId", userId);
                    mapCartDetails.put("grand_total", 0);

                    getCartDetails(db, userId).set(mapCartDetails);
                }
            }
       });

        Map< String, Object > updatedCart = new HashMap< >();
        updatedCart.put("amount", amount);
        updatedCart.put("productId", productId);
        updatedCart.put("price", price);

        return db.collection("carts")
                 .document(userId)
                 .collection("products")
                 .document(productId)
                 .set(updatedCart);
    }

    // Get the item in cart if it exists (WARNING this will produce null pointer exception of you enter a invalid path)
    public static DocumentReference getCartItemRef(FirebaseFirestore db, String userId, String productId) {
        return db.collection("carts")
                 .document(userId)
                 .collection("products")
                 .document(productId);
    }

    public static DocumentReference getProductInfo(FirebaseFirestore db, String shopId, String productId) {
        DocumentReference documentRefProductInfo = db.collection("shops")
                                                     .document(shopId)
                                                     .collection("products")
                                                     .document(productId);

        documentRefProductInfo.addSnapshotListener((snapshots, e) -> { });

        return documentRefProductInfo;
    }

    public static DocumentReference getCartDetails(FirebaseFirestore db, String userId) {
        DocumentReference documentRefCartDetails = db.collection("carts")
                                                     .document(userId);

        documentRefCartDetails.addSnapshotListener((snapshots, e) -> { });

        return documentRefCartDetails;
    }

    public static DocumentReference getShopName(FirebaseFirestore db, String shopId) {
        DocumentReference documentRefShopName = db.collection("shops")
                                                  .document(shopId);

        documentRefShopName.addSnapshotListener((snapshots, e) -> { });

        return documentRefShopName;
    }

    public static Query getCartItem(FirebaseFirestore db, String userId, String productId) {
        Query documentRefCartItem = db.collection("carts")
                                      .document(userId)
                                      .collection("products")
                                      .whereEqualTo("productId", productId);

        documentRefCartItem.addSnapshotListener((snapshots, e) -> {
            if (e != null)
                return;

            assert snapshots != null;
            for (DocumentChange dc : snapshots.getDocumentChanges())
                switch (dc.getType()) {
                    case ADDED:
                        Log.d("TAG", "New Cart Item: " + dc.getDocument().getData());
                        break;
                    case MODIFIED:
                        Log.d("TAG", "Modified Cart Item: " + dc.getDocument().getData());
                        break;
                    case REMOVED:
                        Log.d("TAG", "Removed Cart Item: " + dc.getDocument().getData());
                        break; }
        });

        return documentRefCartItem;
    }

    public static Query getTotalCartItems(FirebaseFirestore db, String userId) {
        Query queryCartItems = db.collection("carts")
                                 .document(userId)
                                 .collection("products");

        queryCartItems.addSnapshotListener((snapshots, e) -> {
            if (e != null)
                return;

            assert snapshots != null;
            for (DocumentChange dc : snapshots.getDocumentChanges())
                switch (dc.getType()) {
                    case ADDED:
                        Log.d("TAG", "New Cart Item: " + dc.getDocument().getData());
                        break;
                    case MODIFIED:
                        Log.d("TAG", "Modified Cart Item: " + dc.getDocument().getData());
                        break;
                    case REMOVED:
                        Log.d("TAG", "Removed Cart Item: " + dc.getDocument().getData());
                        break; }
        });

        return queryCartItems;
    }

    public static DocumentReference getShopDetails(FirebaseFirestore db, String shopId) {
        DocumentReference documentRefShopDetails = db.collection("shops")
                                                     .document(shopId);

        documentRefShopDetails.addSnapshotListener((snapshots, e) -> { });

        return documentRefShopDetails;
    }

    public static Task<QuerySnapshot> checkIfUserExists(FirebaseFirestore db, String userId) {

        return db.collection("users").whereEqualTo("userId", userId)
                                  .get();
    }
}
