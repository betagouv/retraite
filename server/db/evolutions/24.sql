# --- !Ups

ALTER TABLE Caisse RENAME COLUMN linkLabel TO linkLabel1;
ALTER TABLE Caisse RENAME COLUMN linkUrl TO linkUrl1;
ALTER TABLE Caisse ADD linkLabel2 varchar(255);
ALTER TABLE Caisse ADD linkUrl2 varchar(255);
ALTER TABLE Caisse ADD linkLabel3 varchar(255);
ALTER TABLE Caisse ADD linkUrl3 varchar(255);

# --- !Downs

ALTER TABLE Caisse RENAME COLUMN linkUrl1 TO linkUrl;
ALTER TABLE Caisse RENAME COLUMN linkLabel1 TO linkLabel;
ALTER TABLE Caisse DROP linkUrl3;
ALTER TABLE Caisse DROP linkLabel3;
ALTER TABLE Caisse DROP linkUrl2;
ALTER TABLE Caisse DROP linkLabel2;
