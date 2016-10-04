# --- !Ups

ALTER TABLE Chapitre ADD closedInEdition boolean DEFAULT false;

# --- !Downs

ALTER TABLE Chapitre DROP closedInEdition;

