# --- !Ups

create table Checklist (
    id bigserial not null,
    nom varchar(255),
    primary key (id)
);

create table Chapitre (
    id bigserial not null,
    titre varchar(255),
    checklist_id bigint references Checklist (id),
    primary key (id)
);

CREATE INDEX FK_inv29dyn058g9uruepd0era05 ON Chapitre USING hash (checklist_id);

INSERT INTO Checklist (id, nom) VALUES (1, 'CNAV'), (2, 'RSI'), (3, 'MSA');

# CNAV
INSERT INTO Chapitre (checklist_id, titre) VALUES (1, 'Je reconstitue ma carrière');
INSERT INTO Chapitre (checklist_id, titre) VALUES (1, 'Je contacte mon ancien régime');
INSERT INTO Chapitre (checklist_id, titre) VALUES (1, 'Je contacte mes autres régimes');

# RSI
INSERT INTO Chapitre (checklist_id, titre) VALUES (2, 'Je prépare ma cessation d''activité');
INSERT INTO Chapitre (checklist_id, titre) VALUES (2, 'Je reconstiture ma carrière');
INSERT INTO Chapitre (checklist_id, titre) VALUES (2, 'Je contacte mes autres complémentaires');

# MSA
INSERT INTO Chapitre (checklist_id, titre) VALUES (3, 'Je prépare ma cessation d''activité');
INSERT INTO Chapitre (checklist_id, titre) VALUES (3, 'Je reconstiture ma carrière');
INSERT INTO Chapitre (checklist_id, titre) VALUES (3, 'Je contacte mes autres complémentaires');

# --- !Downs

DROP INDEX FK_inv29dyn058g9uruepd0era05;
 
DROP TABLE Chapitre;
DROP TABLE Checklist; 