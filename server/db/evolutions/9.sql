# --- !Ups

ALTER TABLE Chapitre ADD delai_sa_type varchar(50);
ALTER TABLE Chapitre ADD delai_sa_min integer;
ALTER TABLE Chapitre ADD delai_sa_max integer;
ALTER TABLE Chapitre ADD delai_sa_unite varchar(50);

# --- !Downs

ALTER TABLE Chapitre DROP delai_sa_unite;
ALTER TABLE Chapitre DROP delai_sa_max;
ALTER TABLE Chapitre DROP delai_sa_min;
ALTER TABLE Chapitre DROP delai_sa_type;

