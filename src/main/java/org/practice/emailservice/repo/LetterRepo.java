package org.practice.emailservice.repo;

import org.practice.emailservice.entity.dto.LetterDto;
import org.practice.emailservice.entity.model.Letter;
import org.practice.emailservice.entity.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LetterRepo extends JpaRepository<Letter, String> {
    List<Letter> findLettersByUserByAndUserTo(Users by, Users to);

    Optional<Letter> findByIdAndUserBy(String id, Users by);

    List<Letter> findByTopicAndUserBy(String topic, Users by);

    List<Letter> findLettersByUserBy(Users by);

    List<Letter> findByTopicContaining(String topic);

}
