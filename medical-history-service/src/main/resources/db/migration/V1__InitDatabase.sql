CREATE TABLE prescription (
    id uuid NOT NULL PRIMARY KEY,
    medical_record_id uuid NOT NULL, --Rel
    medication VARCHAR (40),
    start_date DATE,
    end_date DATE,
    dosage integer,
    total NUMERIC
);

CREATE TABLE medical_record (
    id uuid NOT NULL PRIMARY KEY,
    doctor_id TEXT NOT NULL, --Rel
    patient_id TEXT NOT NULL, --Rel
    appointment_id uuid, --Rel
    check_in_date DATE,
    notes TEXT,
    disease VARCHAR(40),
    status VARCHAR(15),
    room_no VARCHAR(6)
);


ALTER TABLE prescription ADD CONSTRAINT fk_medical FOREIGN KEY (medical_record_id) REFERENCES medical_record(id)
ON UPDATE CASCADE;
