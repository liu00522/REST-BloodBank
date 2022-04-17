-- this might be useful
USE `bloodbank`;
ALTER TABLE person AUTO_INCREMENT = 1;
ALTER TABLE blood_bank AUTO_INCREMENT = 1;
ALTER TABLE blood_donation AUTO_INCREMENT = 1;
ALTER TABLE donation_record AUTO_INCREMENT = 1;
ALTER TABLE address AUTO_INCREMENT = 1;
ALTER TABLE phone AUTO_INCREMENT = 1;
-- Note: no auto_increment on contact table as it has a composite primary key