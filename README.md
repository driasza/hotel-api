# Hotel API

RESTful API для управления отелями.

## Технологии

- Java 21
- Spring Boot
- Spring Data JPA
- Maven
- H2
- Liquibase

## Запуск приложения

Требуется установленная Java 21.

Запуск через Maven:

```bash
mvn spring-boot:run
```

Приложение запускается на порту `8092`.

## API

Общий префикс: `/property-view`

- `GET /property-view/hotels` — список отелей
- `GET /property-view/hotels/{id}` — подробная информация об отеле
- `GET /property-view/search` — поиск отелей
- `POST /property-view/hotels` — создание отеля
- `POST /property-view/hotels/{id}/amenities` — добавление удобств
- `GET /property-view/histogram/{param}` — статистика по параметру

Дополнительная документация API доступна через Swagger.
