# --- !Ups

ALTER TABLE Chapitre ADD texteIntro text;
ALTER TABLE Chapitre ADD parcoursDematDifferent boolean DEFAULT false;
ALTER TABLE Chapitre ADD parcours text;
ALTER TABLE Chapitre ADD parcoursDemat text;
ALTER TABLE Chapitre ADD texteComplementaire text;
ALTER TABLE Chapitre ADD notes text;

# --- !Downs

ALTER TABLE Chapitre DROP notes;
ALTER TABLE Chapitre DROP texteComplementaire;
ALTER TABLE Chapitre DROP parcoursDemat;
ALTER TABLE Chapitre DROP parcours;
ALTER TABLE Chapitre DROP parcoursDematDifferent;
ALTER TABLE Chapitre DROP texteIntro;

