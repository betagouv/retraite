# --- !Ups

ALTER TABLE Caisse ADD linkLabel varchar(255);
ALTER TABLE Caisse ADD linkUrl varchar(255);

# --- !Downs

ALTER TABLE Caisse DROP linkUrl;
ALTER TABLE Caisse DROP linkLabel;
