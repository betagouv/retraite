# --- !Ups

create table Chapitre (
    id bigint not null auto_increment,
    titre varchar(255),
    checklist_id bigint,
    primary key (id)
);

create table Checklist (
    id bigint not null auto_increment,
    nom varchar(255),
    primary key (id)
);

alter table Chapitre 
    add index FK_inv29dyn058g9uruepd0era05 (checklist_id), 
    add constraint FK_inv29dyn058g9uruepd0era05 foreign key (checklist_id) references Checklist (id);

INSERT INTO Checklist (id, nom) VALUES (1, 'CNAV'), (2, 'RSI'), (3, 'MSA');

# CNAV
INSERT INTO Chapitre (checklist_id, titre) VALUES (1, 'Je reconstitue ma carrière');
INSERT INTO Chapitre (checklist_id, titre) VALUES (1, 'Je contacte mon ancien régime');
INSERT INTO Chapitre (checklist_id, titre) VALUES (1, 'Je contacte mes autres régimes');

# RSI
INSERT INTO Chapitre (checklist_id, titre) VALUES (2, 'Je prépare ma cessation d\'activité');
INSERT INTO Chapitre (checklist_id, titre) VALUES (2, 'Je reconstiture ma carrière');
INSERT INTO Chapitre (checklist_id, titre) VALUES (2, 'Je contacte mes autres complémentaires');

# MSA
INSERT INTO Chapitre (checklist_id, titre) VALUES (3, 'Je prépare ma cessation d\'activité');
INSERT INTO Chapitre (checklist_id, titre) VALUES (3, 'Je reconstiture ma carrière');
INSERT INTO Chapitre (checklist_id, titre) VALUES (3, 'Je contacte mes autres complémentaires');

# --- !Downs

ALTER TABLE Chapitre DROP foreign key FK_inv29dyn058g9uruepd0era05;
ALTER TABLE Chapitre DROP INDEX FK_inv29dyn058g9uruepd0era05;

DROP TABLE Checklist; 
DROP TABLE Chapitre; 