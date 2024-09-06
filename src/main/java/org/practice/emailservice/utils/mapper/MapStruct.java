package org.practice.emailservice.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.practice.emailservice.entity.dto.*;
import org.practice.emailservice.entity.model.BanUsers;
import org.practice.emailservice.entity.model.Letter;
import org.practice.emailservice.entity.model.Users;
import org.practice.emailservice.entity.request.SignUpRequest;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface MapStruct {
    MapStruct INSTANCE = Mappers.getMapper(MapStruct.class);

    UsersDto userToDto(Users user);

    @Mapping(source = "userBy", target = "userBy")
    @Mapping(source = "userTo", target = "userTo")
    @Mapping(source = "files", target = "files")
    LetterDto letterToDto(Letter letter);

//    default List<LetterDto> mapLetterList(List<Letter> letters) {
//        if (letters == null) {
//            return null;
//        }
//        return letters.stream()
//                .map(this::letterToDto)
//                .collect(Collectors.toList());
//    }

    @Mapping(source = "firstname", target = "firstName")
    @Mapping(source = "lastname", target = "lastName")
    Users requestToUser(SignUpRequest signUp);


    BanUserDto banToDto(BanUsers banUsers);

    @Mapping(source = "sendLetters", target = "sendLetters")
    @Mapping(source = "myLetters", target = "myLetters")
    UserFullInfoDto userToFullInfoDto(Users user);

    @Mapping(source = "userBy", target = "userBy")
    @Mapping(source = "files", target = "files")
    MyLetters letterToMyLetters(Letter letter);

    @Mapping(source = "userTo", target = "userTo")
    SendLetters letterToSendLetters(Letter letter);
}
