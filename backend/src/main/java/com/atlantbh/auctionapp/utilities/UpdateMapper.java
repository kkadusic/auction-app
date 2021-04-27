package com.atlantbh.auctionapp.utilities;

import com.atlantbh.auctionapp.model.Card;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.request.CardRequest;
import com.atlantbh.auctionapp.request.UpdateProfileRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UpdateMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePerson(UpdateProfileRequest updateProfileRequest, @MappingTarget Person person);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCard(CardRequest cardRequest, @MappingTarget Card card);
}
