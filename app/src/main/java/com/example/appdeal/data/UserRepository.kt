package com.example.appdeal.data

class UserRepository {
    private val users = mutableListOf<User>()

    fun signUp(email: String, password: String, name: String? = null): Result<User> {
        // Check if email already exists
        if (users.any { it.email == email }) {
            return Result.failure(Exception("Email already registered"))
        }

        // Create new user
        val newUser = User(
            id = java.util.UUID.randomUUID().toString(),
            email = email,
            password = password,
            name = name
        )
        users.add(newUser)
        return Result.success(newUser)
    }

    fun login(email: String, password: String): Result<User> {
        val user = users.find { it.email == email && it.password == password }
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    fun getUserById(id: String): User? {
        return users.find { it.id == id }
    }

    fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }
} 