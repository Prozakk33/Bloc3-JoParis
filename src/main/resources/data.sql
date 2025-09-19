INSERT INTO
    user (
        id,
        first_name,
        last_name,
        email,
        password,
        user_key,
        role
    )
VALUES (
        1,
        'John',
        'Doe',
        'john.doe@example.com',
        'password123',
        'key123',
        'USER'
    );

INSERT INTO
    user (
        id,
        first_name,
        last_name,
        email,
        password,
        user_key,
        role
    )
VALUES (
        2,
        'Jane',
        'Smith',
        'jane.smith@example.com',
        'password456',
        'key456',
        'ADMIN'
    );

INSERT INTO
    user (
        id,
        first_name,
        last_name,
        email,
        password,
        user_key,
        role
    )
VALUES (
        3,
        'Léon',
        'Marchand',
        'leon@marchand.com',
        '$2a$10$eBpAzEpykU8KO0wRm7lKZuAhVUjNfggmlqidzY1llFT5XjieSelcC',
        'key789',
        'USER'
    );

INSERT INTO
    event (
        id,
        title,
        description,
        city,
        stadium,
        date,
        capacity,
        sport,
        price
    )
VALUES (
        1,
        'France - Espagne',
        'Exciting football match',
        'PARIS',
        'Stadium A',
        '2025-09-15T15:00:00',
        50000,
        'FOOTBALL',
        50.0
    );

INSERT INTO
    event (
        id,
        title,
        description,
        city,
        stadium,
        date,
        capacity,
        sport,
        price
    )
VALUES (
        2,
        'Germany - Italy',
        'Thrilling football match',
        'BORDEAUX',
        'Stadium B',
        '2025-09-16T20:00:00',
        60000,
        'FOOTBALL',
        60.0
    );

INSERT INTO
    event (
        id,
        title,
        description,
        city,
        stadium,
        date,
        capacity,
        sport,
        price
    )
VALUES (
        3,
        'France - IRLANDE',
        'Exciting Rugby match',
        'PARIS',
        'Stadium B',
        '2025-09-15T21:00:00',
        50000,
        'RUGBY',
        50.0
    );

INSERT INTO
    event (
        id,
        title,
        description,
        city,
        stadium,
        date,
        capacity,
        sport,
        price
    )
VALUES (
        4,
        'Eliminatoires 100m',
        '1ere phase des sélections du 100m',
        'PARIS',
        'Stade de France',
        '2025-09-15T10:00:00',
        50000,
        'ATHLETISME',
        50.0
    );

INSERT INTO
    ticket (
        id,
        buy_date,
        ticket_key,
        user_id,
        event_id
    )
VALUES (
        1,
        '2025-09-10',
        'ticket123',
        1,
        1
    );