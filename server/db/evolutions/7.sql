# --- !Ups

ALTER TABLE Chapitre ADD closedInEdition boolean DEFAULT 0;

# --- !Downs

ALTER TABLE Chapitre DROP closedInEdition;

