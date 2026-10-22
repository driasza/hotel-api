package com.example.hotelapi.service;

import com.example.hotelapi.dto.CreateHotelRequest;
import com.example.hotelapi.dto.HotelDetailDto;
import com.example.hotelapi.dto.HotelSummaryDto;
import com.example.hotelapi.entity.Amenity;
import com.example.hotelapi.entity.Hotel;
import com.example.hotelapi.mapper.HotelMapper;
import com.example.hotelapi.repository.AddressRepository;
import com.example.hotelapi.repository.AmenityRepository;
import com.example.hotelapi.repository.HotelRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final AddressRepository addressRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    public HotelService(
            HotelRepository hotelRepository,
            AddressRepository addressRepository,
            AmenityRepository amenityRepository,
            HotelMapper hotelMapper
    ) {
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
        this.amenityRepository = amenityRepository;
        this.hotelMapper = hotelMapper;
    }

    // Получение всех отелей в виде кратких DTO
    @Transactional
    public List<HotelSummaryDto> getAllHotelsDto() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toSummaryDto)
                .toList();
    }

    // Получение отеля по ID в виде подробного DTO
    @Transactional
    public HotelDetailDto getHotelDetailDto(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hotel not found"
                ));

        return hotelMapper.toDetailDto(hotel);
    }

    // Создание отеля и возврат краткого DTO
    @Transactional
    public HotelSummaryDto createHotelDto(CreateHotelRequest request) {
        Hotel hotel = hotelMapper.toEntity(request);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toSummaryDto(savedHotel);
    }

    // Исходный метод получения всех Entity
    @Transactional
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    // Исходный метод получения Entity по ID
    @Transactional
    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    // Создание Entity
    @Transactional
    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    // Поиск отелей
    @Transactional
    public List<Hotel> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            String amenities
    ) {
        return hotelRepository.findAll()
                .stream()
                .filter(hotel -> !hasText(name)
                        || containsIgnoreCase(hotel.getName(), name))
                .filter(hotel -> !hasText(brand)
                        || containsIgnoreCase(hotel.getBrand(), brand))
                .filter(hotel -> !hasText(city)
                        || (hotel.getAddress() != null
                        && containsIgnoreCase(
                        hotel.getAddress().getCity(), city)))
                .filter(hotel -> !hasText(country)
                        || (hotel.getAddress() != null
                        && containsIgnoreCase(
                        hotel.getAddress().getCountry(), country)))
                .filter(hotel -> !hasText(amenities)
                        || (hotel.getAmenities() != null
                        && hotel.getAmenities().stream()
                        .anyMatch(a -> containsIgnoreCase(
                                a.getName(), amenities))))
                .toList();
    }

    // Поиск с возвратом кратких DTO
    @Transactional
    public List<HotelSummaryDto> searchHotelsDto(
            String name,
            String brand,
            String city,
            String country,
            String amenities
    ) {
        return searchHotels(name, brand, city, country, amenities)
                .stream()
                .map(hotelMapper::toSummaryDto)
                .toList();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private boolean containsIgnoreCase(String value, String search) {
        return value != null
                && value.toLowerCase(Locale.ROOT)
                .contains(search.trim().toLowerCase(Locale.ROOT));
    }

    // Добавление удобств к отелю
    @Transactional
    public Hotel addAmenities(Long id, List<String> amenities) {
        if (amenities == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amenities list must not be null"
            );
        }

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hotel not found"
                ));

        if (hotel.getAmenities() == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Hotel amenities collection is not initialized"
            );
        }

        for (String name : amenities) {
            if (name == null || name.isBlank()) {
                continue;
            }

            String normalizedName = name.trim();

            boolean alreadyHasAmenity = hotel.getAmenities().stream()
                    .filter(Objects::nonNull)
                    .anyMatch(a -> a.getName() != null
                            && a.getName().trim()
                            .equalsIgnoreCase(normalizedName));

            if (alreadyHasAmenity) {
                continue;
            }

            Amenity amenity = amenityRepository
                    .findByNameIgnoreCase(normalizedName)
                    .orElseGet(() -> {
                        Amenity newAmenity = new Amenity();
                        newAmenity.setName(normalizedName);
                        return amenityRepository.save(newAmenity);
                    });

            hotel.getAmenities().add(amenity);
        }

        return hotelRepository.save(hotel);
    }

    // Добавление удобств с возвратом краткого DTO
    @Transactional
    public HotelSummaryDto addAmenitiesDto(
            Long id,
            List<String> amenities
    ) {
        Hotel hotel = addAmenities(id, amenities);
        return hotelMapper.toSummaryDto(hotel);
    }

    // Гистограмма по параметру
    @Transactional
    public Map<String, Long> getHistogram(String param) {
        if (param == null || param.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Histogram parameter must not be blank"
            );
        }

        List<Hotel> hotels = hotelRepository.findAll();
        Map<String, Long> histogram = new LinkedHashMap<>();

        switch (param.trim().toLowerCase(Locale.ROOT)) {
            case "brand" -> hotels.stream()
                    .map(Hotel::getBrand)
                    .filter(this::hasText)
                    .map(String::trim)
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            LinkedHashMap::new,
                            Collectors.counting()
                    ))
                    .forEach(histogram::put);

            case "city" -> hotels.stream()
                    .filter(h -> h.getAddress() != null)
                    .map(h -> h.getAddress().getCity())
                    .filter(this::hasText)
                    .map(String::trim)
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            LinkedHashMap::new,
                            Collectors.counting()
                    ))
                    .forEach(histogram::put);

            case "country" -> hotels.stream()
                    .filter(h -> h.getAddress() != null)
                    .map(h -> h.getAddress().getCountry())
                    .filter(this::hasText)
                    .map(String::trim)
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            LinkedHashMap::new,
                            Collectors.counting()
                    ))
                    .forEach(histogram::put);

            case "amenities" -> hotels.forEach(hotel -> {
                if (hotel.getAmenities() != null) {
                    Set<String> uniqueAmenities = new TreeSet<>(
                            String.CASE_INSENSITIVE_ORDER
                    );

                    hotel.getAmenities().stream()
                            .filter(Objects::nonNull)
                            .map(Amenity::getName)
                            .filter(this::hasText)
                            .map(String::trim)
                            .forEach(uniqueAmenities::add);

                    uniqueAmenities.forEach(name ->
                            histogram.merge(name, 1L, Long::sum)
                    );
                }
            });

            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported histogram parameter: " + param
            );
        }

        return histogram;
    }
}
