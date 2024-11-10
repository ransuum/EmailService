package org.practice.emailservice.service;

import lombok.RequiredArgsConstructor;
import org.practice.emailservice.entity.dto.BanUserDto;
import org.practice.emailservice.entity.model.BanUsers;
import org.practice.emailservice.entity.model.Users;
import org.practice.emailservice.entity.request.BanUserRequest;
import org.practice.emailservice.exception.NotFoundException;
import org.practice.emailservice.repo.BanUsersRepo;
import org.practice.emailservice.repo.UsersRepo;
import org.practice.emailservice.utils.mapper.MapStruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BanUsersService {
    private final BanUsersRepo banUsersRepo;
    private final UsersRepo usersRepo;

    public BanUserDto createBanUsers(BanUserRequest banUsers) {
        Users yourAccount = usersRepo.findByEmail(getAuthenticatedUsername()).orElseThrow(() -> new NotFoundException("APP ERROR"));

        Users users = usersRepo.findByEmail(banUsers.getEmail()).orElse(null);

        if (users == null) return BanUserDto.builder().reason(banUsers.getReason()).build();

        BanUsers banUsers1 = BanUsers.builder()
                .banUser(users)
                .personalAccount(yourAccount)
                .reason(banUsers.getReason())
                .banType(banUsers.getBanType())
                .banDate(Instant.now())
                .build();

        banUsersRepo.save(banUsers1);

        yourAccount.getBanUsers().add(banUsers1);
        usersRepo.save(yourAccount);

        users.getBanYou().add(banUsers1);
        usersRepo.save(users);

        return MapStruct.INSTANCE.banToDto(banUsers1);
    }

    public void deleteBanUsers(String email) {
        Users personalAcc = usersRepo.findByEmail(getAuthenticatedUsername()).orElseThrow(()
                -> new NotFoundException("APP ERROR"));

        Users banUser = usersRepo.findByEmail(email).orElseThrow(()
                -> new NotFoundException("Not found user with this email"));

        banUsersRepo.findByPersonalAccountAndBanUser(personalAcc, banUser).ifPresent(banUsers -> {
            personalAcc.getBanUsers().remove(banUsers);
            usersRepo.save(personalAcc);

            banUser.getBanYou().remove(banUsers);
            usersRepo.save(banUser);

            banUsersRepo.delete(banUsers);
        });
    }

    public void deleteById(String id){
        BanUsers banUsersEntity = banUsersRepo.findByIdAndPersonalAccount(id, usersRepo.findByEmail(getAuthenticatedUsername())
                .orElseThrow(()
                        -> new NotFoundException("APP ERROR"))).orElseThrow(() -> new NotFoundException("Not found ban with this id"));

        Users banUser = banUsersEntity.getBanUser();
        banUser.getBanYou().remove(banUsersEntity);
        usersRepo.save(banUser);

        Users personalAcc = banUsersEntity.getPersonalAccount();
        personalAcc.getBanUsers().remove(banUsersEntity);
        usersRepo.save(personalAcc);

        banUsersRepo.delete(banUsersEntity);
    }

    @Transactional
    public List<BanUserDto> getBanUsersThatIBanned() {

        Users personalAcc = usersRepo.findByEmail(getAuthenticatedUsername()).orElseThrow(()
                -> new NotFoundException("APP ERROR"));

        return banUsersRepo.findByPersonalAccount(personalAcc)
                .stream()
                .map(MapStruct.INSTANCE::banToDto)
                .sorted(Comparator.comparing(BanUserDto::getBanDate))
                .toList();
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
