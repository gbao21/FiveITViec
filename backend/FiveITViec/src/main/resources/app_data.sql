INSERT INTO roles (role_name) VALUES ('admin');
INSERT INTO roles (role_name) VALUES ('candidate');
INSERT INTO roles (role_name) VALUES ('employer');

-- Insert three users with different roles and status as ENABLE
INSERT INTO `User` (`email`, `password`, `role_id`, `status`,`approval`, `created_at`, `created_by`)
VALUES
    ('admin@gmail.com', '$2a$10$aWwDHNuEokHATx.68lO2EO/mZUv4CYcgyoqyPilffp.9MSx4BemDm', 1, 'ENABLE','APPROVED', NOW(), 'Admin'),
    ('employer@gmail.com', '$2a$10$aWwDHNuEokHATx.68lO2EO/mZUv4CYcgyoqyPilffp.9MSx4BemDm', 3, 'ENABLE','APPROVED', NOW(), 'Admin'),
    ('candidate@gmail.com', '$2a$10$aWwDHNuEokHATx.68lO2EO/mZUv4CYcgyoqyPilffp.9MSx4BemDm', 2, 'ENABLE','APPROVED', NOW(), 'Admin');

INSERT INTO Profile_Employer(user_id, name, user_img)
VALUES
    (3, 'Candidate', 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1696927237/bd120fa2-a147-4334-a93b-3c97de240af3.png');

INSERT INTO Profile_Candidate(user_id, name, company_logo)
VALUES
    (2, 'Employer', 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1696927237/bd120fa2-a147-4334-a93b-3c97de240af3.png');



-- Chèn dữ liệu vào bảng JobCategory
INSERT INTO JobCategory (category_name, `created_at`, `created_by`) VALUES
('Software Development', NOW(),"Admin"),
('Web Development', NOW(),"Admin"),
('Network Administration', NOW(),"Admin"),
('Database Administration', NOW(),"Admin"),
('IT Support', NOW(),"Admin");



INSERT INTO Job (`title`, `category_id`, `user_id`, `job_img`, `description`, `requirements`, `salary`, `location`, `created_at`, `created_by`, `application_deadline`, `status`)
VALUES
('Software Engineer', 1, 2, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444922/job6.svg', 'We are looking for a skilled software engineer to join our development team.', 'Bachelor''s degree in Computer Science, 3+ years of experience in software development, proficiency in programming languages (e.g., Java, Python, C++), strong problem-solving skills', 75000.00, 'San Francisco, CA', NOW(), 'Employer 1', '2023-10-15', 'ENABLE'),
('Network Administrator', 2, 2, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job7.png', 'We are seeking a network administrator to manage our IT infrastructure.', 'Bachelor''s degree in IT or related field, experience in network administration, knowledge of Cisco networking, troubleshooting skills', 65000.00, 'Los Angeles, CA', NOW(), 'Employer 2', '2023-10-16', 'ENABLE'),
('Data Scientist', 3, 3, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job5.jpg', 'We are hiring a data scientist to analyze and interpret data for insights.', 'Master''s degree in Data Science or related field, expertise in data analysis tools (e.g., Python, R), machine learning knowledge, strong statistical skills', 80000.00, 'New York, NY', NOW(), 'Employer 3', '2023-10-17', 'ENABLE'),
('IT Support Specialist', 4, 2, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job3.jpg', 'We have an opening for an IT support specialist to assist users with technical issues.', 'Associate degree in IT, IT support experience, knowledge of Windows and macOS, excellent customer service skills', 45000.00, 'Chicago, IL', NOW(), 'Employer 4', '2023-10-18', 'ENABLE'),
('Web Developer', 5, 3, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job4.png', 'We are looking for a web developer to create and maintain web applications.', 'Bachelor''s degree in Web Development or related field, experience in web development, proficiency in web technologies (e.g., HTML, CSS, JavaScript)', 70000.00, 'Seattle, WA', NOW(), 'Employer 5', '2023-10-19', 'ENABLE'),
('Database Administrator', 1, 3, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job3.jpg', 'We are seeking a database administrator to manage our database systems.', 'Bachelor''s degree in IT, experience in database administration, proficiency in SQL, knowledge of database management systems (e.g., Oracle, MySQL)', 72000.00, 'San Diego, CA', NOW(), 'Employer 6', '2023-10-20','ENABLE'),
('Cybersecurity Analyst', 2, 2, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job2.jpg', 'We have an opening for a cybersecurity analyst to protect our IT systems from security threats.', 'Bachelor''s degree in Cybersecurity or related field, experience in cybersecurity, knowledge of security tools and practices, incident response skills', 65000.00, 'Austin, TX', NOW(), 'Employer 7', '2023-10-21', 'ENABLE'),
('UI/UX Designer', 3, 2, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444783/job1.png', 'We are hiring a UI/UX designer to create user-friendly interfaces for our applications.', 'Bachelor''s degree in Design or related field, experience in UI/UX design, proficiency in design software (e.g., Adobe XD, Sketch), creative design skills', 60000.00, 'Denver, CO', NOW(), 'Employer 8', '2023-10-22', 'ENABLE'),
('IT Project Manager', 4, 3, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job3.jpg', 'We are looking for an IT project manager to oversee IT projects and teams.', 'Bachelor''s degree in IT or related field, project management experience, leadership skills, knowledge of project management tools', 85000.00, 'Boston, MA', NOW(), 'Employer 9', '2023-10-23', 'ENABLE'),
('Software Quality Assurance Analyst', 1, 1, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job7.png', 'We have an opening for a software quality assurance analyst to ensure the quality of our software products.', 'Bachelor''s degree in IT, quality assurance experience, testing skills, knowledge of testing tools (e.g., Selenium)', 55000.00, 'Dallas, TX', NOW(), 'Employer 10', '2023-10-24', 'ENABLE'),
('ByCript', 1, 2, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444922/job6.svg', 'We are looking for a skilled software engineer to join our development team.', 'Bachelor''s degree in Computer Science, 3+ years of experience in software development, proficiency in programming languages (e.g., Java, Python, C++), strong problem-solving skills', 75000.00, 'San Francisco, CA', NOW(), 'Employer 1', '2023-10-15', 'ENABLE'),
('Network', 2, 2, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job7.png', 'We are seeking a network administrator to manage our IT infrastructure.', 'Bachelor''s degree in IT or related field, experience in network administration, knowledge of Cisco networking, troubleshooting skills', 65000.00, 'Los Angeles, CA', NOW(), 'Employer 2', '2023-10-16', 'ENABLE'),
('Data AI', 3, 3, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job5.jpg', 'We are hiring a data scientist to analyze and interpret data for insights.', 'Master''s degree in Data Science or related field, expertise in data analysis tools (e.g., Python, R), machine learning knowledge, strong statistical skills', 80000.00, 'New York, NY', NOW(), 'Employer 3', '2023-10-17', 'ENABLE'),
('Support Specialist', 4, 2, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job3.jpg', 'We have an opening for an IT support specialist to assist users with technical issues.', 'Associate degree in IT, IT support experience, knowledge of Windows and macOS, excellent customer service skills', 45000.00, 'Chicago, IL', NOW(), 'Employer 4', '2023-10-18', 'ENABLE'),
('Developer', 5, 3, 'https://res.cloudinary.com/dzqoi9laq/image/upload/v1697444921/job4.png', 'We are looking for a web developer to create and maintain web applications.', 'Bachelor''s degree in Web Development or related field, experience in web development, proficiency in web technologies (e.g., HTML, CSS, JavaScript)', 70000.00, 'Seattle, WA', NOW(), 'Employer 5', '2023-10-19', 'ENABLE');





    INSERT INTO Blog (`blog_title`, `blog_content`, `blog_img`, `author`, `status`, `created_at`, `created_by`)
   VALUES
       ('Introduction to SQL', 'SQL is a powerful database query language used to manage and manipulate data in relational databases.', 'blog1.png', 'John Doe', 'ENABLE', NOW(), 'admin'),
       ('The Art of Data Modeling', 'Data modeling is a crucial step in database design that involves creating a blueprint for your database structure.', 'blog2.png', 'Jane Smith', 'ENABLE', NOW(), 'admin'),
       ('Web Development Tips', 'Learn some useful tips for web development, including best practices and tools.', 'blog3.png', 'Robert Johnson', 'ENABLE', NOW(), 'admin'),
       ('Machine Learning Fundamentals', 'Discover the basics of machine learning and its applications in various fields.', 'blog4.jpeg', 'Emily Davis', 'ENABLE', NOW(), 'admin'),
       ('Database Normalization Techniques', 'Normalization helps in organizing data efficiently within a relational database.', 'blog5.jpg', 'Michael Wilson', 'ENABLE', NOW(), 'admin'),
       ('Frontend vs. Backend Development', 'Explore the key differences between frontend and backend development in web development.', 'blog6.jpg', 'Sarah Brown', 'ENABLE', NOW(), 'admin'),
       ('Cybersecurity Best Practices', 'Learn how to protect your online assets and data with cybersecurity best practices.', 'blog7.png', 'Daniel White', 'ENABLE', NOW(), 'admin'),
       ('Cloud Computing Solutions', 'Discover the benefits of cloud computing and popular cloud service providers.', 'blog8.jpg', 'Jessica Lee', 'ENABLE', NOW(), 'admin'),
       ('Python Programming Guide', 'Get started with Python programming, one of the most popular programming languages.', 'blog9.jpg', 'Kevin Martin', 'ENABLE', NOW(), 'admin'),
       ('Data Analysis with Excel', 'Learn how to analyze and visualize data using Microsoft Excel.', 'blog10.jpg', 'Olivia Wilson', 'ENABLE', NOW(), 'admin');


INSERT INTO Specialization (`specialization_name`,`created_at`, `created_by`) VALUES
    ('Java', NOW(),"Admin"),
    ('Python', NOW(),"Admin"),
    ('HTML/CSS',NOW(),"Admin"),
    ('Database (SQL)',NOW(),"Admin"),
    ('Networking',NOW(),"Admin"),
    ('AI',NOW(),"Admin"),
    ('AWS',NOW(),"Admin"),
    ('iOS App Dev',NOW(),"Admin"),
    ('Front-End (JS)',NOW(),"Admin"),
    ('Node.js',NOW(),"Admin");



INSERT INTO Review_Rating (job_id, user_id, rating, review_text, created_at, created_by)
VALUES
  (1, 1, 5, 'Great job!', NOW(), 'User1'),
  (2, 2, 4, 'Good work!', NOW(), 'User2'),
  (3, 3, 3, 'Average performance.', NOW(), 'User3'),
  (4, 1, 2, 'Could be better.', NOW(), 'User4'),
  (5, 2, 4, 'Impressive!', NOW(), 'User1'),
  (1, 3, 5, 'Excellent!', NOW(), 'User2'),
  (2, 3, 4, 'Satisfactory.', NOW(), 'User3'),
  (3, 1, 3, 'Not bad.', NOW(), 'User4'),
  (4, 2, 2, 'Needs improvement.', NOW(), 'User1'),
  (5, 3, 4, 'Well done!', NOW(), 'User2'),
  (1, 2, 5, 'Outstanding!', NOW(), 'User3'),
  (2, 1, 4, 'Pretty good.', NOW(), 'User4'),
  (3, 2, 3, 'Okay.', NOW(), 'User1'),
  (4, 1, 2, 'Room for growth.', NOW(), 'User2'),
  (5, 2, 4, 'Nice job!', NOW(), 'User3'),
  (1, 1, 5, 'Exceptional!', NOW(), 'User4'),
  (2, 3, 4, 'Good effort!', NOW(), 'User1'),
  (3, 3, 3, 'Fair work.', NOW(), 'User2'),
  (4, 2, 2, 'Not great.', NOW(), 'User3'),
  (5, 1, 4, 'Impressed.', NOW(), 'User4');


  -- Insert 10 records with "CLOSE" status and real names
  INSERT INTO `contact` (`name`, `email`, `subject`, `message`, `status`, `created_at`, `created_by`)
  VALUES
      ('Sarah Johnson', 'sarah@example.com', 'Complaint', 'This is a closed complaint.', 'CLOSE', NOW(), 'Admin'),
      ('Michael Smith', 'michael@example.com', 'Inquiry', 'This is a closed inquiry.', 'CLOSE', NOW(), 'Admin'),
      ('Laura Davis', 'laura@example.com', 'Request', 'This is a closed request.', 'CLOSE', NOW(), 'Admin'),
      ('William Wilson', 'william@example.com', 'Feedback', 'This is a closed feedback.', 'CLOSE', NOW(), 'Admin'),
      ('Mia Brown', 'mia@example.com', 'Issue', 'This is a closed issue.', 'CLOSE', NOW(), 'Admin'),
      ('Daniel Parker', 'daniel@example.com', 'Comment', 'This is a closed comment.', 'CLOSE', NOW(), 'Admin'),
      ('Sophia Adams', 'sophia@example.com', 'Question', 'This is a closed question.', 'CLOSE', NOW(), 'Admin'),
      ('Olivia Hall', 'olivia@example.com', 'Suggestion', 'This is a closed suggestion.', 'CLOSE', NOW(), 'Admin'),
      ('Ethan Young', 'ethan@example.com', 'Problem', 'This is a closed problem.', 'CLOSE', NOW(), 'Admin'),
      ('Ava Adams', 'ava@example.com', 'Request', 'This is a closed request.', 'CLOSE', NOW(), 'Admin');

  -- Insert 10 records with "OPEN" status
  INSERT INTO `contact` (`name`, `email`, `subject`, `message`, `status`, `created_at`, `created_by`)
  VALUES
      ('John Doe', 'john@example.com', 'Question', 'This is an open question.', 'OPEN', NOW(), 'Admin'),
      ('Jane Smith', 'jane@example.com', 'Feedback', 'This is an open feedback.', 'OPEN', NOW(), 'Admin'),
      ('Alice Johnson', 'alice@example.com', 'Inquiry', 'This is an open inquiry.', 'OPEN', NOW(), 'Admin'),
      ('Bob Wilson', 'bob@example.com', 'Issue', 'This is an open issue.', 'OPEN', NOW(), 'Admin'),
      ('Ella Davis', 'ella@example.com', 'Request', 'This is an open request.', 'OPEN', NOW(), 'Admin'),
      ('Charlie Brown', 'charlie@example.com', 'Suggestion', 'This is an open suggestion.', 'OPEN', NOW(), 'Admin'),
      ('Olivia Parker', 'olivia@example.com', 'Complaint', 'This is an open complaint.', 'OPEN', NOW(), 'Admin'),
      ('Liam Hall', 'liam@example.com', 'Comment', 'This is an open comment.', 'OPEN', NOW(), 'Admin'),
      ('Emma Young', 'emma@example.com', 'Problem', 'This is an open problem.', 'OPEN', NOW(), 'Admin'),
      ('Sophia Adams', 'sophia@example.com', 'Request', 'This is an open request.', 'OPEN', NOW(), 'Admin');

