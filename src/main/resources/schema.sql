CREATE TABLE IF NOT EXISTS employees_details (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    employment_date datetime,
    phone_number VARCHAR(10),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS caretakers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    responsibility VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS caretakers_employees_details (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employees_details BIGINT NOT NULL,
    caretakers BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (caretakers) REFERENCES caretakers(id),
    FOREIGN KEY (employees_details) REFERENCES employees_details(id)
);

CREATE TABLE IF NOT EXISTS vets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    experience INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS vets_employees_details (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employees_details BIGINT NOT NULL,
    vets BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (vets) REFERENCES vets(id),
    FOREIGN KEY (employees_details) REFERENCES employees_details(id)
);


CREATE TABLE IF NOT EXISTS cages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    number_places NUMBER NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cages_caretakers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cages BIGINT NOT NULL,
    caretakers BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cages) REFERENCES cages(id),
    FOREIGN KEY (caretakers) REFERENCES caretakers(id)
);

CREATE TABLE IF NOT EXISTS animals (
    id BIGINT NOT NULL AUTO_INCREMENT,
    animal_type VARCHAR(50),
    birth_year INT,
    found_date datetime NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS employees_details (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_becoming_client datetime,
    birth_date datetime,
    phone_number VARCHAR(10),
    gender CHAR,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS animals_cages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cages BIGINT NOT NULL,
    animals BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cages) REFERENCES cages(id),
    FOREIGN KEY (animals) REFERENCES animals(id)
);

CREATE TABLE IF NOT EXISTS animals_clients (
    id BIGINT NOT NULL AUTO_INCREMENT,
    clients BIGINT NOT NULL,
    animals BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (clients) REFERENCES clients(id),
    FOREIGN KEY (animals) REFERENCES animals(id)
);

CREATE TABLE IF NOT EXISTS diets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    diet_type VARCHAR(50) NOT NULL,
    quantity_on_stock INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS animals_diets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    diets BIGINT NOT NULL,
    animals BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (diets) REFERENCES diets(id),
    FOREIGN KEY (animals) REFERENCES animals(id)
);

CREATE TABLE IF NOT EXISTS vaccines (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vaccine_name VARCHAR(50) NOT NULL,
    expiration_date datetime NOT NULL,
    quantity_on_stock INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS medical_records (
    id BIGINT NOT NULL AUTO_INCREMENT,
    general_health_state VARCHAR(100) NOT NULL,
    generation_date datetime,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS animals_medical_records (
    id BIGINT NOT NULL AUTO_INCREMENT,
    medical_records BIGINT NOT NULL,
    animals BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_records) REFERENCES medical_records(id),
    FOREIGN KEY (animals) REFERENCES animals(id)
);

CREATE TABLE IF NOT EXISTS vaccines_medical_records (
    id BIGINT NOT NULL AUTO_INCREMENT,
    medical_records BIGINT NOT NULL,
    vaccines BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_records) REFERENCES medical_records(id),
    FOREIGN KEY (vaccines) REFERENCES vaccines(id)
);

CREATE TABLE IF NOT EXISTS vets_medical_records (
    id BIGINT NOT NULL AUTO_INCREMENT,
    medical_records BIGINT NOT NULL,
    vets BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_records) REFERENCES medical_records(id),
    FOREIGN KEY (vets) REFERENCES vets(id)
);