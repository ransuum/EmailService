package org.practice.emailservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.practice.emailservice.entity.dto.*;
import org.practice.emailservice.entity.model.FileInLetter;
import org.practice.emailservice.entity.model.Letter;
import org.practice.emailservice.entity.model.Users;
import org.practice.emailservice.entity.request.SendLetterRequest;
import org.practice.emailservice.exception.BanException;
import org.practice.emailservice.exception.NotFoundException;
import org.practice.emailservice.repo.FileInLetterRepo;
import org.practice.emailservice.repo.LetterRepo;
import org.practice.emailservice.repo.UsersRepo;
import org.practice.emailservice.utils.mapper.MapStruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepo letterRepo;
    private final FileInLetterRepo fileInLetterRepo;
    private final UsersRepo usersRepo;

    public LetterDto sendLetter(SendLetterRequest sendLetterRequest, MultipartFile[] file) throws IOException {
        Users usersTo = usersRepo.findByEmail(sendLetterRequest.getEmailTo()).orElseThrow(()
                -> new NotFoundException("User not found by email " + sendLetterRequest.getEmailTo()));

        Users usersBy = usersRepo.findByEmail(getEmailFromAuth()).orElseThrow(()
                -> new NotFoundException("APP ERROR"));

        usersTo.getBanUsers().forEach(banUsers -> {
            if (banUsers.getBanUser().getId().equals(usersBy.getId())) throw new BanException("You cannot send letters because you are banned by this user(");
        });

        Letter letter = Letter.builder()
                .text(sendLetterRequest.getText())
                .topic(sendLetterRequest.getTopic())
                .createdAt(Instant.now())
                .build();


        if (file != null) {
            for (MultipartFile file1 : file) {
                FileInLetter fileInLetter = FileInLetter.builder()
                        .fileContent(file1.getBytes())
                        .letter(letter)
                        .fileName(file1.getOriginalFilename())
                        .storage(file1.getSize())
                        .build();

                fileInLetterRepo.save(fileInLetter);
                letter.getFiles().add(fileInLetter);
            }
        }

        letter.setUserBy(usersBy);
        letter.setUserTo(usersTo);

        letterRepo.save(letter);

        usersBy.getSendLetters().add(letter);
        usersTo.getMyLetters().add(letter);

        usersRepo.save(usersBy);
        usersRepo.save(usersTo);

        return MapStruct.INSTANCE.letterToDto(letter);
    }

    public List<LetterDto> getLetter(String email) {
        return letterRepo.findLettersByUserByAndUserTo(
                        usersRepo.findByEmail(getEmailFromAuth())
                                .orElseThrow(() -> new NotFoundException("Error in app")),
                        usersRepo.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("User with email(" + email + ") not found")))
                .stream()
                .sorted(Comparator.comparing(Letter::getCreatedAt))
                .map(MapStruct.INSTANCE::letterToDto)
                .toList();
    }

    public LetterDto getLetterById(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return MapStruct.INSTANCE.letterToDto(
                letterRepo.findByIdAndUserBy(id, usersRepo.findByEmail(authentication.getName()).orElse(null)).orElseThrow(()
                        -> new NotFoundException("Letter with this id not found"))
        );
    }

    public void deleteLetterFromMe(String id){
        Letter letter = letterRepo.findByIdAndUserBy(id, usersRepo.findByEmail(getEmailFromAuth()).orElse(null))
                .orElseThrow(()
                        -> new NotFoundException("Letter with this id not found"));

        Users users = letter.getUserBy();
        users.getSendLetters().remove(letter);
        usersRepo.save(users);

        letterRepo.delete(letter);
    }

    public void deleteLetterFromAll(String id){
        Letter letter = letterRepo.findByIdAndUserBy(id, usersRepo.findByEmail(getEmailFromAuth()).orElse(null))
                .orElseThrow(()
                        -> new NotFoundException("Letter with this id not found"));

        Users usersBy = letter.getUserBy();
        usersBy.getSendLetters().remove(letter);
        usersRepo.save(usersBy);

        Users usersTo = letter.getUserTo();
        usersTo.getMyLetters().remove(letter);
        usersRepo.save(usersTo);

        letterRepo.delete(letter);
    }

    public List<LetterDto> getByStartOfTheTopic(String searchQuery) {
        Users currentUser = usersRepo.findByEmail(getEmailFromAuth()).orElse(null);

        List<Letter> letters = letterRepo.findByTopicContaining(searchQuery);

        List<Letter> filteredLetters = letters.stream()
                .filter(letter -> letter.getUserBy().equals(currentUser) || letter.getUserTo().equals(currentUser))
                .sorted(Comparator.comparing(Letter::getCreatedAt))
                .toList();

        return filteredLetters.stream()
                .map(MapStruct.INSTANCE::letterToDto)
                .toList();
    }

    public List<LetterDto> getAllTopic(){
        return letterRepo.findLettersByUserBy(usersRepo.findByEmail(getEmailFromAuth()).orElse(null))
                .stream()
                .sorted(Comparator.comparing(Letter::getCreatedAt))
                .map(MapStruct.INSTANCE::letterToDto)
                .toList();
    }

    public UserFullInfoDto checkMyProfile(){
        return MapStruct.INSTANCE.userToFullInfoDto(usersRepo
                .findByEmail(getEmailFromAuth())
                .get());
    }

    public List<MyLetters> myLetters(){
        return usersRepo.findByEmail(getEmailFromAuth()).orElseThrow(() -> new NotFoundException("APP ERROR")).getMyLetters()
                .stream()
                .map(MapStruct.INSTANCE::letterToMyLetters)
                .toList();

    }

    public List<SendLetters> sendLetters(){
        return usersRepo.findByEmail(getEmailFromAuth()).orElseThrow(() -> new NotFoundException("APP ERROR")).getSendLetters()
                .stream()
                .map(MapStruct.INSTANCE::letterToSendLetters)
                .toList();

    }

    private String getEmailFromAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
