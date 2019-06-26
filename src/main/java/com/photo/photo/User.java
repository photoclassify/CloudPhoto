package com.photo.photo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User
{
    @Id
    String  userId;

    String password;
}
