# --- !Ups

ALTER TABLE Checklist ADD published boolean DEFAULT 0;
ALTER TABLE Checklist ADD modifiedButNotPublished boolean DEFAULT 1;

# --- !Downs

ALTER TABLE Checklist DROP modifiedButNotPublished;
ALTER TABLE Checklist DROP published;

