package org.practice.emailservice.repo;

import org.practice.emailservice.entity.model.FileInLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInLetterRepo extends JpaRepository<FileInLetter, String> {

}
