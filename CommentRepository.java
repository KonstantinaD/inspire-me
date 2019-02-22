package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

interface CommentRepository extends JpaRepository<Comment, Long>{
}
