CREATE TABLE doctor (
    id TEXT NOT NULL PRIMARY KEY,
    name VARCHAR (50),
    email VARCHAR(100),
    phone_number VARCHAR(10),
    address VARCHAR (100),
    specialization VARCHAR(30),
    age INTEGER,
    skills VARCHAR(30) ARRAY,
    biography TEXT,
    department VARCHAR(40)
);

CREATE TABLE patient (
    id TEXT NOT NULL PRIMARY KEY,
    name VARCHAR(50),
    address VARCHAR (100),
    phone_number VARCHAR(10),
    email VARCHAR (50),
    age INTEGER,
    blood_group VARCHAR(3),
    religion VARCHAR(20),
    occupation VARCHAR(30),
    gender CHAR(1),
    marital_status VARCHAR(10),
    description TEXT,
    doctor_id TEXT
);

CREATE TABLE availability (
    id uuid NOT NULL PRIMARY KEY,
    doctor_id TEXT NOT NULL, --Rel
    day_of_week VARCHAR(10),
    start_time TIME,
    end_time TIME
);


ALTER TABLE patient ADD CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id);

ALTER TABLE availability ADD CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE;