CREATE TABLE appointment (
    id UUID NOT NULL PRIMARY KEY,
    doctor_id TEXT NOT NULL, --Rel
    patient_id TEXT NOT NULL, --Rel
    appointment_date DATE,
    start_time TIME,
    end_time TIME,
    reason TEXT,
    medical_record_id UUID
);