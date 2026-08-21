INSERT INTO customer (
    cust_name,
    cust_company_name,
    cust_phone,
    cust_email,
    cust_address
)
VALUES
    (
        'ABC Logistics',
        'ABC Logistics Sdn Bhd',
        '0123456789',
        'contact@abc.com',
        'Kuala Lumpur'
    ),
    (
        'Nova Trading',
        'Nova Trading Sdn Bhd',
        '0198765432',
        'admin@nova.com',
        'Petaling Jaya'
    );

INSERT INTO employee (
    emp_name,
    emp_phone,
    emp_role,
    emp_status
)
VALUES
    (
        'Daniel Lim',
        '0122233445',
        'DISPATCHER',
        'ACTIVE'
    );

INSERT INTO employee (
    emp_name,
    emp_phone,
    emp_role,
    emp_license_number,
    emp_license_expiry_date,
    emp_status
)
VALUES
    (
        'Amir Hassan',
        '0175566778',
        'DRIVER',
        'D12345678',
        '2028-12-31',
        'ACTIVE'
    ),
    (
        'Jason Lee',
        '0188877665',
        'DRIVER',
        'D87654321',
        '2029-06-30',
        'ACTIVE'
    );

INSERT INTO truck (
    truck_vin,
    truck_registration_number,
    truck_model,
    truck_capacity_kg,
    truck_status
)
VALUES
    (
        '1HGBH41JXMN109186',
        'VAA1234',
        'Isuzu NPR',
        5000.00,
        'ACTIVE'
    );