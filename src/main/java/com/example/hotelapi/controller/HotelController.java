package com.example.hotelapi.controller;

import com.example.hotelapi.dto.CreateHotelRequest;
import com.example.hotelapi.dto.HotelDetailDto;
import com.example.hotelapi.dto.HotelSummaryDto;
import com.example.hotelapi.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // Получение списка отелей с краткой информацией
    @GetMapping("/hotels")
    public List<HotelSummaryDto> getAllHotels() {
        return hotelService.getAllHotelsDto();
    }

    // Получение подробной информации об отеле
    @GetMapping("/hotels/{id}")
    public HotelDetailDto getHotelById(@PathVariable Long id) {
        return hotelService.getHotelDetailDto(id);
    }

    // Создание отеля
    @PostMapping("/hotels")
    public ResponseEntity<HotelSummaryDto> createHotel(
            @RequestBody CreateHotelRequest request) {

        HotelSummaryDto createdHotel =
                hotelService.createHotelDto(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHotel);
    }

    // Поиск отелей по параметрам
    @GetMapping("/search")
    public List<HotelSummaryDto> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenities) {

        return hotelService.searchHotelsDto(
                name,
                brand,
                city,
                country,
                amenities
        );
    }

    // Добавление удобств отелю
    @PostMapping("/hotels/{id}/amenities")
    public HotelSummaryDto addAmenities(
            @PathVariable Long id,
            @RequestBody List<String> amenities) {

        return hotelService.addAmenitiesDto(id, amenities);
    }

    // Получение статистики по параметру
    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(
            @PathVariable String param) {

        return hotelService.getHistogram(param);
    }
}
