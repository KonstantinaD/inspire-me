package com.inspireme.repository;

import com.inspireme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {  //<entity type, entity id type>

}
