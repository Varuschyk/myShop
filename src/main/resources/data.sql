INSERT INTO shopping_items(id, name, price, created_at) VALUES(1, 'Microwave', 50, CURRENT_DATE);
INSERT INTO shopping_items(id, name, price, created_at) VALUES(2, 'Fridge', 55, CURRENT_DATE);

-- INSERT INTO roles(id, name) VALUES(1, 'USER');
-- INSERT INTO roles(id, name) VALUES(2, 'ADMIN');

INSERT INTO users(id, name, surname, password, email, role) VALUES(7, 'Alex', 'Var', '$2a$12$Tdd93Gvqh4KSp/qe9i1AneCAj1Es/GT9vLw7U1NmQfbIcONCfwy9C', 'alexVar@gmail.com', 'ADMIN');
INSERT INTO users(id, name, surname, password, email, role) VALUES(8, 'Ruslana', 'Var', '$2a$12$Tdd93Gvqh4KSp/qe9i1AneCAj1Es/GT9vLw7U1NmQfbIcONCfwy9C', 'ruslanaVar@gmail.com', 'ADMIN');
INSERT INTO users(id, name, surname, password, email, role) VALUES(9, 'Misha', 'Kos', '$2a$12$Tdd93Gvqh4KSp/qe9i1AneCAj1Es/GT9vLw7U1NmQfbIcONCfwy9C', 'mishaKos@gmail.com', 'USER');
INSERT INTO users(id, name, surname, password, email, role) VALUES(10, 'Kirill', 'Nig', '$2a$12$Tdd93Gvqh4KSp/qe9i1AneCAj1Es/GT9vLw7U1NmQfbIcONCfwy9C', 'kirillNig@gmail.com', 'USER');

