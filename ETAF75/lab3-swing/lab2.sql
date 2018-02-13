PRAGMA foreign_keys=OFF;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS theaters;
DROP TABLE IF EXISTS performances;
DROP TABLE IF EXISTS reservations;
PRAGMA foreign_keys=ON;


CREATE TABLE users(
u_name         TEXT,
given_name     TEXT,
phn_nbr        TEXT,
address        TEXT,
PRIMARY KEY(u_name)
);


CREATE TABLE theaters(
t_name         TEXT,
seat_amount     INT,
PRIMARY KEY(t_name)
);


CREATE TABLE performances(
m_name                TEXT,
date                  DATE,
t_name                TEXT,
PRIMARY KEY(m_name, date),
FOREIGN KEY(t_name) REFERENCES theaters(t_name)
);


CREATE TABLE reservations(
res_id                INTEGER,
m_name                TEXT,
date                  DATE,
u_name                TEXT,
PRIMARY KEY(res_id),
FOREIGN KEY(m_name, date) REFERENCES performances(m_name, date),
FOREIGN KEY(u_name) REFERENCES users(u_name)
);

INSERT INTO users(u_name, given_name, phn_nbr, address)
VALUES          ('anon', 'Frank', '123456789', 'Gata 1'),
                ('femmeanon', 'Anna', '98765432', 'Gata 2'),  
                ('derp', 'Bob', '012345678', 'Gata 3'),
                ('derpina', 'Lisa', '876543210', 'Gata 4');


INSERT INTO theaters(t_name, seat_amount)
VALUES          ('Bergakungen', 400),
		('Ingen Plats', 0),
                ('Valhalla', 250),
                ('Film Paradiset', 300);


INSERT INTO performances(m_name, date, t_name)
VALUES          ('Spirited Away', '2018-02-03' , 'Bergakungen'),
 		('Trångt', '2018-02-03' , 'Ingen Plats'),
                ('Devil May Cry', '2018-02-04', 'Bergakungen'),
                ('Spirited Away', '2018-02-05', 'Valhalla'),
                ('Devil May Cry', '2018-02-06', 'Valhalla'),
                ('Spirited Away', '2018-02-07', 'Film Paradiset'),
                ('Devil May Cry', '2018-02-07', 'Film Paradiset');
                
INSERT INTO reservations(res_id, date, m_name, u_name)
VALUES          (NULL, '2018-02-03', 'Spirited Away', 'anon'),
 		(NULL, '2018-02-03', 'Spirited Away', 'derp'),
                (NULL, '2018-02-04', 'Devil May Cry', 'anon'),
                (NULL, '2018-02-05', 'Spirited Away', 'femmeanon'),
                (NULL, '2018-02-06', 'Devil May Cry', 'derp'),
                (NULL, '2018-02-07', 'Devil May Cry', 'derpina');
