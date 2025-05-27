package com.example.appdeal.data

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseManager {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    
    private val TAG = "FirebaseManager"
    
    // Google Sign In
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        // Configure Google Sign In with web client ID
        // Make sure this matches the web client ID in your Firebase console
        val webClientId = "706356558148-lnkipcmr41r94tnt8u41uvspk07e04ug.apps.googleusercontent.com"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        
        return GoogleSignIn.getClient(context, gso)
    }
    
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            Log.d(TAG, "Starting Firebase authentication with Google credential")
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            
            try {
                val authResult = auth.signInWithCredential(credential).await()
                Log.d(TAG, "Firebase signInWithCredential successful")
                
                authResult.user?.let {
                    Log.d(TAG, "Firebase user retrieved successfully: ${it.uid}")
                    Result.success(it)
                } ?: run {
                    Log.e(TAG, "Firebase auth successful but user is null")
                    Result.failure(Exception("Firebase user is null after authentication"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Firebase signInWithCredential failed", e)
                Result.failure(e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Google credential creation failed", e)
            Result.failure(e)
        }
    }
    
    // Authentication Methods
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Authentication failed"))
        } catch (e: Exception) {
            Log.e(TAG, "Sign in failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun signUp(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("User creation failed"))
        } catch (e: Exception) {
            Log.e(TAG, "Sign up failed", e)
            Result.failure(e)
        }
    }
    
    fun signOut(context: Context) {
        // Sign out from Firebase
        auth.signOut()
        
        // Sign out from Google
        getGoogleSignInClient(context).signOut()
    }
    
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    
    // Firestore Methods
    suspend fun saveRecipe(recipe: Recipe): Result<String> {
        return try {
            val recipeMap = mapOf(
                "id" to recipe.id,
                "title" to recipe.title,
                "description" to recipe.description,
                "imageUrl" to recipe.imageUrl,
                "prepTime" to recipe.prepTime,
                "cookTime" to recipe.cookTime,
                "servings" to recipe.servings,
                "category" to recipe.category,
                "tags" to recipe.tags,
                "ingredients" to recipe.ingredients,
                "instructions" to recipe.instructions,
                "relatedProductIds" to recipe.relatedProductIds
            )
            
            val documentRef = if (recipe.id.isNotEmpty()) {
                firestore.collection("recipes").document(recipe.id)
            } else {
                firestore.collection("recipes").document()
            }
            
            documentRef.set(recipeMap).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving recipe", e)
            Result.failure(e)
        }
    }
    
    suspend fun getRecipes(): Result<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = firestore.collection("recipes").get().await()
                val recipes = snapshot.documents.mapNotNull { document ->
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val description = document.getString("description") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val prepTime = document.getLong("prepTime")?.toInt() ?: 0
                    val cookTime = document.getLong("cookTime")?.toInt() ?: 0
                    val servings = document.getLong("servings")?.toInt() ?: 0
                    val category = document.getString("category") ?: ""
                    val tags = document.get("tags") as? List<String> ?: listOf()
                    val ingredients = document.get("ingredients") as? List<String> ?: listOf()
                    val instructions = document.get("instructions") as? List<String> ?: listOf()
                    val relatedProductIds = document.get("relatedProductIds") as? List<String> ?: listOf()
                    
                    Recipe(
                        id = id,
                        title = title,
                        description = description,
                        imageUrl = imageUrl,
                        prepTime = prepTime,
                        cookTime = cookTime,
                        servings = servings,
                        category = category,
                        tags = tags,
                        ingredients = ingredients,
                        instructions = instructions,
                        relatedProductIds = relatedProductIds
                    )
                }
                Result.success(recipes)
            } catch (e: Exception) {
                Log.e(TAG, "Error getting recipes", e)
                Result.failure(e)
            }
        }
    }
    
    suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return try {
            firestore.collection("recipes").document(recipeId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting recipe", e)
            Result.failure(e)
        }
    }
    
    // Storage Methods
    suspend fun uploadImage(imageBytes: ByteArray, fileName: String): Result<String> {
        return try {
            val storageRef = storage.reference.child("images/$fileName")
            val uploadTask = storageRef.putBytes(imageBytes).await()
            val downloadUrl = storageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading image", e)
            Result.failure(e)
        }
    }
    
    // User Profile Methods
    suspend fun updateUserDisplayName(user: FirebaseUser, displayName: String): Result<Unit> {
        return try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            
            user.updateProfile(profileUpdates).await()
            Log.d(TAG, "Display name updated successfully for user: ${user.uid}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating display name", e)
            Result.failure(e)
        }
    }
} 