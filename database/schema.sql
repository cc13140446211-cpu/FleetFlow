/* ============================================================
   FleetFlow V1 Database Schema
   Database: MySQL 8.4
   ============================================================ */

/* ============================================================
   CUSTOMER
   ============================================================ */

CREATE TABLE customer (
    cust_id BIGINT AUTO_INCREMENT PRIMARY KEY
        COMMENT 'Customer number',

    cust_name VARCHAR(100) NOT NULL
        COMMENT 'Customer name',

    cust_company_name VARCHAR(100)
        COMMENT 'Customer company name, if applicable',

    cust_phone VARCHAR(20) NOT NULL
        COMMENT 'Customer contact number',

    cust_email VARCHAR(100)
        COMMENT 'Customer email address',

    cust_address VARCHAR(255) NOT NULL
        COMMENT 'Customer address',

    cust_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT 'Date and time the customer record was created'
)
COMMENT = 'Stores customer information for freight enquiries and bookings';


/* ============================================================
   EMPLOYEE
   ============================================================ */

CREATE TABLE employee (
    emp_id BIGINT AUTO_INCREMENT PRIMARY KEY
        COMMENT 'Employee number',

    emp_name VARCHAR(100) NOT NULL
        COMMENT 'Employee name',

    emp_phone VARCHAR(20) NOT NULL
        COMMENT 'Employee contact number',

    emp_role VARCHAR(20) NOT NULL
        COMMENT 'Employee role: DISPATCHER or DRIVER',

    emp_license_number VARCHAR(50)
        COMMENT 'Driving licence number, only recorded for drivers',

    emp_license_expiry_date DATE
        COMMENT 'Driving licence expiry date, only recorded for drivers',

    emp_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
        COMMENT 'Employee status: ACTIVE or INACTIVE',

    emp_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT 'Date and time the employee record was created',

    CONSTRAINT uq_emp_license
        UNIQUE (emp_license_number),

    CONSTRAINT chk_emp_role
        CHECK (
            emp_role IN (
                'DISPATCHER',
                'DRIVER'
            )
        ),

    CONSTRAINT chk_emp_status
        CHECK (
            emp_status IN (
                'ACTIVE',
                'INACTIVE'
            )
        )
)
COMMENT = 'Stores dispatcher and driver information';


/* ============================================================
   TRUCK
   ============================================================ */

CREATE TABLE truck (
    truck_id BIGINT AUTO_INCREMENT PRIMARY KEY
        COMMENT 'Truck number',

    truck_vin VARCHAR(17) NOT NULL
        COMMENT 'Vehicle identification number of the truck',

    truck_registration_number VARCHAR(20) NOT NULL
        COMMENT 'Registration number of the truck',

    truck_model VARCHAR(100) NOT NULL
        COMMENT 'Truck model',

    truck_capacity_kg DECIMAL(10,2) NOT NULL
        COMMENT 'Maximum carrying capacity of the truck in kilograms',

    truck_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
        COMMENT 'Truck status: ACTIVE, MAINTENANCE, or INACTIVE',

    truck_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT 'Date and time the truck record was created',

    CONSTRAINT uq_truck_vin
        UNIQUE (truck_vin),

    CONSTRAINT uq_truck_registration
        UNIQUE (truck_registration_number),

    CONSTRAINT chk_truck_capacity
        CHECK (
            truck_capacity_kg > 0
        ),

    CONSTRAINT chk_truck_status
        CHECK (
            truck_status IN (
                'ACTIVE',
                'MAINTENANCE',
                'INACTIVE'
            )
        )
)
COMMENT = 'Stores truck and fleet information';


/* ============================================================
   QUOTE
   ============================================================ */

