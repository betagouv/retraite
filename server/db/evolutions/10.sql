# --- !Ups

ALTER TABLE Chapitre ADD description varchar(255);

# --- !Downs

ALTER TABLE Chapitre DROP description;

