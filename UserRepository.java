package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.User;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<User, Long> {  //<entity type, entity id type>
}
