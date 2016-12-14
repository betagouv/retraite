# --- !Ups

ALTER TABLE Caisse ADD linkType1 varchar(255);
ALTER TABLE Caisse ADD linkType2 varchar(255);
ALTER TABLE Caisse ADD linkType3 varchar(255);

# --- !Downs

ALTER TABLE Caisse DROP linkType1;
ALTER TABLE Caisse DROP linkType2;
ALTER TABLE Caisse DROP linkType3;
