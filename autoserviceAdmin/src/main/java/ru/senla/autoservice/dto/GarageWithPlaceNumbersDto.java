package ru.senla.autoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarageWithPlaceNumbersDto {

    private Long id;
    private List<Integer> placeNumbers;

}
