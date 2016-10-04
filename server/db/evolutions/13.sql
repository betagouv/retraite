# --- !Ups

ALTER TABLE Checklist ADD published boolean DEFAULT false;
ALTER TABLE Checklist ADD modifiedButNotPublished boolean DEFAULT true;

# --- !Downs

ALTER TABLE Checklist DROP modifiedButNotPublished;
ALTER TABLE Checklist DROP published;

