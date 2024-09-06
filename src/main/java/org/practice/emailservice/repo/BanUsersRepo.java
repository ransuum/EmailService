package org.practice.emailservice.repo;

import org.practice.emailservice.entity.model.BanUsers;
import org.practice.emailservice.entity.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BanUsersRepo extends JpaRepository<BanUsers, String> {
    Optional<BanUsers> findByBanUser(Users users);

    Optional<BanUsers> findByPersonalAccountAndBanUser(Users personalAccount, Users banUser);

    List<BanUsers> findByPersonalAccount(Users personalAccount);

    Optional<BanUsers> findByIdAndPersonalAccount(String id, Users personalAccount);
}
