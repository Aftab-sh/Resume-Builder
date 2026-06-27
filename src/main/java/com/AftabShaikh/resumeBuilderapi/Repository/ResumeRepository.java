package com.AftabShaikh.resumeBuilderapi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.AftabShaikh.resumeBuilderapi.Entity.Resume;
import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume,Long>
{

	//user id se hame finder method bnana he 
	List<Resume> findByUserIdOrderByUpdatedAtDesc(Long userId);
	//we find resume only the logedin user to issiliye findByUserIdAndId
	Optional<Resume>findByUserIdAndId(Long userId,Long resumeid);
}
/*
 * “This repository is used to perform database operations for Resume entity.
It extends JpaRepository to get built-in CRUD functionality and uses method naming conventions to create custom queries without writing SQL.”
 */

/*
Q1: Repository kya hota hai?
“It is used to interact with the database.”

Q2: JpaRepository kyu use kiya?
“To avoid writing boilerplate CRUD code.”

Q3: Interface kyu banaya class nahi?
“Spring automatically provides implementation at runtime.”

 Important ( Must)
Q4: Ye method kaise kaam kar raha hai bina SQL ke?
“Spring Data JPA creates query automatically based on method name.”

Q5: findByUserIdAndId kyu use kiya?
“To ensure user can access only their own resume.”

Q6: Optional kyu use kiya?
“To avoid null pointer exception and handle absence of data safely.”

Q7: OrderByUpdatedAtDesc ka kya matlab?
“Sort results in descending order based on updated time.”
 */

/*
 * Ye class kyu use ki gayi hai?
 * 
Database se data fetch / save / update / delete karna
Custom queries likhna without SQL
 */

/*
 * Service call karta hai repository ko
Repository DB se baat karta hai
Data fetch/save hota hai
Result wapas service ko milta hai
 */
