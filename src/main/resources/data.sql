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
        'Admin',
        'Admin',
        'admin@admin.com',
        '$2a$10$QdaJCZEz5JZmXLKea/ZZ4u.m0jcXQ/x8ILM1lNSRsbQWiC9K0GWA.',
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnamh1QGhvdG1haWwuY29tIiwiaWF0IjoxNzU4NTMzNzk4fQ.YD7HJ8el6Jx9f4rRXe5SsOMgIsIEbi6njzgsAAb0LY4',
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
        4,
        'Philippe',
        'CHABERT',
        'ooodieu@hotmail.com',
        '$2a$10$hN8bpMMHRkgoeuIbpRYS8.S2DXY711PPrMQdLxfyReKOiZy0DeIWS',
        'key789',
        'ADMIN'
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
        5,
        'Eliminatoires 100m',
        'Seconde phase des sélections du 100m',
        'PARIS',
        'Stade de France',
        '2025-09-15T12:00:00',
        50000,
        'ATHLETISME',
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
        6,
        'Eliminatoires 200m',
        '1ere phase des sélections du 200m',
        'PARIS',
        'Stade de France',
        '2025-09-15T14:00:00',
        50000,
        'ATHLETISME',
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
        7,
        'Eliminatoires 200m',
        'Seconde phase des sélections du 200m',
        'PARIS',
        'Stade de France',
        '2025-09-15T16:00:00',
        50000,
        'ATHLETISME',
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
        8,
        'Concours Complet',
        '1ere phase des sélections du Concours Complet',
        'LILLE',
        'Stade Pierre-Mauroy',
        '2025-09-18T10:00:00',
        50000,
        'CHEVAL',
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
        9,
        'Finale - France - Argentine',
        'C\'est l\'heure de la finale !',
        'PARIS',
        'Grand Palais',
        '2025-09-11T21:00:00',
        10000,
        'ESCRIME',
        100.0
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
        10,
        'Eliminatoire -80kg H.',
        '1er tour des éliminatoires -80kg Hommes',
        'LYON',
        'Stade de Lyon',
        '2025-10-15T11:30:00',
        10000,
        'JUDO',
        30.0
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
        11,
        'Eliminatoire -80kg F.',
        '1er tour des éliminatoires -80kg Femmes',
        'LYON',
        'Stade de Lyon',
        '2025-10-15T13:30:00',
        10000,
        'JUDO',
        30.0
    );

INSERT INTO
    ticket (
        id,
        buy_date,
        ticket_key,
        user_id,
        event_id,
        ticket_type
    )
VALUES (
        1,
        '2025-09-10',
        'ticket123',
        1,
        1,
        'Solo'
    );

INSERT INTO
    ticket (
        id,
        buy_date,
        ticket_key,
        user_id,
        event_id,
        ticket_type
    )
VALUES (
        2,
        '2025-09-30 15:53:47.557000',
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUaWNrZXQiLCJmaXJzdE5hbWUiOiJQaGlsaXBwZSIsImxhc3ROYW1lIjoiQ0hBQkVSVCIsImJ1eURhdGUiOjE3NTkyNDA0Mjc1NTcsImV2ZW50SWQiOjksInRpY2tldFR5cGUiOiJTb2xvIiwiaWF0IjoxNzU5MjQwNDI3fQ.9WPzYoV9dDZd9jTwsl0DEisSrxRP9dLTidE6TYLjeOQ',
        4,
        9,
        'Solo'
    );

INSERT INTO
    ticket (
        id,
        buy_date,
        ticket_key,
        user_id,
        event_id,
        ticket_type
    )
VALUES (
        3,
        '2025-09-30 15:53:47.758000',
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUaWNrZXQiLCJmaXJzdE5hbWUiOiJQaGlsaXBwZSIsImxhc3ROYW1lIjoiQ0hBQkVSVCIsImJ1eURhdGUiOjE3NTkyNDA0Mjc3NTgsImV2ZW50SWQiOjExLCJ0aWNrZXRUeXBlIjoiRmFtaWxsZSIsImlhdCI6MTc1OTI0MDQyN30.CC6NC9RFU9S_qVojqbDU0U-sfDPM2rwrsWhoBpXxAzw',
        4,
        3,
        'Famille'
    );

INSERT INTO
    ticket (
        id,
        buy_date,
        ticket_key,
        user_id,
        event_id,
        ticket_type
    )
VALUES (
        4,
        '2025-09-30 15:53:47.958000',
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUaWNrZXQiLCJmaXJzdE5hbWUiOiJQaGlsaXBwZSIsImxhc3ROYW1lIjoiQ0hBQkVSVCIsImJ1eURhdGUiOjE3NTkyNDA0Mjc5MTAsImV2ZW50SWQiOjExLCJ0aWNrZXRUeXBlIjoiU29sbyIsImlhdCI6MTc1OTI0MDQyN30.X6Augb5amfKMwnQiT_jO9XWDWGevOKsc-Sz2Z65fsdM',
        4,
        4,
        'Duo'
    );