CREATE TABLE quote (
    quote_id BIGINT AUTO_INCREMENT PRIMARY KEY
        COMMENT 'Quote number',

    cust_id BIGINT NOT NULL
        COMMENT 'Customer number associated with the quote',

    prepared_by_emp_id BIGINT NOT NULL
        COMMENT 'Employee number of the dispatcher who prepared the quote',

    quote_pickup_location VARCHAR(255) NOT NULL
        COMMENT 'Pickup location specified in the quote',

    quote_dropoff_location VARCHAR(255) NOT NULL
        COMMENT 'Drop-off location specified in the quote',

    quote_preferred_pickup_date DATE NOT NULL
        COMMENT 'Preferred pickup date requested by the customer',

    quote_price DECIMAL(10,2) NOT NULL
        COMMENT 'Price quoted to the customer',

    quote_status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        COMMENT 'Quote status: PENDING, ACCEPTED, REJECTED, or CONVERTED',

    quote_payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID'
        COMMENT 'Payment status: UNPAID or PAID',

    quote_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT 'Date and time the quote was created',

    quote_updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
        COMMENT 'Date and time the quote was last updated',

    CONSTRAINT fk_quote_customer
        FOREIGN KEY (cust_id)
        REFERENCES customer(cust_id),

    CONSTRAINT fk_quote_prepared_by
        FOREIGN KEY (prepared_by_emp_id)
        REFERENCES employee(emp_id),

    CONSTRAINT chk_quote_price
        CHECK (
            quote_price > 0
        ),

    CONSTRAINT chk_quote_status
        CHECK (
            quote_status IN (
                'PENDING',
                'ACCEPTED',
                'REJECTED',
                'CANCELLED',
                'CONVERTED'
            )
        ),

    CONSTRAINT chk_quote_payment_status
        CHECK (
            quote_payment_status IN (
                'UNPAID',
                'PAID'
            )
        )
)
COMMENT = 'Stores freight quotations prepared for customers';


/* ============================================================
   JOB
   ============================================================ */

CREATE TABLE job (
    job_id BIGINT AUTO_INCREMENT PRIMARY KEY
        COMMENT 'Job number',

    quote_id BIGINT NOT NULL
        COMMENT 'Quote number from which the job was created',

    driver_emp_id BIGINT NOT NULL
        COMMENT 'Employee number of the driver assigned to the job',

    scheduled_by_emp_id BIGINT NOT NULL
        COMMENT 'Employee number of the dispatcher who scheduled the job',

    truck_id BIGINT NOT NULL
        COMMENT 'Truck number assigned to the job',

    job_pickup_datetime DATETIME NOT NULL
        COMMENT 'Scheduled pickup date and time',

    job_expected_dropoff_datetime DATETIME NOT NULL
        COMMENT 'Expected drop-off date and time',

    job_final_price DECIMAL(10,2) NOT NULL
        COMMENT 'Final price charged for the freight job',

    job_status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED'
        COMMENT 'Job status: SCHEDULED, IN_PROGRESS, COMPLETED, or CANCELLED',

    job_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT 'Date and time the job record was created',

    job_updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
        COMMENT 'Date and time the job record was last updated',

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
        CHECK (
            job_final_price > 0
        ),

    CONSTRAINT chk_job_status
        CHECK (
            job_status IN (
                'SCHEDULED',
                'IN_PROGRESS',
                'COMPLETED',
                'CANCELLED'
            )
        )
)
COMMENT = 'Stores scheduled freight jobs and assigned transport resources';


/* ============================================================
   INDEXES
   ============================================================ */

/* Speeds up retrieval of quotes belonging to a customer */
CREATE INDEX idx_quote_customer
    ON quote(cust_id);


/* Supports driver scheduling and conflict checks */
CREATE INDEX idx_job_driver_schedule
    ON job(
        driver_emp_id,
        job_pickup_datetime,
        job_expected_dropoff_datetime
    );


/* Supports truck scheduling and conflict checks */
CREATE INDEX idx_job_truck_schedule
    ON job(
        truck_id,
        job_pickup_datetime,
        job_expected_dropoff_datetime
    );


/* Supports filtering jobs by operational status */
CREATE INDEX idx_job_status
    ON job(job_status);