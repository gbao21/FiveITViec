create database FiveITViec;
use FiveITViec;

-- Tạo bảng Role nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS `Roles` (
    `role_id` INT AUTO_INCREMENT PRIMARY KEY,
    `role_name` VARCHAR(50)
);

-- Tạo bảng User nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS `User` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(100),
    `password` VARCHAR(255),
    `role_id` INT,
    `status` varchar(10),
    `approval` varchar(10),
   `created_at` TIMESTAMP NOT NULL,
   `created_by` VARCHAR(50) NOT NULL,
   `updated_at` TIMESTAMP DEFAULT NULL,
   `updated_by` VARCHAR(50) DEFAULT NULL,
    FOREIGN KEY (`role_id`) REFERENCES Roles(`role_id`)
);
-- Tạo bảng JobCategory nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS `JobCategory` (
    `category_id` INT AUTO_INCREMENT PRIMARY KEY,
    `category_name` VARCHAR(50),
    `category_img` TEXT,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` VARCHAR(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` VARCHAR(50) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `Job` (
    `job_id` INT  AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(100),
    `category_id` INT,
    `user_id` INT,
    `job_img` TEXT,
    `description` LONGTEXT,
    `requirements` LONGTEXT,
    `salary` DECIMAL(10,2),
    `location` VARCHAR(100),
    `created_at` TIMESTAMP NOT NULL,
    `created_by` VARCHAR(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` VARCHAR(50) DEFAULT NULL,
    `application_deadline` DATE,
    `status` VARCHAR(20),
    `approval` VARCHAR(20),
     FOREIGN KEY (`category_id`) REFERENCES `JobCategory`(`category_id`),
     FOREIGN KEY (`user_id`) REFERENCES User(`user_id`)
);

CREATE TABLE `Review_Rating` (
    `review_id` INT AUTO_INCREMENT PRIMARY KEY,
    `job_id` INT,
    `user_id` INT,
    `rating` INT,
    `review_text` LONGTEXT,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` VARCHAR(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` VARCHAR(50) DEFAULT NULL,
    FOREIGN KEY (`job_id`) REFERENCES Job(`job_id`),
    FOREIGN KEY (`user_id`) REFERENCES User(`user_id`)
);





CREATE TABLE `contact` (
    `contact_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `subject` VARCHAR(255) NOT NULL,
    `message` TEXT NOT NULL,
    `status` VARCHAR(255),
    `created_at` TIMESTAMP NOT NULL,
    `created_by` VARCHAR(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` VARCHAR(50) DEFAULT NULL
);

    CREATE TABLE IF NOT EXISTS `Blog`(
    `blog_id`int AUTO_INCREMENT primary key,
    `blog_title` varchar(150),
    `blog_content` varchar(2000),
    `blog_img` varchar(50),
    `author` varchar(50),
    `status` VARCHAR(255),
    `created_at` TIMESTAMP NOT NULL,
    `created_by` VARCHAR(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` VARCHAR(50) DEFAULT NULL
    );


-- Create the Specialization table first
CREATE TABLE IF NOT EXISTS `Specialization` (
    `specialization_id` INT AUTO_INCREMENT PRIMARY KEY,
    `specialization_name` VARCHAR(100),
    `created_at` TIMESTAMP NOT NULL,
    `created_by` VARCHAR(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` VARCHAR(50) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `user_specialization` (
  `user_id` int NOT NULL,
  `specialization_id` int NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`),
  FOREIGN KEY (`specialization_id`) REFERENCES `specialization`(`specialization_id`),
   PRIMARY KEY (`user_id`,`specialization_id`)
);

CREATE TABLE IF NOT EXISTS `Profile_Candidate` (
    `profile_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT,
    `name` VARCHAR(100),
    `user_image` TEXT,
    `gender` VARCHAR(20),
    `phone_number` VARCHAR(20),
    `address` VARCHAR(255),
    `bio` TEXT,
    FOREIGN KEY (`user_id`) REFERENCES User(`user_id`)
);



-- Tạo bảng Profile_Employer nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS `Profile_Employer`(
    `profile_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT,
    `name` VARCHAR(100),
    `company_name` VARCHAR(100),
    `company_logo` TEXT,
    `phone_number` VARCHAR(11),
    `address` varchar(100),
    `bio` TEXT,
    `tax_number` VARCHAR(20),
    FOREIGN KEY (`user_id`) REFERENCES User(`user_id`)
);



-- tao bang blog comment
CREATE TABLE `Blog_Comment` (
    `comment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `blog_id` INT,
    `user_id` INT,
    `rating` INT,
    `comment_text` LONGTEXT,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` VARCHAR(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` VARCHAR(50) DEFAULT NULL,
    FOREIGN KEY (`blog_id`) REFERENCES `Blog`(`blog_id`),
    FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`)
);

CREATE TABLE IF NOT EXISTS `Applicant` (
    `applicant_id` INT AUTO_INCREMENT PRIMARY KEY,
    `job_id` INT,
    `user_id` INT,
	`full_name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(20) NOT NULL,
    `cv` TEXT,
    `coverletter` LONGTEXT,
    `status` VARCHAR(255),
    `created_at` TIMESTAMP NOT NULL,
    `created_by` VARCHAR(50) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` VARCHAR(50) DEFAULT NULL,
    FOREIGN KEY (job_id) REFERENCES Job(job_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);


--Trigger xoa specialization
DELIMITER $$
CREATE TRIGGER DeleteUserSpecializations
BEFORE DELETE ON Specialization
FOR EACH ROW
BEGIN
    DELETE FROM user_specialization
    WHERE specialization_id = OLD.specialization_id;
END;
$$
DELIMITER ;



