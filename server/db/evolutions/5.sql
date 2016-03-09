# --- !Ups

ALTER TABLE Checklist ADD type varchar(50);

UPDATE Checklist SET type='cnav' WHERE id=1;
UPDATE Checklist SET type='rsi' WHERE id=2;
UPDATE Checklist SET type='msa' WHERE id=3;

ALTER TABLE Chapitre ADD delai_type varchar(50);
ALTER TABLE Chapitre ADD delai_min integer;
ALTER TABLE Chapitre ADD delai_max integer;
ALTER TABLE Chapitre ADD delai_unite varchar(50);

# --- !Downs

ALTER TABLE Chapitre DROP delai_unite;
ALTER TABLE Chapitre DROP delai_max;
ALTER TABLE Chapitre DROP delai_min;
ALTER TABLE Chapitre DROP delai_type;

ALTER TABLE Checklist DROP type;
