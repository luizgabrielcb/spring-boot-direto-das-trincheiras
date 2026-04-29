INSERT INTO user_service.profile (id, description, name) VALUES (1, 'Administrador', 'Admin');
INSERT INTO user_service.profile (id, description, name) VALUES (2, 'Regular user with standard permissions', 'Regular user');
INSERT INTO user_service.user (id, email, first_name, last_name, password, roles) VALUES (11, 'tanjiro.oni2006@kimetsu.com', 'Tanjiro', 'Kamado', '{bcrypt}$2a$10$QRaKpNct1DI8N844HR6R5egEF9toedQJ8hBoeHJOFetcdtK9X40cu', '');
INSERT INTO user_service.user (id, email, first_name, last_name, password, roles) VALUES (13, 'gojo.satoru@jujutsu.com', 'Satoru', 'Gojo', '', '');
INSERT INTO user_service.user (id, email, first_name, last_name, password, roles) VALUES (14, 'ttmy.pillar@gmail.com', 'Thaemy', 'Melo', '{bcrypt}$2a$10$KH88AA8xkUV1.aJwQQPiX.95U2xTyPvoMiElBmu4iMYbYv8wQrZ6.', 'ADMIN');
INSERT INTO user_service.user_profile (id, profile_id, user_id) VALUES (4, 1, 11);
