CREATE TABLE expense (
    id uuid NOT NULL PRIMARY KEY,
    patient_id TEXT, --Rel
    name VARCHAR(30),
    category VARCHAR(30),
    description TEXT,
    amount NUMERIC,
    date_of_expense DATE,
    paid BOOLEAN
);