package ru.senla.autoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GarageWithPlaceNumbersDto {

    private Long id;
    private List<Integer> placeNumbers;

}
