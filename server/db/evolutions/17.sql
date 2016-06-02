# --- !Ups

ALTER TABLE Chapitre ADD texteActions text;
ALTER TABLE Chapitre ADD texteModalites text;
ALTER TABLE Chapitre ADD texteInfos text;

# --- !Downs

ALTER TABLE Chapitre DROP texteInfos;
ALTER TABLE Chapitre DROP texteModalites;
ALTER TABLE Chapitre DROP texteActions;
