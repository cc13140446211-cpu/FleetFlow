-- ============================================
-- FleetFlow V1 Database Schema
-- MySQL 8.4
-- ============================================


-- ============================================
-- CUSTOMER
-- ============================================

CREATE TABLE customer (
    cust_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cust_name VARCHAR(100) NOT NULL,
    cust_company_name VARCHAR(100),
    cust_phone VARCHAR(20) NOT NULL,
    cust_email VARCHAR(100),
    cust_address VARCHAR(255) NOT NULL,
    cust_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ============================================
-- EMPLOYEE
-- ============================================

CREATE TABLE employee (
    emp_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(100) NOT NULL,
    emp_phone VARCHAR(20) NOT NULL,
    emp_role VARCHAR(20) NOT NULL,
    emp_license_number VARCHAR(50),
    emp_license_expiry_date DATE,
    emp_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    emp_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_emp_role
        CHECK (emp_role IN ('DISPATCHER', 'DRIVER')),

    CONSTRAINT chk_emp_status
        CHECK (emp_status IN ('ACTIVE', 'INACTIVE')),

    CONSTRAINT uq_emp_license
        UNIQUE (emp_license_number)
);


-- ============================================
-- TRUCK
-- ============================================

CREATE TABLE truck (
    truck_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    truck_vin VARCHAR(17) NOT NULL,
    truck_registration_number VARCHAR(20) NOT NULL,
    truck_model VARCHAR(100) NOT NULL,
    truck_capacity_kg DECIMAL(10,2) NOT NULL,
    truck_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    truck_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_truck_vin
        UNIQUE (truck_vin),

    CONSTRAINT uq_truck_registration
        UNIQUE (truck_registration_number),

    CONSTRAINT chk_truck_capacity
        CHECK (truck_capacity_kg > 0),

    CONSTRAINT chk_truck_status
        CHECK (
            truck_status IN (
                'ACTIVE',
                'MAINTENANCE',
                'INACTIVE'
            )
        )
);


-- ============================================
-- QUOTE
-- ============================================

CREATE TABLE quote (
    quote_id BIGINT AUTO_INCREMENT PRIMARY KEY,

    cust_id BIGINT NOT NULL,
    prepared_by_emp_id BIGINT NOT NULL,

    quote_pickup_location VARCHAR(255) NOT NULL,
    quote_dropoff_location VARCHAR(255) NOT NULL,
    quote_preferred_pickup_date DATE NOT NULL,
    quote_price DECIMAL(10,2) NOT NULL,

    quote_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    quote_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quote_updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_quote_customer
        FOREIGN KEY (cust_id)
        REFERENCES customer(cust_id),

    CONSTRAINT fk_quote_prepared_by
        FOREIGN KEY (prepared_by_emp_id)
        REFERENCES employee(emp_id),

    CONSTRAINT chk_quote_price
        CHECK (quote_price > 0),

    CONSTRAINT chk_quote_status
        CHECK (
            quote_status IN (
                'PENDING',
                'ACCEPTED',
                'REJECTED',
                'CONVERTED'
            )
        )
);


-- ============================================
-- JOB
-- ============================================

CREATE TABLE job (
    job_id BIGINT AUTO_INCREMENT PRIMARY KEY,

    quote_id BIGINT NOT NULL,
    driver_emp_id BIGINT NOT NULL,
    scheduled_by_emp_id BIGINT NOT NULL,
    truck_id BIGINT NOT NULL,

    job_pickup_datetime DATETIME NOT NULL,
    job_expected_dropoff_datetime DATETIME NOT NULL,

    job_final_price DECIMAL(10,2) NOT NULL,

    job_status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    job_payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',

    job_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    job_updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_job_quote
        UNIQUE (quote_id),

    CONSTRAINT fk_job_quote
        FOREIGN KEY (quote_id)
        REFERENCES quote(quote_id),

    CONSTRAINT fk_job_driver
        FOREIGN KEY (driver_emp_id)
        REFERENCES employee(emp_id),

    CONSTRAINT fk_job_scheduler
        FOREIGN KEY (scheduled_by_emp_id)
        REFERENCES employee(emp_id),

    CONSTRAINT fk_job_truck
        FOREIGN KEY (truck_id)
        REFERENCES truck(truck_id),

    CONSTRAINT chk_job_time
        CHECK (
            job_expected_dropoff_datetime > job_pickup_datetime
        ),

    CONSTRAINT chk_job_price
        CHECK (job_final_price > 0),

    CONSTRAINT chk_job_status
        CHECK (
            job_status IN (
                'SCHEDULED',
                'IN_PROGRESS',
                'COMPLETED',
                'CANCELLED'
            )
        ),

    CONSTRAINT chk_job_payment_status
        CHECK (
            job_payment_status IN (
                'UNPAID',
                'PAID'
            )
        )
);


-- ============================================
-- INDEXES
-- ============================================

CREATE INDEX idx_quote_customer
    ON quote(cust_id);

CREATE INDEX idx_job_driver_schedule
    ON job(
        driver_emp_id,
        job_pickup_datetime,
        job_expected_dropoff_datetime
    );

CREATE INDEX idx_job_truck_schedule
    ON job(
        truck_id,
        job_pickup_datetime,
        job_expected_dropoff_datetime
    );

CREATE INDEX idx_job_status
    ON job(job_status);