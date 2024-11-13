CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email varchar(40) NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    latitude decimal NOT NULL,
    longitude decimal NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(50) NOT NULL,
    pinned boolean NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title varchar(120) not null,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT references categories (id) not null,
    description text NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id bigint references users (id) not null,
    location_id bigint references locations (id) not null,
    paid boolean not null,
    participant_limit bigint,
    request_moderation boolean,
    state varchar(20),
    CONSTRAINT pk_events PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS event_views (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id bigint references users (id) NOT NULL,
    event_id bigint references events (id) NOT NULL,
    view_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_event_views PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event_requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id bigint references users (id) NOT NULL,
    event_id bigint references events (id) NOT NULL,
    request_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status varchar(15),
    CONSTRAINT pk_event_requests PRIMARY KEY (id)
);

create table if not exists compilation_events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    compilation_id BIGINT references compilations (id),
    event_id BIGINT references events (id),
    CONSTRAINT pk_compilation_events PRIMARY KEY (id)
);






















