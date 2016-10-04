# --- !Ups

create table RetraiteUser (
    id bigserial not null,
    login varchar(255),
    password varchar(255),
    authorizedChecklists varchar(255),
    primary key (id)
);

INSERT INTO RetraiteUser (login, password,authorizedChecklists) VALUES 
	('cnav','cnav2015','CNAV'), 
	('msa','msa2015','MSA'), 
	('rsi','rsi2015','RSI'), 
	('retraite','je découvre mes démarches retraite','CNAV,MSA,RSI');

# --- !Downs

DROP TABLE RetraiteUser; 
