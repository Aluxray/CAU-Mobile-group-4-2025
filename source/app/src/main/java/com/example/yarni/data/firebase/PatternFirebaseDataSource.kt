package com.example.yarni.data.firebase

import android.net.Uri
import com.example.yarni.data.model.PatternDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class PatternFirebaseDataSource {

    private val collection = Firebase.firestore.collection("patterns")

    suspend fun addPattern(uri: Uri, fileName: String, fileSize: Long) {

        val document = hashMapOf(
            "name" to fileName.substringBeforeLast("."),  // titre par défaut
            "filename" to fileName,
            "size" to fileSize,
            "date" to Timestamp.now(),
        )

        collection.add(document).await()
    }

    suspend fun getPattern(id: String): PatternDto? {
        return collection.document(id).get().await().toObject(PatternDto::class.java)
    }

    suspend fun deletePattern(id: String) {
        collection.document(id).delete().await()
    }

    suspend fun updatePattern(id: String, newTitle: String) {
        collection.document(id)
            .update("name", newTitle)
            .await()
    }

    suspend fun getPatterns(): List<Pair<String, PatternDto>> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            val dto = doc.toObject(PatternDto::class.java)
            if (dto != null) (doc.id to dto) else null
        }
    }
}
