CREATE TABLE IF NOT EXISTS persons_details (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(10),
    UNIQUE(first_name, last_name),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS employees (
    id BIGINT NOT NULL AUTO_INCREMENT,
    responsibility VARCHAR(100),
    experience INT,
    employment_date datetime,
    salary INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS employees_persons_details (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employees BIGINT NOT NULL,
    persons_details BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (employees) REFERENCES employees(id),
    FOREIGN KEY (persons_details) REFERENCES persons_details(id)
);

CREATE TABLE IF NOT EXISTS cages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    number_places NUMBER NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cages_employees (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cages BIGINT NOT NULL,
    employees BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cages) REFERENCES cages(id),
    FOREIGN KEY (employees) REFERENCES employees(id)
);

CREATE TABLE IF NOT EXISTS animals (
    id BIGINT NOT NULL AUTO_INCREMENT,
    animal_type VARCHAR(50),
    birth_year INT,
    found_date datetime NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS clients (
    id BIGINT NOT NULL AUTO_INCREMENT,
    date_becoming_client datetime,
    birth_date datetime,
    gender CHAR,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS clients_persons_details (
    id BIGINT NOT NULL AUTO_INCREMENT,
    clients BIGINT NOT NULL,
    persons_details BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (clients) REFERENCES clients(id),
    FOREIGN KEY (persons_details) REFERENCES persons_details(id)
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
    diet_type VARCHAR(50) NOT NULL UNIQUE,
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
    vaccine_name VARCHAR(50) NOT NULL UNIQUE,
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

CREATE TABLE IF NOT EXISTS employees_medical_records (
    id BIGINT NOT NULL AUTO_INCREMENT,
    medical_records BIGINT NOT NULL,
    employees BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (medical_records) REFERENCES medical_records(id),
    FOREIGN KEY (employees) REFERENCES employees(id)
);