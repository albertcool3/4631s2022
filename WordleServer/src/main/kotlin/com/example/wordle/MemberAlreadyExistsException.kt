package com.example.wordle

class MemberAlreadyExistsException: Exception(
    "There is already a member with that username in this Wordle game."
)