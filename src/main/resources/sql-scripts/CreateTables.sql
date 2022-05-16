CREATE TABLE product (
    prod_id BIGSERIAL PRIMARY KEY,
    prod_name VARCHAR(255) NOT NULL,
    rating FLOAT NOT NULL,
    sales_rank INT NOT NULL,
    image VARCHAR(255)
);

CREATE TABLE person (
    person_id BIGSERIAL PRIMARY KEY,
    person_name VARCHAR(50)
);

CREATE TABLE book (
    book_id BIGINT PRIMARY KEY REFERENCES product (prod_id) ON DELETE CASCADE,                -- on DELETE CASCADE??
    isbn VARCHAR(50) UNIQUE NOT NULL,
    publisher VARCHAR(255) NOT NULL,                                        -- id and own table??
    release_date DATE NOT NULL,
    pages INT NOT NULL
    
);

CREATE TABLE author (
    book_id BIGINT REFERENCES book (book_id) ON DELETE CASCADE,
    person_id BIGINT REFERENCES person (person_id) ON DELETE RESTRICT,
    PRIMARY KEY (book_id, person_id)
);

CREATE TABLE dvd (
    dvd_id BIGINT PRIMARY KEY REFERENCES product (prod_id) ON DELETE CASCADE,                 -- on DELETE CASCADE
    movie_id INT UNIQUE NOT NULL,
    format VARCHAR(50) NOT NULL,
    term_in_sec INT NOT NULL,
    region_code INT NOT NULL
);

CREATE TABLE dvd_person (
    dvd_id BIGINT REFERENCES dvd (dvd_id) ON DELETE CASCADE,
    person_id BIGINT REFERENCES person (person_id) ON DELETE RESTRICT,
    p_role VARCHAR(50),
    PRIMARY KEY (dvd_id, person_id, p_role),
    CONSTRAINT check_role CHECK (p_role::text = 'Director'::text OR p_role::text = 'Actor'::text OR p_role = 'Creator'::text) 
);

CREATE TABLE cd (
    cd_id BIGINT PRIMARY KEY REFERENCES product (prod_id) ON DELETE CASCADE,                  -- on DELETE CASCADE?
    label VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL
);

CREATE TABLE artist (
    artist_id BIGSERIAL PRIMARY KEY,
    artist_name VARCHAR(255) NOT NULL
);

CREATE TABLE title (
    title_id BIGSERIAL PRIMARY KEY,
    artist_id BIGINT NOT NULL REFERENCES artist (artist_id) ON DELETE CASCADE,
    title_name VARCHAR(255) NOT NULL
);
    
CREATE TABLE cd_title (
    cd_id BIGINT REFERENCES cd (cd_id) ON DELETE CASCADE,
    title_id BIGINT REFERENCES title (title_id) ON DELETE RESTRICT,
    PRIMARY KEY (cd_id, title_id)
);

CREATE TABLE category (                                                                     -- category key word???
    category_id BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    super_category BIGINT REFERENCES category ON DELETE RESTRICT
);

CREATE TABLE product_category (
    prod_id BIGINT REFERENCES product (prod_id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES category (category_id) ON DELETE RESTRICT,d
    PRIMARY KEY (prod_id, category_id)
);

CREATE TABLE address (
    address_id BIGSERIAL PRIMARY KEY,
    street_name VARCHAR(50) NOT NULL,
    street_number INT NOT NULL,
    city VARCHAR(50) NOT NULL,
    postcode VARCHAR(50) NOT NULL,
    country VARCHAR(50)
);

CREATE TABLE store (                                                                        -- store key word??
    store_id BIGSERIAL PRIMARY KEY,
    address_id BIGINT NOT NULL REFERENCES address (address_id) ON DELETE RESTRICT,
    store_name VARCHAR(255) NOT NULL
);

CREATE TABLE inventory (
    store_id BIGINT REFERENCES store (store_id) ON DELETE CASCADE,
    prod_id BIGINT REFERENCES product (prod_id) ON DELETE CASCADE,
    prize NUMERIC(32,2) NOT NULL,
    condition VARCHAR(50),
    PRIMARY KEY (store_id, prod_id)
);

CREATE TABLE customer (
    customer_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    address_id BIGINT NOT NULL REFERENCES address (address_id) ON DELETE RESTRICT,
    bankaccount VARCHAR(50) NOT NULL
);

CREATE TABLE "order" (                                                                        -- order key word??
    order_id BIGSERIAL PRIMARY KEY,
    store_id BIGINT NOT NULL REFERENCES store (store_id) ON DELETE RESTRICT,
    customer_id BIGINT NOT NULL REFERENCES customer (customer_id) ON DELETE RESTRICT
);

CREATE TABLE review (
    prod_id BIGINT REFERENCES product (prod_id) ON DELETE CASCADE,
    username VARCHAR(50) NOT NULL,
    reviewdate DATE NOT NULL,
    rating INT NOT NULL,
    helpful_rating INT NOT NULL,
    review_sum VARCHAR(255),
    review_text VARCHAR(5000),
    PRIMARY KEY (prod_id, username)
);
