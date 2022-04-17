USE `bloodbank`;

-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)

-- data for table `address`
INSERT INTO `address` (`address_id`, `street_number`, `street`, `city`, `province`, `country`, `zipcode`, `created`, `updated`) 
  VALUES (1,'123','Abcd Dr.W','Ottawa','ON','CA','A1B2C3',now(), now());

-- data for table `person`
INSERT INTO `person` (`id`, `first_name`, `last_name`,  `created`, `updated`)
  VALUES (1,'Teddy','Yap',now(),now());

-- data for table `phone`
INSERT INTO `phone` (`phone_id`, `country_code`, `area_code`, `number`, `created`, `updated`)
  VALUES (1,'0','234','5678900',now(),now()),(2,'1','432','0098765',now(), now());

-- data for table `blood_bank`
INSERT INTO `blood_bank` (`bank_id`, `name`, `privately_owned`,`created`, `updated`)
  VALUES (1,'Bloody Bank',1,now(),now()),(2,'Cheap Bloody Bank',0,now(),now());

-- data for table `blood_donation`
INSERT INTO `blood_donation` (`donation_id`, `bank_id`, `milliliters`, `blood_group`, `rhd`, `created`, `updated`)
  VALUES (1,2,10,'B',0,now(),now()),(2,2,10,'A',0,now(),now());

-- data for table `contact`
INSERT INTO `contact` (`person_id`, `phone_id`, `contact_type`, `email`, `address_id`, `created`, `updated`)
  VALUES (1,1,'Home','test@test.com',1,now(),now()),(1,2,'Work',NULL,NULL,now(),now());

-- data for table `donation_record`
INSERT INTO `donation_record` (`record_id`, `person_id`, `donation_id`, `tested`, `created`, `updated`)
  VALUES (1,1,1,1,now(),now()),(2,1,2,0,now(),now());

-- data for table `security_role`
INSERT INTO `security_role` (`role_id`, `name`)
  VALUES (1,'ADMIN_ROLE'), (2,'USER_ROLE');

-- data for table `security_user`
-- value for `password_hash` column computed by PBKDF2HashGenerator
--   user 'admin', password 'admin'
--   user 'cst8277', password '8277'
INSERT INTO `security_user` (`user_id`, `password_hash`, `username`, `person_id`)
  VALUES (1, 'PBKDF2WithHmacSHA256:2048:hYKwYbuwalL2mbXT3Lx8QgJuTWT8GgZcGljMPEW+TZA=:6GmiBW47QsKVgqF7wzt/wjQAMDd0RVMok3M8WPu8Y1U=', 'admin', null), (2, 'PBKDF2WithHmacSHA256:2048:ZJC4ipE7LQOZzOQyd2ch7VOxHJWwrVfDFTbo9H+U5Fw=:j5Wulo/tVmolv8hqu0k5ejTOPEMbzviQXStg/0/c6Qo=', 'cst8277', 1);

--  data for table `user_has_role`
INSERT INTO `user_has_role` (`user_id`, `role_id`)
  VALUES (1,1), (2,2);
