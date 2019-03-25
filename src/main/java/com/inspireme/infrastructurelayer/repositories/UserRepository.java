package com.inspireme.infrastructurelayer.repositories;

import com.inspireme.domainlayer.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Long> {  //<entity type, entity id type>

}
